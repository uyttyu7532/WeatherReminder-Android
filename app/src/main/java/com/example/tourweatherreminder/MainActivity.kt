package com.example.tourweatherreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourweatherreminder.MainActivity.AddSchedule.resetAdapter
import kotlinx.android.synthetic.main.activity_main.*
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
                    var latitude = data!!.getDoubleExtra("latitude", 37.521611)
                    var longitude = data!!.getDoubleExtra("longitude", 127.046651)
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



