package com.example.tourweatherreminder

import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tourweatherreminder.MainActivity.resetSchedule.resetAdapter
import com.example.tourweatherreminder.db.AppDatabase
import com.example.tourweatherreminder.db.entity.ScheduleEntity
import com.example.tourweatherreminder.model.MyJobService
import com.example.tourweatherreminder.model.makeNotification
import com.example.tourweatherreminder.model.notificationContent
import com.example.tourweatherreminder.model.notificationResultCnt
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

var dailyWeatherArray = arrayListOf<WeatherData>()
var hourlyWeatherArray = arrayListOf<WeatherData>()
var leastDiffData: WeatherData? = null
var now = System.currentTimeMillis() / 1000
var isHourly = false
var timeStamp: Long? = null

lateinit var timelineRecyclerAdapter: TimelineRecyclerAdapter
lateinit var recyclerView: RecyclerView
lateinit var updatetime: TextView
var ScheduleList: ArrayList<ScheduleEntity> = arrayListOf()

//var WeatherList: ArrayList<String> = arrayListOf()
var isModify = false
var modifyList = arrayListOf<String>() // title, place, lat, lon,timestamp, date


lateinit var mContext: Context

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this.applicationContext
        recyclerView = this.findViewById(R.id.recycler_view)
        updatetime = this.findViewById(R.id.updateTime)


        val bottomAppBar = findViewById(R.id.appBar) as BottomAppBar
        setSupportActionBar(bottomAppBar)

        init()

    }

    override fun onResume() {
        super.onResume()
        updateAllSchedule()
//        if(isModify){
//            val intent = Intent(this, AddActivity::class.java)
////            intent.putExtra("modify", ismodify)
//            startActivityForResult(intent, 100)
//        }
    }


    fun init() {
//        val js = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//        val serviceComponent = ComponentName(this, MyJobService::class.java)
//        val jobInfo = JobInfo.Builder(22, serviceComponent)
//            .setMinimumLatency(TimeUnit.MINUTES.toMillis(1))
//            .setOverrideDeadline(TimeUnit.MINUTES.toMillis(3))
//            .build()
//        js.schedule(jobInfo)

        val js = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceComponent = ComponentName(this, MyJobService::class.java)
        val jobInfo = JobInfo.Builder(22, serviceComponent)
            .setPeriodic(TimeUnit.MINUTES.toMillis(15))
            .build()
        js.schedule(jobInfo)


        refreshBtn.setOnClickListener {
            updateAllSchedule()
        }

        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing = true // progress bar 돌아가는 작업

            updateAllSchedule()

            // 비동기에서 작업이 끝날때 swiperefresh.isRefreshing = false해줘야함
            swiperefresh.isRefreshing = false
        }


        // 일정추가 FAB
        addFAB.setOnClickListener {
            isModify = false
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, 100)
        }


        val appDatabase = AppDatabase
        appDatabase?.getInstance(applicationContext)?.DataDao()?.getData()?.observe(this,
            androidx.lifecycle.Observer {
                ScheduleList.clear()
                Log.d("로그 db내용", it.toString()) // 전체 저장된 List<ScheduleEntity>
                it?.forEach {
                    ScheduleList.add(it)
                }
                resetAdapter()
            })
//        appDatabase?.getInstance(applicationContext)?.DataDao()?.isWeatherChanged()?.observe(this,
//            androidx.lifecycle.Observer {
//                Log.d("로그 isWeatherChanged", it.toString()) // 전체 저장된 List<ScheduleEntity>
//
//            })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbarmenu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_route -> {
                if (ScheduleList.size > 0) {
                    val intent = Intent(this, RouteActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "추가된 일정이 없습니다.", Toast.LENGTH_SHORT).show()
                    Log.i("로그 스케줄리스트 상태", ScheduleList.size.toString() + ScheduleList.toString())
                }
            }
            R.id.developer_info -> {
                Toast.makeText(this, "스마트ict융합공학과 201714286 조예린", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    // addActivity에서 돌아온 결과
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.i("로그 온액티비티result실행됨", "그렇다")
            isModify = false
            when (requestCode) {
                100 -> {
                    var title = data!!.getStringExtra("title")
                    var place = data!!.getStringExtra("placeName")
                    var latitude = data!!.getDoubleExtra("latitude", 37.521611)
                    var longitude = data!!.getDoubleExtra("longitude", 127.046651)
                    var timestamp = data!!.getLongExtra("timestamp", 0)
                    var date = data!!.getStringExtra("date")

                    var inputScheduleEntity = ScheduleEntity(
                        "",
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


    fun updateAllSchedule() {
        Handler().postDelayed(Runnable
        {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = current.format(formatter)

            notificationContent = ""
            notificationResultCnt = 0
            //makeNotification(content)
            // 모든 날씨 정보를 다시 받아오는 작업
            for (i in ScheduleList) {
                MainAsyncTask(applicationContext).execute(i)
            }
            updatetime.setText(formatted)
        }, 1000)


    }


    object resetSchedule {


        fun resetAdapter() {
            // 리사이클러 뷰 시간순으로 정렬 후 다시 보이기 -> db에서 ORDERBY로 해결
//            Collections.sort(ScheduleList, object : Comparator<ScheduleEntity> {
//                override fun compare(x: ScheduleEntity, y: ScheduleEntity) =
//                    x.date.compareTo(y.date)
//            })
            if (ScheduleList.size > 0) {
                ScheduleList[0].isFirstItem = true
                ScheduleList[ScheduleList.size - 1].isLastItem = true
            }
            timelineRecyclerAdapter = TimelineRecyclerAdapter()
            recyclerView.adapter = timelineRecyclerAdapter
            recyclerView.layoutManager = LinearLayoutManager(mContext)

            for (i in 0 until ScheduleList.size) {
                if (!ScheduleList[i].isFirstItem) {
                    timelineRecyclerAdapter.addTimepoint(Timepoint())
                }
                timelineRecyclerAdapter.addSchedule(ScheduleList[i], ScheduleList[i].isFirstItem)
            }
            Log.i("로그 스케줄리스트", ScheduleList.size.toString() + ScheduleList.toString())
        }


    }
}



