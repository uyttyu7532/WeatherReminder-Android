package com.example.tourweatherreminder.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tourweatherreminder.db.entity.ScheduleEntity


@Dao
abstract class DataDao : BaseDao<ScheduleEntity> {


    @Query("SELECT * FROM Schedule")
    abstract fun getData(): LiveData<List<ScheduleEntity>>

    // 해당 title이 이미 존재하는지
    @Query("SELECT exists (SELECT title FROM Schedule WHERE title = :title LIMIT 1)")
    abstract fun getItemTitle(title: String): Boolean

//    @Query("SELECT weather FROM Schedule")
//    abstract fun isWeatherChanged(): LiveData<List<String>>

//    @Query("SELECT title ,weather FROM Schedule")
//    abstract fun getWeather(): LiveData<String>

    @Delete
    abstract fun deleteSchedule(schedule: ScheduleEntity)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateSchedule(schedule: ScheduleEntity)

    @Insert
    abstract fun insertSchedule(schedule: ScheduleEntity)



}