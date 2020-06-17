package com.example.tourweatherreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tourweatherreminder.db.entity.ScheduleEntity
import kotlinx.android.synthetic.main.item_weather.view.*
import pl.hypeapp.materialtimelineview.MaterialTimelineView


class WeatherItemDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        WeatherItemViewHolder(parent)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as WeatherItemViewHolder
        holder.bind(item as ScheduleEntity)
    }

    inner class WeatherItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
    ) {


        fun bind(item: ScheduleEntity) = with(itemView) {
            if (item.isLastItem) {
                item_weather_timeline.position = MaterialTimelineView.POSITION_LAST
            }
            title.text = item.title
            date.text = item.date
            temperature_degree.text = "${item.temp}\u00b0"
            rain_percentage.text = item.rain.toString()+"mm / h"
            place.text = item.place

            itemView.setOnClickListener {
                Toast.makeText(it.context, adapterPosition.toString(), Toast.LENGTH_SHORT).show()
            }


            when (item.weather) {
                "01d","01n" -> { //해
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon01
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon01
                        )
                    )
                }
                "02d","02n" -> { //구름+해
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon02
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon02
                        )
                    )
                }
                "03d","03n" -> { //구름
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon03
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon03
                        )
                    )
                }
                "04d","04n" -> { // 먹구름
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon04
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon04
                        )
                    )
                }
                "09d","09n" -> { //  소나기
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon09
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon09
                        )
                    )
                }
                "10d","10n" -> { // 비
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon10
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon10
                        )
                    )
                }
                "11d","11n" -> { // 천둥번개
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon11
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon11
                        )
                    )
                }
                "13d","13n" -> { //눈
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon13
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon13
                        )
                    )
                }
                "50d","50n" -> { // 안개
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon50
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon50
                        )
                    )
                }
            }
        }

    }

}
