package com.example.tourweatherreminder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tourweatherreminder.db.dao.DataDao
import com.example.tourweatherreminder.db.entity.ScheduleEntity

@Database(entities = arrayOf(ScheduleEntity::class), version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun DataDao(): DataDao

    companion object { // 싱글톤
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "schedule_db.db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}
