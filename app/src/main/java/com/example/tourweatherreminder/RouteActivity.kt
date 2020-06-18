package com.example.tourweatherreminder

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


class RouteActivity : AppCompatActivity() {

    lateinit var googleMap: GoogleMap
    lateinit var leftBtn: FloatingActionButton
    lateinit var rightBtn: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        initMap()

        init()
    }

    fun init() {
        var currentSchedule = 0

        leftBtn = findViewById(R.id.leftBtn)
        rightBtn = findViewById(R.id.rightBtn)

        leftBtn.setOnClickListener {
            if (currentSchedule == 0) {
                Toast.makeText(this, "처음 일정입니다.", Toast.LENGTH_SHORT).show()
            } else {
                currentSchedule--
                Toast.makeText(this, currentSchedule.toString(), Toast.LENGTH_SHORT).show()
                googleMap.animateCamera(

                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            ScheduleList[currentSchedule].latitude!!,
                            ScheduleList[currentSchedule].longitude!!
                        ), 16.0f
                    )
                )
            }

        }
        rightBtn.setOnClickListener {
            if (currentSchedule == ScheduleList.size - 1) {
                Toast.makeText(this, "마지막 일정입니다.", Toast.LENGTH_SHORT).show()
            } else {
                currentSchedule++
                Toast.makeText(this, currentSchedule.toString(), Toast.LENGTH_SHORT).show()
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            ScheduleList[currentSchedule].latitude!!,
                            ScheduleList[currentSchedule].longitude!!
                        ), 16.0f
                    )
                )

            }

        }
    }

    fun initMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.routemap) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it


            var lineOptions = PolylineOptions()

            for (i in 0..ScheduleList.size - 1) {

                lineOptions!!.add(
                    LatLng(
                        ScheduleList[i].latitude!!,
                        ScheduleList[i].longitude!!
                    )
                )

                // 마커 변경
                val markerimg = getResources().getIdentifier(
                    "icon${ScheduleList[i].weather?.substring(0, 2)}",
                    "drawable",
                    getPackageName()
                )


                var markeroptions = MarkerOptions()
                markeroptions
                    .position(
                        LatLng(
                            ScheduleList[i].latitude!!,
                            ScheduleList[i].longitude!!
                        )
                    )
                    .title("${i + 1}번째 일정")
//                        .icon(BitmapDescriptorFactory.fromResource(markerimg))
                    .snippet(ScheduleList[i].weather+"\t"+ScheduleList[i].rain+"\t"+ScheduleList[i].title+"\t"+ScheduleList[i].date)

                val adapter = InfoWindowAdapter(this)
                googleMap.setInfoWindowAdapter(adapter)

                googleMap.addMarker(markeroptions).showInfoWindow()
            }

            googleMap.addPolyline(lineOptions)
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        ScheduleList[0].latitude!!,
                        ScheduleList[0].longitude!!
                    ), 16.0f
                )
            )
        }
    }


}