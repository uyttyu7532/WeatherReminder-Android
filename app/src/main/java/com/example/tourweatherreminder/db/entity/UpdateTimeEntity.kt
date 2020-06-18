package com.example.tourweatherreminder.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UpdateTime")
data class UpdateTimeEntity(
    @PrimaryKey
    var updateTime: String
)