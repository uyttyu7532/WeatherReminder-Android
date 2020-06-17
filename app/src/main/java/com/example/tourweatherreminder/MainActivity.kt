package com.example.tourweatherreminder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourweatherreminder.MainActivity.AddSchedule.resetAdapter
import com.example.tourweatherreminder.db.AppDatabase
import com.example.tourweatherreminder.db.entity.ScheduleEntity
import com.google.android.material.bottomappbar.BottomAppBar
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
var ScheduleList: ArrayList<ScheduleEntity> = arrayListOf()


lateinit var mContext: Context

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = this.applicationContext
        recyclerView = this.findViewById(R.id.recycler_view)

        val bottomAppBar = findViewById(R.id.appBar) as BottomAppBar
        setSupportActionBar(bottomAppBar)

        init()
        }


    fun init() {
        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing = true // progress bar 돌아가는 작업
            // 모든 날씨 정보를 다시 받아오는 작업

            for (i in ScheduleList) {
                MainAsyncTask(applicationContext).execute(i)
            }

            // 비동기에서 작업이 끝날때 swiperefresh.isRefreshing = false해줘야함
            swiperefresh.isRefreshing = false
        }


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbarmenu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_route->{
                val intent = Intent(this, RouteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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

                    var inputScheduleEntity = ScheduleEntity(
                        null,
                        title,
                        date,
                        timestamp,
                        null,
                        null,
                        latitude,
                        longitude,
                        place
                    )

                    MainAsyncTask(applicationContext).execute(inputScheduleEntity)
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
            timelineRecyclerAdapter.addWeatherHeader(
                CityWeather(
                    "Warsaw",
                    "Sunny", 21.5f, "5%", "56%", "25km/h"
                )
            )
            for (i in 0 until ScheduleList.size) {
                timelineRecyclerAdapter.addTimepoint(Timepoint())
                timelineRecyclerAdapter.addSchedule(ScheduleList[i])
            }
        }

    }
}



