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
    task.execute(URL("https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&&appid=0278d360e035caa40fc3debf63523512&units=metric&exclude=daily,minutely,current"))
}

class MyAsyncTask(context: MainActivity) : AsyncTask<URL, Unit, Unit>() {

    override fun doInBackground(vararg params: URL?) {

        val doc = Jsoup.connect(params[0].toString()).ignoreContentType(true).get()
        Log.i("파싱1", doc.text())
        val json = JSONObject(doc.text())
        val hourlyWeather = json.getJSONArray("hourly")
        Log.i("파싱2", hourlyWeather.toString())

//        Log.i("파싱3",hourlyWeather[0].toString())
//        Log.i("파싱3",hourlyWeather.getString(1))
//        Log.i("파싱3",hourlyWeather.getString(2))
//
        for(i in 0 until hourlyWeather.length()){
            Log.i("파싱dt",hourlyWeather.getJSONObject(i).getString("dt"))
            Log.i("파싱temp",hourlyWeather.getJSONObject(i).getString("temp"))
            Log.i("파싱icon",hourlyWeather.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"))
            var tmp = hourlyWeather.getJSONObject(i)
            if( !tmp.isNull("rain")){
                Log.i("파싱rain",tmp.getJSONObject("rain").getString("1h"))
            }
        }

//        Log.i("파싱3",hourlyWeather.names().toString())








    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

    }

}


