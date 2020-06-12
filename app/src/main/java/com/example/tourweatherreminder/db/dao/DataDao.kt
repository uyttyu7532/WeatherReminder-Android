package com.example.tourweatherreminder.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tourweatherreminder.db.entity.ScheduleEntity
import io.reactivex.Maybe


@Dao
abstract class DataDao : BaseDao<ScheduleEntity> {

    /**
     * Get all data from the Data table.
     */
    @Query("SELECT * FROM Schedule")
    abstract suspend fun getData(): List<ScheduleEntity>

    @Query("SELECT * FROM Schedule WHERE id = :id")
    abstract fun selectById(id: Int): Maybe<ScheduleEntity>

    @Query("SELECT * FROM Schedule")
    abstract fun selectAll(): Maybe<List<ScheduleEntity>>

    @Query("SELECT * FROM Schedule WHERE date = :date")
    abstract fun selectCountByDate(date: String): Int

    @Query("DELETE FROM Schedule WHERE date = :date")
    abstract fun deleteByDate(date: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSchedule(schedule: ScheduleEntity)

}