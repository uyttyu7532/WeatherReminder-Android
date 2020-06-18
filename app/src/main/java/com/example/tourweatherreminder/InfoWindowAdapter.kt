package com.example.tourweatherreminder

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class InfoWindowAdapter(private val context: Activity) : GoogleMap.InfoWindowAdapter {


    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        var weather: Map<String, Int> = mapOf(
            "01" to R.drawable.icon01,
            "02" to R.drawable.icon02,
            "03" to R.drawable.icon03,
            "04" to R.drawable.icon04,
            "09" to R.drawable.icon09,
            "10" to R.drawable.icon10,
            "11" to R.drawable.icon11,
            "13" to R.drawable.icon13,
            "50" to R.drawable.icon50
        )


        val view: View = context.layoutInflater.inflate(R.layout.info_window, null)
        val infoWeather = view.findViewById<View>(R.id.infoweather) as ImageView
        val infoRain = view.findViewById<View>(R.id.inforain) as TextView
        val infoNum = view.findViewById<View>(R.id.infonum) as TextView
        val infoTitle = view.findViewById<View>(R.id.infotitle) as TextView
        val infoDate = view.findViewById<View>(R.id.infodate) as TextView

        infoWeather.setImageResource(weather.get(marker.snippet.split("\t")[0].substring(0,2))!!)

        infoRain.text = marker.snippet.split("\t")[1] + "mm/h"
        infoNum.text = marker.title
        infoTitle.text = marker.snippet.split("\t")[2]
        infoDate.text = marker.snippet.split("\t")[3]
        return view
    }

}