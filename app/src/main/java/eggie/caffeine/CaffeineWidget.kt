package eggie.caffeine

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class CaffeineWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        for (widgetId in appWidgetIds) {

            val views = RemoteViews(
                context.packageName,
                android.R.layout.simple_list_item_1
            )

            views.setTextViewText(android.R.id.text1, "☕ Caffeine")

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }
}