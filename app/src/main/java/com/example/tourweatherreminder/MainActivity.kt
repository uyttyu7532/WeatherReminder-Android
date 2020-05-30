package com.example.tourweatherreminder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener,
    OnDateSetListener {

    lateinit var timelineRecyclerAdapter: TimelineRecyclerAdapter

    var mDialogAll: TimePickerDialog? = null
    var SelectedDate:String? = null
    var sf = SimpleDateFormat("yyyy-MM-dd HH:mm")

    val text : String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timelineRecyclerAdapter = TimelineRecyclerAdapter()


        recycler_view.adapter = timelineRecyclerAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        timelineRecyclerAdapter.addWeatherHeader(cityWeather)
        for (i in 0..5) {
            timelineRecyclerAdapter.addTimepoint(timepoints[i])
            timelineRecyclerAdapter.addSchedule(ScheduleList[i])
        }

        addFAB.setOnClickListener {
//            initView()
            createDatePickerDialog()
            mDialogAll!!.show(supportFragmentManager, "all")
//            Toast.makeText(this, SelectedDate.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    fun createDatePickerDialog() {
        val nineDays = 9 * 1000 * 60 * 60 * 24L
        mDialogAll = TimePickerDialog.Builder()
            .setCallBack(this)
            .setCancelStringId("취소")
            .setSureStringId("확인")
            .setTitleStringId("날짜 및 시간 선택")
            .setYearText("년")
            .setMonthText("월")
            .setDayText("일")
            .setHourText("시")
            .setMinuteText("분")
            .setCyclic(false)
            .setMinMillseconds(System.currentTimeMillis())
            .setMaxMillseconds(System.currentTimeMillis() + nineDays)
            .setCurrentMillseconds(System.currentTimeMillis())
            .setThemeColor(resources.getColor(R.color.timepicker_dialog_bg))
            .setType(Type.ALL)
            .setWheelItemTextNormalColor(resources.getColor(R.color.timetimepicker_default_text_color))
            .setWheelItemTextSelectorColor(resources.getColor(R.color.timepicker_toolbar_bg))
            .setWheelItemTextSize(12)
            .build()
    }

    // 각 날짜에 해당하는 textView에 text설정
//    fun initView() {
//        findViewById<View>(R.id.btn_all).setOnClickListener(this)
//
//        mTvTime = findViewById<View>(R.id.tv_time) as TextView
//    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_all -> mDialogAll!!.show(supportFragmentManager, "all")
        }
    }

    override fun onDateSet(
        timePickerDialog: TimePickerDialog,
        millseconds: Long
    ) {
        val text = getDateToString(millseconds)
//        mTvTime!!.text = text
        Toast.makeText(this, text.toString(), Toast.LENGTH_SHORT).show()

    }

    fun getDateToString(time: Long): String {
        val d = Date(time)
        return sf.format(d)
    }


    companion object FakeData {
        val cityWeather: CityWeather = CityWeather(
            "Warsaw",
            "Sunny", 21.5f, "5%", "56%", "25km/h"
        )

        val timepoints: ArrayList<Timepoint> = arrayListOf(
            Timepoint("Next 6 hours", "Sunny"),
            Timepoint("Next 24 hours", "Clear sky"),
            Timepoint("Next day", "Cloudy"),
            Timepoint("Next 2 days from now", "Rainy"),
            Timepoint("Next 3 days from now", "Sunny"),
            Timepoint("Next week", "Clear sky")
        )

        val ScheduleList: ArrayList<Schedule> = arrayListOf(
            Schedule("Sunny", "영화", "2020-05-30 01:50", 24f, "강남역"),
            Schedule("Windy", "건대입구", "2020-05-31", 22.2f, "강남역"),
            Schedule("Rain fall", "성수역", "2020-05-31", 18.5f, "강남역"),
            Schedule("Cloudy", "춘천", "2020-05-31", 18f, "강남역"),
            Schedule("Clear sky", "강릉", "2020-06-02", 21.5f, "강남역"),
            Schedule("Sunny", "양막창", "2020-06-03", 21.5f, "신논현역"),
            Schedule("Rain fall", "떡볶이", "2020-06-03", 19.7f, "강남역", isLastItem = true)
        )

    }
}
