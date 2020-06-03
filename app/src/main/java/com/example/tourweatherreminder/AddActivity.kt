package com.example.tourweatherreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity(),
    OnDateSetListener {

    var latitude: Double?=null
    var longitude:Double?=null
    var timestamp:Long?=null

    var mDialogAll: TimePickerDialog? = null
    var selectedDateText: TextView? = null
    var editTitleText: EditText? = null
    var selectedPlaceText:TextView?=null
    var sf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        initView()
        createDatePickerDialog()

    }


    fun createDatePickerDialog() {
        val sevenDays = 7 * 1000 * 60 * 60 * 24L
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
            .setMaxMillseconds(System.currentTimeMillis() + sevenDays)
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
        selectedPlaceText = findViewById<View>(R.id.selectedPlaceText) as TextView
        editTitleText = findViewById<View>(R.id.editTitleText) as EditText

        addDateBtn.setOnClickListener {
            mDialogAll!!.show(supportFragmentManager, "all")
        }
        addPlaceBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivityForResult(intent, 100)
        }
        button.setOnClickListener {
            val intent = Intent()
            intent.putExtra("title", editTitleText?.text.toString())
            intent.putExtra("timestamp", timestamp)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("placeName",selectedPlaceText?.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    selectedPlaceText?.text = data!!.getStringExtra("placeName").toString()
                    latitude = data.getDoubleExtra("latitude",0.0)
                    longitude = data.getDoubleExtra("longitude",0.0)
                    timestamp = data.getLongExtra("timestamp",0)
                }
            }
        }
    }


    override fun onDateSet(
        timePickerDialog: TimePickerDialog,
        millseconds: Long
    ) {
        val text = getDateToString(millseconds)
        selectedDateText!!.text = text
        timestamp = millseconds
//        selectedDateText!!.text=timestamp.toString()
    }

    fun getDateToString(time: Long): String {
        val d = Date(time)
        return sf.format(d)
    }


}