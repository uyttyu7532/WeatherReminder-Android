package com.example.tourweatherreminder

import android.os.AsyncTask
import android.util.Log
import com.example.tourweatherreminder.model.ForAsync
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URL
import java.util.*


fun startJSONTask(
    title: String,
    place: String,
    latitude: Double,
    longitude: Double,
    timestamp: Long,
    date: String
) {
    val task = MainAsyncTask(context = MainActivity())

    var url =
        URL("https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&&appid=0278d360e035caa40fc3debf63523512&units=metric&exclude=minutely,current")
    var forAsync = ForAsync(url, title, date, timestamp, place, latitude, longitude)

    task.execute(forAsync)
}

// 비동기
class MainAsyncTask(context: MainActivity) : AsyncTask<ForAsync, Unit, ForAsync>() {

    override fun doInBackground(vararg params: ForAsync): ForAsync? {

        Log.i("위도파싱", params[0].latitude.toString())
        Log.i("경도파싱", params[0].longitude.toString())



        val doc = Jsoup.connect(params[0].url.toString()).ignoreContentType(true).get()
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
            parseDaily(json,tempTime(params[0].date))
            findMinDiffDt(dailyWeatherArray)
        }
        return params[0]

    }

    override fun onPostExecute(result: ForAsync) {
        super.onPostExecute(result)
        MainActivity.AddSchedule.addSchedule(
            leastDiffData!!.icon,
            result.title,
            result.date,
            leastDiffData!!.temp,
            leastDiffData!!.rain,
            result.place
        )
    }

    fun findMinDiffDt(WeatherArray: ArrayList<WeatherData>): WeatherData {
        var diff: Long = Math.abs(WeatherArray[0].dt - timeStamp!!)

        var least: Long = diff
//        Log.i("첫diff", diff.toString())
//        Log.i("첫least", least.toString())
        leastDiffData = WeatherArray[0]

        for (i in 1..WeatherArray.size - 1) {
//            Log.i("WeatherArray[i].dt", WeatherArray[i].dt.toString())
            diff = Math.abs(WeatherArray[i].dt - timeStamp!!)
//            Log.i("diff", diff.toString())
            if (diff < least) {
                least = diff
                leastDiffData = WeatherArray[i]
//                Log.i("least", least.toString())
//                Log.i("leastDiffData", leastDiffData.toString())
            }

        }
        Log.i("최종least파싱", least.toString())
        Log.i("최종leastDiffData파싱", leastDiffData.toString())

        return leastDiffData as WeatherData
    }


    fun parseDaily(json: JSONObject, tempTime:String) {
        dailyWeatherArray = arrayListOf<WeatherData>()
        val dailyWeather = json.getJSONArray("daily")
//    Log.i("파싱daily", dailyWeather.toString())

        for (i in 0 until dailyWeather.length()) {

            var dt: Long = dailyWeather.getJSONObject(i).getString("dt").toLong()
            var temp: Float =
                dailyWeather.getJSONObject(i).getJSONObject("temp").getString(tempTime).toFloat()
            Log.i("eve tempTime파싱",dailyWeather.getJSONObject(i).getJSONObject("temp").getString("eve").toString())
            Log.i("morn tempTime파싱",dailyWeather.getJSONObject(i).getJSONObject("temp").getString("morn").toString())
            Log.i("day tempTime파싱",dailyWeather.getJSONObject(i).getJSONObject("temp").getString("day").toString())
            Log.i("night tempTime파싱",dailyWeather.getJSONObject(i).getJSONObject("temp").getString("night").toString())
            var icon: String =
                dailyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0)
                    .getString("icon")

//        Log.i("파싱dt", dailyWeather.getJSONObject(i).getString("dt"))
//        Log.i("파싱temp", dailyWeather.getJSONObject(i).getJSONObject("temp").getString("day"))
//        Log.i(
//            "파싱icon",
//            dailyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")
//        )
            var tmp = dailyWeather.getJSONObject(i)
            var rain: Float? = 0.0f
            if (!tmp.isNull("rain")) {
                rain = dailyWeather.getJSONObject(i).getString("rain").toFloat()
//            Log.i("파싱dailyrain", dailyWeather.getJSONObject(i).getString("rain"))
            } else {
                rain = 0.0f
//            Log.i("파싱dailyrain", "0")
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
//            Log.i("파싱dt", hourlyWeather.getJSONObject(i).getString("dt"))
//        Log.i("파싱temp", hourlyWeather.getJSONObject(i).getString("temp"))
//        Log.i(
//            "파싱icon",
//            hourlyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")
//        )
            var rain: Float? = null
            var tmp = hourlyWeather.getJSONObject(i)
            if (!tmp.isNull("rain")) {
                rain = tmp.getJSONObject("rain").getString("1h").toFloat()
//                Log.i("파싱rain", tmp.getJSONObject("rain").getString("1h"))
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


    fun tempTime(date: String):String {
        // eve, morning, day, night 구분하기 위해서 시간별로 daily 날씨를 다르게 표시해야함!
        // timestamp에서 시간들을 뽑아올 수 있느냐...(시차고려) -> add에서 애초에 분류를 해서 갖고오자!
        // eve 24:00-06:00
        // morning 06:00-12:00
        // day 12:00-18:00
        // night 18:00-24:00

        var tempTime="null"

        var hour = date?.split(":")?.get(0)?.split(" ")?.get(1)
        Log.i("hour파싱",hour)
        if(hour=="00"||hour=="01"||hour=="02"||hour=="03"||hour=="04"||hour=="05"){
            tempTime = "eve"
        }
        else if(hour=="06"||hour=="07"||hour=="08"||hour=="09"||hour=="10"||hour=="11"){
            tempTime = "morn"
        }
        else if(hour=="12"||hour=="13"||hour=="14"||hour=="15"||hour=="16"||hour=="17"){
            tempTime = "day"
        }
        else{
            tempTime = "night"
        }
        Log.i("tempTime파싱",tempTime)
        return tempTime

    }

}