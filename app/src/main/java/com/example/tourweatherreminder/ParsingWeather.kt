package com.example.tourweatherreminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.tourweatherreminder.db.AppDatabase
import com.example.tourweatherreminder.db.entity.ScheduleEntity
import com.example.tourweatherreminder.model.makeNotification
import com.example.tourweatherreminder.model.notificationContent
import com.example.tourweatherreminder.model.notificationResultCnt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.jsoup.Jsoup
import java.lang.ref.WeakReference
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


// 비동기
class MainAsyncTask(context: Context) : AsyncTask<ScheduleEntity, Unit, ScheduleEntity>() {

    private var contextRef: WeakReference<Context>? = null
    val mContext = context
    var isPast = false

    override fun doInBackground(vararg params: ScheduleEntity): ScheduleEntity? {
        // 예전일정이라면
        if (params[0].timestamp < System.currentTimeMillis()+1000*60*60*9) {
            isPast = true
            return params[0]
        }

        else {
            isPast = false

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
    }


    override fun onPostExecute(result: ScheduleEntity) {
        super.onPostExecute(result)
        notificationResultCnt++
        Log.i("로그 result","${result}")
        Log.i("로그 notificationResultCnt","${notificationResultCnt}")
        contextRef = WeakReference(mContext)
        val context = contextRef!!.get()
        if (context != null) {
            val scheduleEntity : ScheduleEntity
            val appDatabase = AppDatabase?.getInstance(context)?.DataDao()
            Log.i("로그 과거인가", isPast.toString())
            if(isPast){
                scheduleEntity = ScheduleEntity(
                    result.weather,
                    result.title,
                    result.date,
                    result.timestamp,
                    result.temp,
                    result.rain,
                    result.latitude,
                    result.longitude,
                    result.place
                )
                Log.i("로그 과거데이터", scheduleEntity.toString())
            }else {
                scheduleEntity = ScheduleEntity(
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
            }

            CoroutineScope(Dispatchers.IO).launch {

                // title이 이미 db에 존재하는 지 확인
                // 있으면 true, 없으면 false
                val isTitle = appDatabase?.getScheduleByTitle(result.title)

                if (isTitle == null) { // title이 없다면
                    appDatabase?.insertSchedule(scheduleEntity)
                    Log.i("로그 ", "인서트")
                } else {

                    if (isTitle.weather != null && isTitle.weather != scheduleEntity.weather) {
                        notificationContent += "${isTitle.title} 날씨 정보가 변경되었습니다.\n"
                        Log.i("로그 ", "날씨바뀜")
                    }
                    appDatabase?.updateSchedule(scheduleEntity)
                    Log.i("로그 ", "업데이트")
                }
            }
        }


        if (notificationResultCnt == ScheduleList.size) {
            makeNotification()
            val current = LocalDateTime.now().plusHours(9)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = current.format(formatter)
            updatetime.setText(formatted)
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