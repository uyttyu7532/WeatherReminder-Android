package com.example.tourweatherreminder

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.example.tourweatherreminder.db.AppDatabase

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {

    // 위젯이 설치될 때마다 호출되는 함수수
   override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    // 앱 위젯이 최초로 설치되는 순간 호출
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    // 위젯이 제거되는 순간 호출
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


// 위젯의 크기 및 옵션이 변경될 때 마다 호출되는 함수
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    // RemoteViews는 런처앱에 업데이트를 의뢰할 때 의뢰 내용 저장하는 클래스

    var WidgetSchedule = ScheduleList[0]


    // 실제 데이터와 연결을 해야하는데
    // 어떤 일정의 날씨를 선택할 것인지, 어떻게 위젯에서 업데이트 시킬 것인지..

    Log.i("로그",WidgetSchedule.toString())
    MainAsyncTask(context).execute(WidgetSchedule)
    Log.i("로그",WidgetSchedule.toString())

    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setImageViewResource(R.id.widget_weather_icon, R.drawable.icon02)
    views.setTextViewText(R.id.widget_title, WidgetSchedule.title)
    views.setTextViewText(R.id.widget_date, WidgetSchedule.date)
    views.setTextViewText(R.id.widget_place, WidgetSchedule.place)
    views.setTextViewText(R.id.widget_temperature_degree, WidgetSchedule.temp.toString())
    views.setTextViewText(R.id.widget_rain_percentage, WidgetSchedule.rain.toString())


    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}