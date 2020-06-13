package com.example.tourweatherreminder.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tourweatherreminder.ViewType

@Entity(tableName = "Schedule")
data class ScheduleEntity(
    var weather: String,
    @PrimaryKey
    var title: String,
    var date: String,
    var temp: Float,
    var rain: Float,
    var place: String,
    var isLastItem: Boolean = false
) : ViewType {

    override fun getViewType(): Int = ViewType.ITEM

}








