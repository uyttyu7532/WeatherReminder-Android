package com.example.tourweatherreminder

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import kotlinx.android.synthetic.main.activity_add.*
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*


var latitude :Double = 23.057582
var longitude :Double = 72.534458

class AddActivity : AppCompatActivity(),
    OnDateSetListener {
    var mDialogAll: TimePickerDialog? = null
    var selectedDateText: TextView? = null
    var editTitleText: EditText? = null
    var sf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        initView()
        createDatePickerDialog()



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

    fun initView() {
        selectedDateText = findViewById<View>(R.id.selectedDateText) as TextView
        addDateBtn.setOnClickListener {
            mDialogAll!!.show(supportFragmentManager, "all")
        }
        addPlaceBtn.setOnClickListener {
            startActivity<MapsActivity>()
        }
        button.setOnClickListener {
//            val mActivity = MainActivity()
//            mActivity.addSchedule("Sunny", "유튜브", "2020-06 01:50", 28f, "집", 37.504182, 127.026738)
        }
    }






    override fun onDateSet(
        timePickerDialog: TimePickerDialog,
        millseconds: Long
    ) {
        val text = getDateToString(millseconds)
        selectedDateText!!.text = text
    }

    fun getDateToString(time: Long): String {
        val d = Date(time)
        return sf.format(d)
    }



}