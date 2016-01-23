package barqsoft.footballscores.widget;

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

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Created by Suriya on 1/17/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoresWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = ScoresWidgetProvider.class.getSimpleName();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(LOG_TAG, "onUpdate");
        for (int appWidgetId : appWidgetIds) {
            Log.v(LOG_TAG, "widget - " + appWidgetIds.toString());
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_today);

            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, remoteViews);
            } else {
                setRemoteAdapterV11(context, remoteViews);
            }

            remoteViews.setTextViewText(R.id.widgetTextLoading, context.getString(R.string.widget_loaded) + sdf.format(new Date()));
            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.scores_list);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        Log.v(LOG_TAG, "onReceive");
        super.onReceive(context, intent);
        //if (SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.scores_list);
        //}
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        Log.v(LOG_TAG, "setRemoteAdapter");
        views.setRemoteAdapter(R.id.scores_list,
                new Intent(context, ScoresWidgetRemoteViewsService.class));
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        Log.v(LOG_TAG, "setRemoteAdapterV11");
        views.setRemoteAdapter(0, R.id.scores_list,
                new Intent(context, ScoresWidgetRemoteViewsService.class));
    }
}
