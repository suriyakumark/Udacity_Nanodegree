package barqsoft.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.ViewHolder;

/**
 * Created by Suriya on 1/19/2016.
 */
public class ScoresWidgetAdapter extends CursorAdapter{

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

        private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

        public ScoresWidgetAdapter(Context context,Cursor cursor,int flags)
        {
            super(context,cursor,flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            View mItem = LayoutInflater.from(context).inflate(R.layout.widget_scores_list, parent, false);
            ViewHolder mHolder = new ViewHolder(mItem);
            mItem.setTag(mHolder);
            return mItem;
        }

        @Override
        public void bindView(View view, final Context context, Cursor cursor)
        {
            final ViewHolder mHolder = (ViewHolder) view.getTag();
            mHolder.home_name.setText(cursor.getString(COL_HOME));
            mHolder.away_name.setText(cursor.getString(COL_AWAY));
            mHolder.date.setText(cursor.getString(COL_MATCHTIME));
            mHolder.score.setText(Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
            mHolder.match_id = cursor.getDouble(COL_ID);

        }

}
