package com.example.tourweatherreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_weather.view.*
import pl.hypeapp.materialtimelineview.MaterialTimelineView


class WeatherItemDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        WeatherItemViewHolder(parent)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as WeatherItemViewHolder
        holder.bind(item as Schedule)
    }

    inner class WeatherItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
    ) {


        fun bind(item: Schedule) = with(itemView) {
            // If is last item we need to change position type to last.
            if (item.isLastItem) {
                item_weather_timeline.position = MaterialTimelineView.POSITION_LAST
            }
            title.text = item.title
            date.text = item.date
            temperature_degree.text = "${item.temp}\u00b0"
            place.text = item.place

            itemView.setOnClickListener {
                Toast.makeText(it.context, adapterPosition.toString(), Toast.LENGTH_SHORT).show()
            }

            when (item.weather) {
                "Sunny" -> {
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.sunny
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_weather_sunny
                        )
                    )
                }
                "Cloudy" -> {
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.cloudy
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_cloud
                        )
                    )
                }
                "Rain fall" -> {
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.rain
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_rain
                        )
                    )
                }
                "Clear sky" -> {
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.clear
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_clear
                        )
                    )
                }
                "Windy" -> {
                    item_weather_timeline.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.clear
                        )
                    )
                    weather_icon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_windy
                        )
                    )
                }
            }
        }

    }

}
