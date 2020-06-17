package com.example.tourweatherreminder.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tourweatherreminder.db.entity.ScheduleEntity


@Dao
abstract class DataDao : BaseDao<ScheduleEntity> {

    /**
     * Get all data from the Data table.
     */
    @Query("SELECT * FROM Schedule")
    abstract fun getData(): LiveData<List<ScheduleEntity>>
//
//    @Query("SELECT * FROM Schedule WHERE id = :id")
//    abstract fun selectById(id: Int): ScheduleEntity
//
//    @Query("SELECT * FROM Schedule")
//    abstract fun selectAll(): List<ScheduleEntity>
//
//    @Query("SELECT * FROM Schedule WHERE date = :date")
//    abstract fun selectCountByDate(date: String): Int
//
//    @Query("DELETE FROM Schedule WHERE date = :date")
//    abstract fun deleteByDate(date: String)

    @Delete
    abstract fun deleteSchedule(schedule: ScheduleEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSchedule(schedule: ScheduleEntity)

//    @Query("UPDATE Schedule SET isLastItem= :isLastItem WHERE title = :title")
//    abstract fun updateLastItem(title:String, isLastItem:Boolean)

}