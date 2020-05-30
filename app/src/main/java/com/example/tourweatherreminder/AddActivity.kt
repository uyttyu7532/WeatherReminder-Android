package com.example.tourweatherreminder
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import java.text.SimpleDateFormat
import java.util.*


class AddActivity : AppCompatActivity(), View.OnClickListener,
    OnDateSetListener {
    var mDialogAll: TimePickerDialog? = null
    var mTvTime: TextView? = null
    var sf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        initView()
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
        findViewById<View>(R.id.btn_all).setOnClickListener(this)

        mTvTime = findViewById<View>(R.id.tv_time) as TextView
    }

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
        mTvTime!!.text = text
    }

    fun getDateToString(time: Long): String {
        val d = Date(time)
        return sf.format(d)
    }
}