package com.example.tourweatherreminder

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URL

//var latitude: Double? = 37.497886
//var longitude: Double? = 127.027416

var dailyWeatherArray = arrayListOf<WeatherData>()
var hourlyWeatherArray = arrayListOf<WeatherData>()

var now = System.currentTimeMillis()/1000
var isHourly = false
var timeStamp:Long?=null



fun startJSONTask(latitude: Double, longitude: Double, timestamp: Long) {
    val task = MyAsyncTask(context = MainActivity())
    Log.i("파싱위치", latitude.toString())
    Log.i("파싱위치", longitude.toString())

    timeStamp=timestamp/1000
    var timeDiff = Math.abs(now-timeStamp!!)
    isHourly = timeDiff<2*86400 // 2일(초)
    Log.i("2일", now.toString()+" "+timeStamp.toString()+" "+isHourly.toString()+" "+timeDiff)

    task.execute(URL("https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&&appid=0278d360e035caa40fc3debf63523512&units=metric&exclude=minutely,current"))
}


class MyAsyncTask(context: MainActivity) : AsyncTask<URL, Unit, Unit>() {

    override fun doInBackground(vararg params: URL?) {

        val doc = Jsoup.connect(params[0].toString()).ignoreContentType(true).get()
        val json = JSONObject(doc.text())
//        var now = System.currentTimeMillis()
//        var timeDiff = now!!.minus(timeStamp!!)
        if (isHourly) {
//            Log.i("파싱 if",timeDiff.toString())
            parseHourly(json)
        } else {
//            Log.i("파싱 else",timeDiff.toString())
            parseDaily(json)
        }


    }


    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

        if(isHourly){
            findMinDiffDt(hourlyWeatherArray)
        }else{
            findMinDiffDt(dailyWeatherArray)
        }


    }
}

fun findMinDiffDt(WeatherArray:ArrayList<WeatherData>):WeatherData{

    var diff:Long = Math.abs(WeatherArray[0].dt - timeStamp!!)

    var least:Long = diff
    Log.i("첫diff",diff.toString())
    Log.i("첫least",least.toString())
    var leastDiffData:WeatherData=WeatherArray[0]

    for(i in 1..WeatherArray.size-1){
        Log.i("WeatherArray[i].dt",WeatherArray[i].dt.toString())
        diff =Math.abs(WeatherArray[i].dt - timeStamp!!)
        Log.i("diff",diff.toString())
        if(diff<least){
            least=diff
            leastDiffData= WeatherArray[i]
            Log.i("least",least.toString())
            Log.i("leastDiffData",leastDiffData.toString())
        }

    }
    Log.i("최종least",least.toString())
    Log.i("최종leastDiffData",leastDiffData.toString())

    return leastDiffData
}


fun parseDaily(json: JSONObject) {
    dailyWeatherArray = arrayListOf<WeatherData>()
    val dailyWeather = json.getJSONArray("daily")
//    Log.i("파싱daily", dailyWeather.toString())

    for (i in 0 until dailyWeather.length()) {

        var dt: Long = dailyWeather.getJSONObject(i).getString("dt").toLong()
        var temp: Float =
            dailyWeather.getJSONObject(i).getJSONObject("temp").getString("day").toFloat()
        var icon: String =
            dailyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")

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
        var icon: String = hourlyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0)
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

        hourlyWeatherArray.add(WeatherData(dt,temp,icon,rain))
    }
    Log.i("hourly파싱", hourlyWeatherArray.toString())
}


