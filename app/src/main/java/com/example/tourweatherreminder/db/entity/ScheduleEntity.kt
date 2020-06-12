package com.example.tourweatherreminder.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tourweatherreminder.ViewType

@Entity(tableName = "Schedule")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id:Long,
    val weather: String,
    var title: String,
    var date: String,
    val temp: Float,
    val rain: Float,
    var place: String
//    val isLastItem: Boolean = false
) : ViewType {

    override fun getViewType(): Int = ViewType.ITEM

}







