package com.example.tourweatherreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_time_point.view.*


class TimepointItemDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = WeatherItemViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as WeatherItemViewHolder
        holder.bind(item as Timepoint)
    }

    inner class WeatherItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_time_point, parent, false)) {

        fun bind(item: Timepoint) = with(itemView) {
            time.text = item.timepoint
            time_description.text = item.description
        }

    }

}


