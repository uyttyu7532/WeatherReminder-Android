package com.example.tourweatherreminder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourweatherreminder.MainActivity.AddSchedule.resetAdapter
import com.example.tourweatherreminder.db.AppDatabase
import com.example.tourweatherreminder.db.entity.ScheduleEntity
import com.example.tourweatherreminder.model.ForAsync
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
var ScheduleList: ArrayList<ScheduleEntity> = arrayListOf()


lateinit var mContext: Context

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = this.applicationContext

        recyclerView = this.findViewById(R.id.recycler_view)

//        resetAdapter()

        // 일정추가 FAB
        addFAB.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, 100)
        }

        val appDatabase = AppDatabase

        appDatabase?.getInstance(applicationContext)?.DataDao()?.getData()?.observe(this,
            androidx.lifecycle.Observer {
                ScheduleList.clear()
                Log.d("뭐가 저장되어 있니?", it.toString()) // 전체 저장된 List<ScheduleEntity>
                it?.forEach {
                    it.isLastItem = false
                    ScheduleList.add(it)
                }
                resetAdapter()
            })


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

                    var url =
                        URL("https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&&appid=0278d360e035caa40fc3debf63523512&units=metric&exclude=minutely,current")
                    var forAsync = ForAsync(url, title, date, timestamp, place, latitude, longitude)

                    MainAsyncTask(applicationContext).execute(forAsync)
                }
            }
        }
    }


    object AddSchedule {

        // 리사이클러 뷰 시간순으로 정렬 후 다시 보이기
        fun resetAdapter() {
            Collections.sort(ScheduleList, object : Comparator<ScheduleEntity> {
                override fun compare(x: ScheduleEntity, y: ScheduleEntity) =
                    x.date.compareTo(y.date)
            })
            if (ScheduleList.size > 0) {
                ScheduleList[ScheduleList.size - 1].isLastItem = true
            }
            timelineRecyclerAdapter = TimelineRecyclerAdapter()
            recyclerView.adapter = timelineRecyclerAdapter
            recyclerView.layoutManager = LinearLayoutManager(mContext)
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
            ScheduleList.add(ScheduleEntity(weather, title, date, temp, rain, place))
            resetAdapter()
        }
    }

    companion object FakeData {
        val cityWeather: CityWeather = CityWeather(
            "Warsaw",
            "Sunny", 21.5f, "5%", "56%", "25km/h"
        )


    }
}



