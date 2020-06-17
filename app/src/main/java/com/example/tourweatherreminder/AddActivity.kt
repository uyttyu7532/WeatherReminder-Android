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
import com.libizo.CustomEditText
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity(),
    OnDateSetListener {

    var latitude: Double? = null
    var longitude: Double? = null
    var timestamp: Long? = null
    var date: String? = null

    var mDialogAll: TimePickerDialog? = null
    var selectedDateText: TextView? = null
    var editTitleText: CustomEditText? = null
    var selectedPlaceText: TextView? = null
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
            .setThemeColor(R.color.timepicker_dialog_bg)
            .setType(Type.ALL)
            .setWheelItemTextNormalColor(R.color.timetimepicker_default_text_color)
            .setWheelItemTextSelectorColor(R.color.timepicker_toolbar_bg)
            .setWheelItemTextSize(12)
            .build()
    }

    fun initView() {
        selectedDateText = findViewById<View>(R.id.selectedDateText) as TextView
        selectedPlaceText = findViewById<View>(R.id.selectedPlaceText) as TextView
        editTitleText = findViewById<View>(R.id.editTitleText) as CustomEditText

        // 날짜 및 시간 선택 버튼
        addDateBtn.setOnClickListener {
            mDialogAll!!.show(supportFragmentManager, "all")
        }

        // 장소 선택 버튼
        addPlaceBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivityForResult(intent, 100)
        }

        // 확인 버튼
        addBtn.setOnClickListener {
            val intent = Intent()
            var title = editTitleText?.text.toString()
            intent.putExtra("title", title)
            intent.putExtra("timestamp", timestamp)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("placeName", selectedPlaceText?.text.toString())
            intent.putExtra("date", selectedDateText?.text.toString())

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
                    latitude = data.getDoubleExtra("latitude", 0.0)
                    longitude = data.getDoubleExtra("longitude", 0.0)
                    timestamp = data.getLongExtra("timestamp", 0)
                    date = data.getStringExtra("date")
                }
            }
        }
    }


    override fun onDateSet(
        timePickerDialog: TimePickerDialog,
        millseconds: Long
    ) {
        val dateToString = getDateToString(millseconds)
        date = dateToString
        selectedDateText!!.text = dateToString
        timestamp = millseconds
    }

    fun getDateToString(time: Long): String {
        val d = Date(time)
        return sf.format(d)
    }
}