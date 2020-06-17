package com.example.tourweatherreminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class RouteActivity : AppCompatActivity() {

    lateinit var googleMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        initMap()
    }

    fun initMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.routemap) as SupportMapFragment
        mapFragment.getMapAsync{
            googleMap = it
        }
    }


}