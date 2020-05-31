package com.example.tourweatherreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.KeyUtils
import com.vanillaplacepicker.utils.MapType
import com.vanillaplacepicker.utils.PickerLanguage
import com.vanillaplacepicker.utils.PickerType
import kotlinx.android.synthetic.main.activity_search_place.*

class SearchPlaceActivity : AppCompatActivity(), OnMapReadyCallback {
    var latitude: Double = 23.057582
    var longitude: Double = 72.534458

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_place)
//        mapView.getMapAsync(this)
        init()

    }

    fun init() {
        searchBtn.setOnClickListener {
            var intent = VanillaPlacePicker.Builder(this)
                .withLocation(latitude, longitude) // 이전 위치에서 시작하게
                .with(PickerType.MAP_WITH_AUTO_COMPLETE)
                .setMapType(MapType.NORMAL)
                .setPickerLanguage(PickerLanguage.ENGLISH)
                .setCountry("KR")
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
                        latitude = it.latitude!!
                        longitude = it.longitude!!
//                        tvSelectedPlace.text = it.name
                        Toast.makeText(
                            this,
                            it.latitude.toString() + " " + it.longitude.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onMapReady(googlemap: GoogleMap?) {
        mMap = googlemap!! // 지도가 준비되면 객체 얻음

        // Add a marker in Sydney and move the camera
        // 시드니에 마커 추가하고 카메라 이동
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
