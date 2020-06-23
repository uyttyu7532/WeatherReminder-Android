package com.example.tourweatherreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.KeyUtils
import com.vanillaplacepicker.utils.PickerLanguage
import com.vanillaplacepicker.utils.PickerType
import kotlinx.android.synthetic.main.activity_maps.*

class SearchPlaceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

    }


    fun init() {
        searchBtn.setOnClickListener {
            var intent = VanillaPlacePicker.Builder(this)
                .with(PickerType.AUTO_COMPLETE)
                .setPickerLanguage(PickerLanguage.ENGLISH)
                .build()
            startActivityForResult(intent, KeyUtils.REQUEST_PLACE_PICKER)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                KeyUtils.REQUEST_PLACE_PICKER -> {
                    val vanillaAddress = VanillaPlacePicker.onActivityResult(data)
                    vanillaAddress?.let {
//                        Toast.makeText(
//                            this,
//                            it.latitude.toString() + " " + it.longitude.toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }
            }
        }
    }

}
