package com.example.tourweatherreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.KeyUtils
import com.vanillaplacepicker.utils.PickerLanguage
import com.vanillaplacepicker.utils.PickerType
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var latitude: Double = 37.506361
    var longitude: Double = 127.026395
    var placeName: String = "선택한 장소"


    private lateinit var mMap: GoogleMap // onMapReady에서 초기화 됨

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this) // onMapReadyCallback 인터페이스 구현
        init()
    }


    fun init() {
        searchBtn.setOnClickListener {
            var intent = VanillaPlacePicker.Builder(this)
                .withLocation(latitude, longitude) // 이전 위치에서 시작하게
                .with(PickerType.AUTO_COMPLETE)
                .setPickerLanguage(PickerLanguage.ENGLISH)
                .setCountry("KR")
                .build()

            startActivityForResult(intent, KeyUtils.REQUEST_PLACE_PICKER)
        }
        okBtn.setOnClickListener {
            val intent = Intent()
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("placeName", placeName)
            setResult(Activity.RESULT_OK, intent)
            finish()
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
                        placeName = it.name!!
                        var selectedLocation = LatLng(latitude, longitude)
                        mMap.clear()
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15F))
                        mMap.addMarker(MarkerOptions().position(selectedLocation).title(placeName))

                    }

                }
            }
        }
    }


    // OnMapReadyCallback - 맵이 사용할 준비가 다 됐어~
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        // 처음에 맵을 켰을 때
        val myLocation = LatLng(latitude, longitude) // 현재 위치
        mMap.addMarker(
            MarkerOptions().position(myLocation).title("선택한 위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .snippet("이 위치로 선택하시겠습니까?")
        ).showInfoWindow()


        for (i in 0..ScheduleList.size - 1) {
            Log.i("일정", "${ScheduleList[i].weather}")
            Log.i("일정", ScheduleList.size.toString())


            // 마커 변경
//            val markerimg = getResources().getIdentifier("icon${ScheduleList[i].weather?.substring(0, 2)}","drawable",getPackageName())


            mMap.addMarker(
                MarkerOptions().position(
                    LatLng(
                        ScheduleList[i].latitude!!,
                        ScheduleList[i].longitude!!
                    )
                ).title("${i + 1}번째 일정")
//                    .icon(BitmapDescriptorFactory.fromResource(makerimg))
                    .snippet(ScheduleList[i].place)
            ).showInfoWindow()


        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16.0f))
        initMapListener()
    }

    // 클릭했을 때
    fun initMapListener() {
        mMap.setOnMapClickListener {
            mMap.clear()
            latitude = it.latitude
            longitude = it.longitude
            mMap.addMarker(
                MarkerOptions().position(it).title("선택한 위치")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .snippet("이 위치로 선택하시겠습니까?")
            ).showInfoWindow()
        }


    }


}
