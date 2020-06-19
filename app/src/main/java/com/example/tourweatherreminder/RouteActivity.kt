package com.example.tourweatherreminder

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_route.*


class RouteActivity : AppCompatActivity() {

    lateinit var googleMap: GoogleMap
    lateinit var leftBtn: FloatingActionButton
    lateinit var rightBtn: FloatingActionButton
    lateinit var markerList: ArrayList<MarkerOptions>
    lateinit var cu: CameraUpdate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        initMap()


        init()

    }

    fun init() {

        markerList = ArrayList(ScheduleList.size)
        var currentSchedule = -1

        leftBtn = findViewById(R.id.leftBtn)
        rightBtn = findViewById(R.id.rightBtn)

        leftBtn.setOnClickListener {
            if (currentSchedule <= 0) {
                Toast.makeText(this, "첫번째 일정입니다.", Toast.LENGTH_SHORT).show()
            } else {
                currentSchedule--
                googleMap.addMarker(markerList!![currentSchedule]).showInfoWindow()

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
                googleMap.addMarker(markerList!![currentSchedule]).showInfoWindow()
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

        totalRoute.setOnClickListener {
            googleMap.animateCamera(cu)
        }
    }

    fun initMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.routemap) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            var lineOptions = PolylineOptions()
            var builder: LatLngBounds.Builder = LatLngBounds.Builder()


            for (i in 0..ScheduleList.size - 1) {

                // 동선 표시
                lineOptions!!.add(
                    LatLng(
                        ScheduleList[i].latitude!!,
                        ScheduleList[i].longitude!!
                    )
                )
//                // 마커 변경
//                val markerimg = getResources().getIdentifier(
//                    "icon${ScheduleList[i].weather?.substring(0, 2)}",
//                    "drawable",
//                    getPackageName()
//                )
//
//                val markerimg = getResources().getIdentifier(
//                    "pin",
//                    "drawable",
//                    getPackageName()
//                )

                var markeroptions = MarkerOptions()
                markeroptions
                    .position(
                        LatLng(
                            ScheduleList[i].latitude!!,
                            ScheduleList[i].longitude!!
                        )
                    )
                    .title("${i + 1}번째 일정")
//                  .icon(BitmapDescriptorFactory.fromResource(markerimg))
                    .snippet(ScheduleList[i].weather + "\t" + ScheduleList[i].rain + "\t" + ScheduleList[i].title + "\t" + ScheduleList[i].date)

                markerList.add(markeroptions)
                builder.include(markeroptions.position)
            }

            val adapter = InfoWindowAdapter(this)
            googleMap.setInfoWindowAdapter(adapter)
            for (i in 0..ScheduleList.size - 1) {
                googleMap.addMarker(markerList!![i]).hideInfoWindow()
            }

            googleMap.addPolyline(lineOptions)
            val padding = 300 // offset from edges of the map in pixels
            var bounds = builder.build()
            cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            googleMap.animateCamera(cu);

        }
    }


}