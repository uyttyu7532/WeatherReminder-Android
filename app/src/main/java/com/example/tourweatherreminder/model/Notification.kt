package com.example.tourweatherreminder.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tourweatherreminder.R
import com.example.tourweatherreminder.mContext


var notificationContent: String = ""
var notificationResultCnt: Int = 0

fun makeNotification() {
    if (mContext == null || notificationContent == "") {
        return
    }
    var CHANNEL_ID = "hi"
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

    val builder = NotificationCompat.Builder(mContext, CHANNEL_ID)
    builder.setSmallIcon(R.drawable.icon01)
    builder.setContentTitle(title)
    builder.setContentText(content)
    builder.priority = NotificationCompat.PRIORITY_HIGH
    builder.setAutoCancel(true)

    val notificationManager = NotificationManagerCompat.from(mContext)
    notificationManager.notify(notiId, builder.build())
}

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