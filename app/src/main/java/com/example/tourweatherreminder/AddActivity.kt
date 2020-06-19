package com.example.tourweatherreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.tourweatherreminder.db.AppDatabase
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
            .setWheelItemTextSelectorColor(R.color.main_green_color)
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
            // TODO 만약 editTitleText가 db에 있는 내용이 아니거나 비어있지 않다면 추가! + 장소, 시간 선택도
            if(editTitleText!!.currentTextColor==R.color.textwatcher){
                Toast.makeText(this,"이미 존재하는 일정입니다.",Toast.LENGTH_SHORT).show()
            }
            if(editTitleText!!.text==null){
                Toast.makeText(this,"일정 이름을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            else {
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

//        editTitleText!!.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // TODO 입력한 값이 이미 db의 id에 존재한다면 색 바꾸기
//                if(s!=null){
//                    editTitleText!!.setHintTextColor(resources.getColor(R.color.textwatcher))
//                }
//            }
//        })

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