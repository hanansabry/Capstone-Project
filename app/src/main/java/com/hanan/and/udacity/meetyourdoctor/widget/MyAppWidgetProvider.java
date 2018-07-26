package com.hanan.and.udacity.meetyourdoctor.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.hanan.and.udacity.meetyourdoctor.R;
import com.hanan.and.udacity.meetyourdoctor.activities.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = getSpecialistsListRemoteViews(context);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static RemoteViews getSpecialistsListRemoteViews(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_provider);

        Intent intent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_list, intent);

        Intent specialistsIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, specialistsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);

        //set empty view
        views.setEmptyView(R.id.widget_list, R.id.empty_view);
        //set action to open the activity when click on the empty_view
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity
                (context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.empty_view, appPendingIntent);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
