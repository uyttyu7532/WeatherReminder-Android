package com.example.tourweatherreminder.db.dao

import androidx.room.*
import com.example.tourweatherreminder.db.entity.ScheduleEntity
import com.example.tourweatherreminder.db.entity.UpdateTimeEntity


@Dao
abstract class UpdateTimeDao : BaseDao<ScheduleEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUpdateTime(updateTime: UpdateTimeEntity)



}