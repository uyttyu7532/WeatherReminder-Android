package com.example.tourweatherreminder.model

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tourweatherreminder.MainActivity
import com.example.tourweatherreminder.R
import com.example.tourweatherreminder.mContext


var notificationContent: String = ""
var notificationResultCnt: Int = 0

// 상태바 알림 생성
fun makeNotification() {
    if (mContext == null || notificationContent == "") {
        return
    }
    var CHANNEL_ID = "notification_weather"
    var notiId = 123
    createNotificationChannel(
        mContext,
        NotificationManager.IMPORTANCE_HIGH,
        true,
        CHANNEL_ID,
        CHANNEL_ID
    )

    val title = "날씨 알림이"
    val content = notificationContent

    val notifyIntent = Intent(mContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val notifyPendingIntent = PendingIntent.getActivity(
        mContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(mContext, CHANNEL_ID)
    builder.setSmallIcon(R.mipmap.ic_launcher)
    builder.setContentTitle(title)
    builder.setContentText(content)
    builder.setContentIntent(notifyPendingIntent)
    builder.priority = NotificationCompat.PRIORITY_HIGH
    builder.setAutoCancel(true)
    builder.setStyle(
        NotificationCompat.BigTextStyle()
            .bigText(content)
    )

    val notificationManager = NotificationManagerCompat.from(mContext)
    notificationManager.notify(notiId, builder.build())
}

// 알림 채널 생성
fun createNotificationChannel(
    context: Context, importance: Int, showBadge: Boolean,
    name: String, description: String
) {
    val channel = NotificationChannel(name, name, importance)
    channel.description = description
    channel.setShowBadge(showBadge)

    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)
}