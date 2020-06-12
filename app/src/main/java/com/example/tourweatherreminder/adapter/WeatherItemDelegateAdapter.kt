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
            // If is last item we need to change position type to last.
//            if (item.isLastItem) {
//                item_weather_timeline.position = MaterialTimelineView.POSITION_LAST
//            }
            title.text = item.title
            date.text = item.date
            temperature_degree.text = "${item.temp}\u00b0"
            rain_percentage.text = item.rain.toString()+"mm / h"
            place.text = item.place

            itemView.setOnClickListener {
                Toast.makeText(it.context, adapterPosition.toString(), Toast.LENGTH_SHORT).show()
            }


            when (item.weather) {
                "01d" -> { //해
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon01d
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon01d
                        )
                    )
                }
                "02d" -> { //구름+해
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon02d
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon02d
                        )
                    )
                }
                "03d" -> { //구름
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon03d
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon03d
                        )
                    )
                }
                "04d" -> { // 먹구름
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon04d
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon04d
                        )
                    )
                }
                "09d" -> { //  소나기
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon09d
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon09d
                        )
                    )
                }
                "10d" -> { // 비
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon10d
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon10d
                        )
                    )
                }
                "11d" -> { // 천둥번개
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon11d
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon11d
                        )
                    )
                }
                "13d" -> { //눈
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon13d
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon13d
                        )
                    )
                }
                "50d" -> { // 안개
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.icon50d
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon50d
                        )
                    )
                }
            }
        }

    }

}
