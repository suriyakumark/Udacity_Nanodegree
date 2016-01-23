package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.MainScreenFragment;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;
import barqsoft.footballscores.data.DatabaseContract;

/**
 * Created by Suriya on 1/18/2016.
 */
public class ScoresWidgetRemoteViewsService extends RemoteViewsService {

    public static final String LOG_TAG = ScoresWidgetRemoteViewsService.class.getSimpleName();
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
                Log.v(LOG_TAG, "onCreate");
            }

            @Override
            public void onDataSetChanged() {
                Log.v(LOG_TAG, "onDataSetChanged");
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                //Widget shows only today's data.
                Date fragmentdate = new Date(System.currentTimeMillis()+((0)*86400000));
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                Log.v(LOG_TAG, "fragmentdate - " + fragmentdate);
                data = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
                        null,null,new String[]{mformat.format(fragmentdate)},null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                Log.v(LOG_TAG, "getViewAt - " + position);
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_scores_list);

                views.setTextViewText(R.id.home_name, data.getString(COL_HOME));
                views.setContentDescription(R.id.home_name, getString(R.string.cd_home_name) + data.getString(COL_HOME));

                views.setTextViewText(R.id.away_name, data.getString(COL_AWAY));
                views.setContentDescription(R.id.away_name, getString(R.string.cd_away_name) + data.getString(COL_AWAY));

                views.setTextViewText(R.id.data_textview, data.getString(COL_MATCHTIME));
                views.setContentDescription(R.id.data_textview, getString(R.string.cd_time) + data.getString(COL_MATCHTIME));

                views.setTextViewText(R.id.score_textview, Utilities.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS),getApplicationContext()));
                views.setContentDescription(R.id.score_textview, getString(R.string.cd_scores) + Utilities.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS),getApplicationContext()));

                final Intent fillInIntent = new Intent();
                fillInIntent.setData(DatabaseContract.scores_table.buildScoreWithDate());
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_scores_list);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
