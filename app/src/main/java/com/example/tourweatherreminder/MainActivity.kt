package com.example.tourweatherreminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

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
            timelineRecyclerAdapter.addSchedule(ScheduleList[i])
        }

        addFAB.setOnClickListener {
            startActivity<AddActivity>()
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

        val ScheduleList: ArrayList<Schedule> = arrayListOf(
            Schedule("Sunny","영화", "2020-05-30 01:50", 24f,"강남역"),
            Schedule("Windy","건대입구", "2020-05-31", 22.2f,"강남역"),
            Schedule("Rain fall","성수역", "2020-05-31", 18.5f,"강남역"),
            Schedule("Cloudy","춘천", "2020-05-31", 18f,"강남역"),
            Schedule("Clear sky","강릉", "2020-06-02", 21.5f,"강남역"),
            Schedule("Sunny","양막창", "2020-06-03", 21.5f,"신논현역"),
            Schedule("Rain fall","떡볶이", "2020-06-03", 19.7f, "강남역",isLastItem = true)
        )

    }
}
