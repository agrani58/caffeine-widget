package dev.eggie.caffeine

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews

class CaffeineWidget : AppWidgetProvider() {

    companion object {
        private const val ACTION_CLICK = "dev.eggie.caffeine.ACTION_CLICK"
        private const val PREFS = "caffeine_prefs"
        private const val KEY_ENABLED = "enabled"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("CaffeineWidget", "onUpdate for ids=${appWidgetIds.toList()}")
        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("CaffeineWidget", "onEnabled")
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(
            android.content.ComponentName(context, CaffeineWidget::class.java)
        )
        onUpdate(context, manager, ids)
    }

    private fun updateWidget(context: Context, manager: AppWidgetManager, widgetId: Int) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean(KEY_ENABLED, false)

        Log.d("CaffeineWidget", "updateWidget id=$widgetId enabled=$enabled")

        val views = RemoteViews(context.packageName, R.layout.widget_caffeine)

        views.setTextViewText(
            R.id.icon,
            if (enabled) "☕ ON" else "☕ OFF"
        )

        views.setTextColor(
            R.id.icon,
            if (enabled) Color.parseColor("#4CAF50") else Color.WHITE
        )

        // CRITICAL: Use CaffeineClickReceiver, not CaffeineWidget
        val intent = Intent(context, CaffeineClickReceiver::class.java).apply {
            action = ACTION_CLICK
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE        )

        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        manager.updateAppWidget(widgetId, views)
    }
}