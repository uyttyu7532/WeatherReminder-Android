package com.example.tourweatherreminder.model

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.example.tourweatherreminder.MainAsyncTask
import com.example.tourweatherreminder.ScheduleList
import com.example.tourweatherreminder.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyJobService : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        Log.d("로그", "onStartJob: ${params.jobId} ${notificationContent}")

        ScheduleList.clear()
        val appDatabase = AppDatabase
        CoroutineScope(Dispatchers.IO).launch {
            var it = appDatabase?.getInstance(applicationContext)?.DataDao()?.getData2()
            it?.forEach {
                ScheduleList.add(it)
            }

            notificationContent = ""
            notificationResultCnt = 0
            //makeNotification(content)
            // 모든 날씨 정보를 다시 받아오는 작업
            for (i in ScheduleList) {
                MainAsyncTask(applicationContext).execute(i)
            }
        }
        return true
    }


    override fun onStopJob(params: JobParameters): Boolean {
        Log.d("로그", "onStopJob: ${params.jobId}")
        return false
    }
}