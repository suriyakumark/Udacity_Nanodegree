package com.simplesolutions2003.happybabycare;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by SuriyaKumar on 9/3/2016.
 */
public class ActivitiesAdapter extends CursorAdapter {

    private final String TAG = BabyAdapter.class.getSimpleName();
    private Context context;

    public static class ViewHolder {

        public final ImageView activityIcon;
        public final TextView activityDate;
        public final TextView activityTime;
        public final TextView activitySummary;
        public final TextView activityDetail;

        public ViewHolder(View view) {
            activityIcon = (ImageView) view.findViewById(R.id.activity_icon);
            activityDate = (TextView) view.findViewById(R.id.activity_date);
            activityTime = (TextView) view.findViewById(R.id.activity_time);
            activitySummary = (TextView) view.findViewById(R.id.activity_summary);
            activityDetail = (TextView) view.findViewById(R.id.activity_detail);
        }
    }


    public ActivitiesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.v(TAG, "newView");
        int layoutId = R.layout.activities_list_item;
        View view = (View) LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.v(TAG, "bindView");
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(cursor != null){

            switch (cursor.getString(ActivitiesFragment.COL_ACTIVITY_TYPE)){
                case "Diaper":
                    viewHolder.activityIcon.setImageResource(R.drawable.ic_menu_diaper);
                    break;
                case "Feeding":
                    viewHolder.activityIcon.setImageResource(R.drawable.ic_menu_feeding);
                    break;
                case "Sleeping":
                    viewHolder.activityIcon.setImageResource(R.drawable.ic_menu_sleeping);
                    break;
                case "Health":
                    viewHolder.activityIcon.setImageResource(R.drawable.ic_menu_health);
                    break;
                default:
                    viewHolder.activityIcon.setImageResource(R.drawable.logo);
                    break;
            }

            viewHolder.activityDate.setText(cursor.getString(ActivitiesFragment.COL_ACTIVITY_DATE));
            viewHolder.activityTime.setText(cursor.getString(ActivitiesFragment.COL_ACTIVITY_TIME));
            viewHolder.activitySummary.setText(cursor.getString(ActivitiesFragment.COL_ACTIVITY_SUMMARY));
            viewHolder.activityDetail.setText(cursor.getString(ActivitiesFragment.COL_ACTIVITY_DETAIL));

        }

    }

}
