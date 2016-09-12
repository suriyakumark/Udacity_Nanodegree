package com.simplesolutions2003.happybabycare.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.simplesolutions2003.happybabycare.MainActivity;
import com.simplesolutions2003.happybabycare.R;

/**
 * Created by SuriyaKumar on 9/10/2016.
 */
public class ActivitiesWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = ActivitiesWidgetProvider.class.getSimpleName();

    public static final String REFRESH_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(LOG_TAG, "onUpdate");
        for (int appWidgetId : appWidgetIds) {
            Log.v(LOG_TAG, "widget - " + appWidgetIds.toString());
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_activities);

            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

            setRemoteAdapter(context, remoteViews);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetAdapterViewFlipper);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        Log.v(LOG_TAG, "onReceive");
        super.onReceive(context, intent);

        String action = intent.getAction();

        if (action.equals(REFRESH_ACTION)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetAdapterViewFlipper);
        }

    }

    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        Log.v(LOG_TAG, "setRemoteAdapter");
        views.setRemoteAdapter(R.id.widgetAdapterViewFlipper,
                new Intent(context, ActivitiesWidgetRemoteViewsService.class));
    }

}
