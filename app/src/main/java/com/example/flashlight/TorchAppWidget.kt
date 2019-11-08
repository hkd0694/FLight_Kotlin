package com.example.flashlight

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class TorchAppWidget : AppWidgetProvider() {

    //위젯이 업데이트되어야 할 때 호출된다.
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for(appWidgetId in appWidgetIds){
            //위젯이 여러 개 배치되었다면 모든 위젯을 업데이트 한다.
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    //위젯이 처음 생성될 때 호출된다.
    override fun onEnabled(context: Context?) {

    }

    //여러 개일 경우 마지막 위젯이 제거될 때 호출된다.
    override fun onDisabled(context: Context?) {

    }

    companion object {
        //위젯을 업데이트할 때 수행되는 코드이다.
        internal  fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId : Int){
            val widgetText = context.getString(R.string.appwidget_text)
            //Construct the RemoteViews object
            //위젯은 액티비티에서 레이아웃을 다루는 것과는 조금 다르다.
            //위젯에 배치하는 뷰는 따로 있다. 그것들을 RemoteViews  객체로 가져올 수 있다.
            val views = RemoteViews(context.packageName,R.layout.torch_app_widget)

            //setTextViewText() 메서드는 RemoteViews 객체용으로 준비된 텍스트 값을 변경하는 메서드이다.
            views.setTextViewText(R.id.appwidget_text, widgetText)

            //실행할 Intent 작성
            val intent = Intent(context, TorchService::class.java)
            val pendingIntent = PendingIntent.getService(context,0,intent,0)

            //위젯을 클릭하면 위에서 정의한 Intent 실행
            views.setOnClickPendingIntent(R.id.appwidget_layout,pendingIntent)

            //레이아웃을 모두 수정했다면 AppWidgetManager를 사용해 위젯을 업데이트 한다.
            appWidgetManager.updateAppWidget(appWidgetId,views)
        }
    }
}