package com.example.tourweatherreminder

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourweatherreminder.MainActivity.AddSchedule.addSchedule
import com.example.tourweatherreminder.MainActivity.AddSchedule.resetAdapter
import com.example.tourweatherreminder.model.ForAsync
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URL
import java.util.*


var dailyWeatherArray = arrayListOf<WeatherData>()
var hourlyWeatherArray = arrayListOf<WeatherData>()

var leastDiffData: WeatherData? = null

var now = System.currentTimeMillis() / 1000
var isHourly = false
var timeStamp: Long? = null

lateinit var timelineRecyclerAdapter: TimelineRecyclerAdapter

lateinit var recyclerView: RecyclerView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = this.findViewById(R.id.recycler_view)

        resetAdapter()

        // 일정추가 FAB
        addFAB.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, 100)

        }
    }

    // addActivity에서 돌아온 결과
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    var title = data!!.getStringExtra("title")
                    var place = data!!.getStringExtra("placeName")
                    var latitude = data!!.getDoubleExtra("latitude", 0.0)
                    var longitude = data!!.getDoubleExtra("longitude", 0.0)
                    var timestamp = data!!.getLongExtra("timestamp", 0)
                    var date = data!!.getStringExtra("date")


                    startJSONTask(title, place, latitude, longitude, timestamp, date)
                }
            }
        }
    }


    object AddSchedule {

        // 리사이클러 뷰 시간순으로 정렬 후 다시 보이기
        fun resetAdapter() {
            Collections.sort(ScheduleList, object : Comparator<Schedule> {
                override fun compare(x: Schedule, y: Schedule) = x.date.compareTo(y.date)
            })
            timelineRecyclerAdapter = TimelineRecyclerAdapter()
            recyclerView.adapter = timelineRecyclerAdapter
            recyclerView.layoutManager = LinearLayoutManager(MainActivity())
            timelineRecyclerAdapter.addWeatherHeader(cityWeather)
            for (i in 0 until ScheduleList.size) {
                timelineRecyclerAdapter.addTimepoint(Timepoint())
                timelineRecyclerAdapter.addSchedule(ScheduleList[i])
            }
        }

        // 일정 추가
        fun addSchedule(
            weather: String,
            title: String,
            date: String,
            temp: Float,
            rain: Float,
            place: String
        ) {
            ScheduleList.add(Schedule(weather, title, date, temp, rain, place))
            resetAdapter()
        }
    }

    // TODO db와 연결해서 데이터 설정해야함
    companion object FakeData {
        val cityWeather: CityWeather = CityWeather(
            "Warsaw",
            "Sunny", 21.5f, "5%", "56%", "25km/h"
        )

        val ScheduleList: ArrayList<Schedule> = arrayListOf(
            Schedule("Sunny", "영화", "2020-05-30 01:50", 24f, 30.0f, "강남역"),
            Schedule("Windy", "건대입구", "2020-05-31", 22.2f, 20.0f, "강남역"),
            Schedule("Rain fall", "성수역", "2020-05-31", 18.5f, 50.0f, "강남역"),
            Schedule("Cloudy", "춘천", "2020-05-31", 18f, 70.0f, "강남역"),
            Schedule("Clear sky", "강릉", "2020-06-02", 21.5f, 10.0f, "강남역"),
            Schedule("Sunny", "양막창", "2020-06-03", 21.5f, 0.0f, "신논현역"),
            Schedule(
                "Rain fall",
                "떡볶이",
                "2020-06-03",
                19.7f, 10.0f,
                "강남역",
                isLastItem = true
            )
        )

    }
}


fun startJSONTask(
    title: String,
    place: String,
    latitude: Double,
    longitude: Double,
    timestamp: Long,
    date: String
) {
    val task = MainAsyncTask(context = MainActivity())


    timeStamp = timestamp / 1000
    var timeDiff = Math.abs(now - timeStamp!!)
    isHourly = timeDiff < 2 * 86400 // 2일(초)
    Log.i(
        "2일",
        now.toString() + " " + timeStamp.toString() + " " + isHourly.toString() + " " + timeDiff
    )

    var url =
        URL("https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&&appid=0278d360e035caa40fc3debf63523512&units=metric&exclude=minutely,current")
    var forAsync = ForAsync(url,title, date, place)

    task.execute(forAsync)
}


// 비동기
class MainAsyncTask(context: MainActivity) : AsyncTask<ForAsync, Unit, ForAsync>() {

    override fun doInBackground(vararg params:ForAsync): ForAsync? {

        Log.i("파람",params[0].url.toString())
        val doc = Jsoup.connect(params[0].url.toString()).ignoreContentType(true).get()
        val json = JSONObject(doc.text())
        if (isHourly) {
            parseHourly(json)
            findMinDiffDt(hourlyWeatherArray)
        } else {
            parseDaily(json)
            findMinDiffDt(dailyWeatherArray)
        }
        return params[0]

    }

    override fun onPostExecute(result: ForAsync) {
        super.onPostExecute(result)
        addSchedule(
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
        Log.i("최종least", least.toString())
        Log.i("최종leastDiffData", leastDiffData.toString())

        return leastDiffData as WeatherData
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



}