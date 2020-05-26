package com.example.tourweatherreminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var timelineRecyclerAdapter: TimelineRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timelineRecyclerAdapter = TimelineRecyclerAdapter()

        recycler_view.adapter = timelineRecyclerAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        timelineRecyclerAdapter.addWeatherHeader(cityWeather)
        for (i in 0..5) {
            timelineRecyclerAdapter.addTimepoint(timepoints[i])
            timelineRecyclerAdapter.addWeather(weatherList[i])
        }
    }

    companion object FakeData {
        val cityWeather: CityWeather = CityWeather(
            "Warsaw",
            "Sunny", 21.5f, "5%", "56%", "25km/h"
        )

        val timepoints: ArrayList<Timepoint> = arrayListOf(
            Timepoint("Next 6 hours", "Sunny"),
            Timepoint("Next 24 hours", "Clear sky"),
            Timepoint("Next day", "Cloudy"),
            Timepoint("Next 2 days from now", "Rainy"),
            Timepoint("Next 3 days from now", "Sunny"),
            Timepoint("Next week", "Clear sky")
        )

        val weatherList: ArrayList<Weather> = arrayListOf(
            Weather("강남역에서 영화", "01:50", 24f),
            Weather("건대입구", "Clear sky", 22.2f),
            Weather("Tuesday", "Cloudy", 18.5f),
            Weather("Wednesday", "Rain fall", 18f),
            Weather("Thursday", "Sunny", 21.5f),
            Weather("양막창", "eat", 21.5f),
            Weather("Monday", "Windy", 19.7f, isLastItem = true)
        )

    }
}
