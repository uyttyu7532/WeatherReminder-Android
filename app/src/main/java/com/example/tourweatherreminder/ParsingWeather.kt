package com.example.tourweatherreminder

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URL

var latitude: Double? = null
var longitude: Double? = null


fun startJSONTask() {
    val task = MyAsyncTask(context = MainActivity())
    task.execute(URL("https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&&appid=0278d360e035caa40fc3debf63523512&units=metric&exclude=daily,minutely,current"))
}

class MyAsyncTask(context: MainActivity) : AsyncTask<URL, Unit, Unit>() {

    override fun doInBackground(vararg params: URL?) {

        val doc = Jsoup.connect(params[0].toString()).ignoreContentType(true).get()
        Log.i("파싱1", doc.text())
        val json = JSONObject(doc.text())
        val hourly = json.getJSONArray("hourly")
        Log.i("파싱2", hourly.toString())
        val weather = hourly.getString(0)
        Log.i("파싱3",weather.toString())



    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

    }

}