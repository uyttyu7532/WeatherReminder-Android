package com.example.tourweatherreminder.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tourweatherreminder.db.entity.ScheduleEntity


@Dao
abstract class DataDao : BaseDao<ScheduleEntity> {

    // 전체 DB에 저장된 데이터 가져오기(LiveData)
    @Query("SELECT * FROM Schedule ORDER BY timestamp ")
    abstract fun getData(): LiveData<List<ScheduleEntity>>

    // 전체 DB에 저장된 데이터 가져오기
    @Query("SELECT * FROM Schedule ORDER BY timestamp ")
    abstract fun getData2(): List<ScheduleEntity>

    // 해당 title이 이미 존재하는지
//    @Query("SELECT exists (SELECT title FROM Schedule WHERE title = :title LIMIT 1)")
//    abstract fun getItemTitle(title: String): Boolean

    // title로 ScheduleEntity 찾기 (수정/ 추가모드 위해 데이터 존재 여부 확인)
    @Query("SELECT * FROM Schedule WHERE title = :title")
    abstract fun getScheduleByTitle(title: String): ScheduleEntity

    @Delete
    abstract fun deleteSchedule(schedule: ScheduleEntity)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateSchedule(schedule: ScheduleEntity)

    @Insert
    abstract fun insertSchedule(schedule: ScheduleEntity)
}