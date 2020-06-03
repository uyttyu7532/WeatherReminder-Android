package com.example.tourweatherreminder

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URL

var latitude: Double? = 37.497886
var longitude: Double? = 127.027416

fun startJSONTask() {
    val task = MyAsyncTask(context = MainActivity())
    task.execute(URL("https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&&appid=0278d360e035caa40fc3debf63523512&units=metric&exclude=minutely,current"))
}

class MyAsyncTask(context: MainActivity) : AsyncTask<URL, Unit, Unit>() {

    override fun doInBackground(vararg params: URL?) {

        val doc = Jsoup.connect(params[0].toString()).ignoreContentType(true).get()
        val json = JSONObject(doc.text())


        val hourlyWeather = json.getJSONArray("hourly")
        for(i in 0 until hourlyWeather.length()){
            Log.i("파싱dt",hourlyWeather.getJSONObject(i).getString("dt"))
            Log.i("파싱temp",hourlyWeather.getJSONObject(i).getString("temp"))
            Log.i("파싱icon",hourlyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"))
            var tmp = hourlyWeather.getJSONObject(i)
            if( !tmp.isNull("rain")){
                Log.i("파싱rain",tmp.getJSONObject("rain").getString("1h"))
            }
        }

        val dailyWeather = json.getJSONArray("daily")
        Log.i("파싱daily",dailyWeather.toString())

        for(i in 0 until dailyWeather.length()) {
            Log.i("파싱dt",dailyWeather.getJSONObject(i).getString("dt"))
            Log.i("파싱temp",dailyWeather.getJSONObject(i).getJSONObject("temp").getString("day"))
            Log.i("파싱icon",dailyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"))
            var tmp = dailyWeather.getJSONObject(i)
            if (!tmp.isNull("rain")) {
                Log.i("파싱dailyrain", dailyWeather.getJSONObject(i).getString("rain"))
            }
            else{
                Log.i("파싱dailyrain","0")
            }
        }











    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

    }

}


