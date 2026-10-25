package dev.eggie.caffeine

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast

class CaffeineClickReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION_CLICK = "dev.eggie.caffeine.ACTION_CLICK"

        private const val PREFS = "caffeine_prefs"
        private const val KEY_ENABLED = "enabled"
        private const val KEY_PREVIOUS_TIMEOUT = "previous_timeout"
    }

    override fun onReceive(context: Context, intent: Intent) {

        Log.d("CaffeineClick", "Receiver fired: ${intent.action}")

        if (intent.action != ACTION_CLICK) return

        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean(KEY_ENABLED, false)

        val canWrite = Settings.System.canWrite(context)

        Log.d("CaffeineClick", "canWrite=$canWrite")

        if (!canWrite) {

            Toast.makeText(
                context,
                "Please allow Modify system settings",
                Toast.LENGTH_LONG
            ).show()

            val settingsIntent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            context.startActivity(settingsIntent)
            return
        }

        try {

            if (!enabled) {

                val currentTimeout = Settings.System.getInt(
                    context.contentResolver,
                    Settings.System.SCREEN_OFF_TIMEOUT
                )

                prefs.edit()
                    .putInt(KEY_PREVIOUS_TIMEOUT, currentTimeout)
                    .apply()

                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_OFF_TIMEOUT,
                    Int.MAX_VALUE
                )

                prefs.edit()
                    .putBoolean(KEY_ENABLED, true)
                    .apply()

                Toast.makeText(context, "Caffeine ON", Toast.LENGTH_SHORT).show()

            } else {

                val previousTimeout =
                    prefs.getInt(KEY_PREVIOUS_TIMEOUT, 60000)

                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_OFF_TIMEOUT,
                    previousTimeout
                )

                prefs.edit()
                    .putBoolean(KEY_ENABLED, false)
                    .apply()

                Toast.makeText(context, "Caffeine OFF", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {

            Log.e("CaffeineClick", "Failed", e)

            Toast.makeText(
                context,
                e.message ?: "Unknown error",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(
            ComponentName(context, CaffeineWidget::class.java)
        )

        ids.forEach {
            updateWidget(context, manager, it)
        }
    }

    private fun updateWidget(
        context: Context,
        manager: AppWidgetManager,
        widgetId: Int
    ) {

        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean(KEY_ENABLED, false)

        val views = RemoteViews(
            context.packageName,
            R.layout.widget_caffeine
        )

        views.setImageViewResource(
            R.id.icon,
            if (enabled) R.drawable.ic_cup_on else R.drawable.ic_cup_off
        )

        val clickIntent = Intent(
            context,
            CaffeineClickReceiver::class.java
        ).apply {
            action = ACTION_CLICK
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            clickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        views.setOnClickPendingIntent(
            R.id.widget_root,
            pendingIntent
        )

        manager.updateAppWidget(widgetId, views)
    }
}
