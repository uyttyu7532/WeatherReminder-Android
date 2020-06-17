package com.example.tourweatherreminder

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.tourweatherreminder.db.AppDatabase
import com.example.tourweatherreminder.db.entity.ScheduleEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.jsoup.Jsoup
import java.lang.ref.WeakReference
import java.net.URL
import java.util.*


// 비동기
class MainAsyncTask(context: Context) : AsyncTask<ScheduleEntity, Unit, ScheduleEntity>() {

    private var contextRef: WeakReference<Context>? = null
    val mContext = context

    override fun doInBackground(vararg params: ScheduleEntity): ScheduleEntity? {

            var url =
                URL("https://api.openweathermap.org/data/2.5/onecall?lat=${params[0].latitude}&lon=${params[0].longitude}&&appid=0278d360e035caa40fc3debf63523512&units=metric&exclude=minutely,current")
            val doc = Jsoup.connect(url.toString()).ignoreContentType(true).get()
            val json = JSONObject(doc.text())

            timeStamp = params[0].timestamp / 1000 - getGMTOffset(json)
            Log.i("now파싱", now.toString())
            Log.i("timeStamp파싱", timeStamp.toString())
            var timeDiff = Math.abs(now - timeStamp!!)
            Log.i("timeDiff파싱", timeDiff.toString())
            isHourly = timeDiff < 2 * 86400 // 2일(초)

            if (isHourly) {
                parseHourly(json)
                findMinDiffDt(hourlyWeatherArray)
            } else {
                parseDaily(json, tempTime(params[0].date))
                findMinDiffDt(dailyWeatherArray)
            }

        return params[0]

    }

    override fun onPostExecute(result: ScheduleEntity) {
        super.onPostExecute(result)

        contextRef = WeakReference(mContext)
        val context = contextRef!!.get()
        if (context != null) {
            val appDatabase = AppDatabase
            val scheduleEntity = ScheduleEntity(
                leastDiffData!!.icon,
                result.title,
                result.date,
                result.timestamp,
                leastDiffData!!.temp,
                leastDiffData!!.rain,
                result.latitude,
                result.longitude,
                result.place
            )
            CoroutineScope(Dispatchers.IO).launch {
                appDatabase?.getInstance(context)?.DataDao()?.insertSchedule(scheduleEntity)
                ScheduleList?.add(scheduleEntity)
            }
        }
    }


    fun findMinDiffDt(WeatherArray: ArrayList<WeatherData>): WeatherData {
        var diff: Long = Math.abs(WeatherArray[0].dt - timeStamp!!)

        var least: Long = diff
        leastDiffData = WeatherArray[0]

        for (i in 1..WeatherArray.size - 1) {
            diff = Math.abs(WeatherArray[i].dt - timeStamp!!)
            if (diff < least) {
                least = diff
                leastDiffData = WeatherArray[i]
            }

        }
        return leastDiffData as WeatherData
    }


    fun parseDaily(json: JSONObject, tempTime: String) {
        dailyWeatherArray = arrayListOf<WeatherData>()
        val dailyWeather = json.getJSONArray("daily")
//    Log.i("파싱daily", dailyWeather.toString())

        for (i in 0 until dailyWeather.length()) {

            var dt: Long = dailyWeather.getJSONObject(i).getString("dt").toLong()
            var temp: Float =
                dailyWeather.getJSONObject(i).getJSONObject("temp").getString(tempTime).toFloat()
            var icon: String =
                dailyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0)
                    .getString("icon")

            var tmp = dailyWeather.getJSONObject(i)
            var rain: Float? = 0.0f
            if (!tmp.isNull("rain")) {
                rain = dailyWeather.getJSONObject(i).getString("rain").toFloat()
            } else {
                rain = 0.0f
            }
            dailyWeatherArray.add(WeatherData(dt, temp, icon, rain))
        }
        Log.i("daily파싱파싱", dailyWeatherArray.toString())
    }

    fun parseHourly(json: JSONObject) {
        hourlyWeatherArray = arrayListOf<WeatherData>()
        val hourlyWeather = json.getJSONArray("hourly")
        for (i in 0 until hourlyWeather.length()) {
            var dt: Long = hourlyWeather.getJSONObject(i).getString("dt").toLong()
            var temp: Float = hourlyWeather.getJSONObject(i).getString("temp").toFloat()
            var icon: String =
                hourlyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0)
                    .getString("icon")
            var rain: Float? = null
            var tmp = hourlyWeather.getJSONObject(i)
            if (!tmp.isNull("rain")) {
                rain = tmp.getJSONObject("rain").getString("1h").toFloat()
            } else {
                rain = 0.0f
            }
            hourlyWeatherArray.add(WeatherData(dt, temp, icon, rain))
        }
        Log.i("hourly파싱", hourlyWeatherArray.toString())
    }


    fun getGMTOffset(json: JSONObject): Long {
        var GMToffset = json.getString("timezone_offset")
        Log.i("파싱offset", GMToffset)

        return GMToffset.toLong()
    }


    fun tempTime(date: String): String {

        var tempTime = "null"

        var hour = date?.split(":")?.get(0)?.split(" ")?.get(1)
        Log.i("hour파싱", hour)

        when (hour) {
            "17", "18", "19", "20", "21" -> tempTime = "eve"
            "06", "07", "08", "09", "10", "11" -> tempTime = "morn"
            "12", "13", "14", "15", "16" -> tempTime = "day"
            else -> tempTime = "night"
        }
        Log.i("tempTime파싱", tempTime)
        return tempTime

    }

}