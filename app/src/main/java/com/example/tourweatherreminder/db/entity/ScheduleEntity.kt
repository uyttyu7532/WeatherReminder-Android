package com.example.tourweatherreminder.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Schedule")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val weather:String,
    var title: String,
    var date: String,
    val temp: Float,
    val rain: Float,
    var place: String
)


