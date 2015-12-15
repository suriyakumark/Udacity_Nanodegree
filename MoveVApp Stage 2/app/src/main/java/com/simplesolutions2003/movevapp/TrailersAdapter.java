package com.simplesolutions2003.movevapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Suriya on 12/12/2015.
 */
public class TrailersAdapter extends CursorAdapter implements OnClickListener{

    private final String LOG_TAG = TrailersAdapter.class.getSimpleName();
    private Context context;
    private String movieName;

    public static class ViewHolder {

        public final ImageButton trailerPlayView;
        public final TextView trailerNameView;
        public final ImageButton trailerShareView;

        public ViewHolder(View view) {
            trailerPlayView = (ImageButton) view.findViewById(R.id.trailer_play);
            trailerNameView = (TextView) view.findViewById(R.id.trailer_name);
            trailerShareView = (ImageButton) view.findViewById(R.id.trailer_share);
        }
    }

    public TrailersAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.v(LOG_TAG, "newView");
        int layoutId = R.layout.list_item_trailers;
        View view = (View) LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.v(LOG_TAG, "bindView");
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(cursor != null){
            if(!cursor.getString(DetailFragment.COL_TRAILER_NAME).isEmpty() && cursor.getString(DetailFragment.COL_TRAILER_NAME) != null) {
                viewHolder.trailerNameView.setText(cursor.getString(DetailFragment.COL_TRAILER_NAME));

                ImageButton trailerPlayView = (ImageButton) view.findViewById(R.id.trailer_play);
                trailerPlayView.setTag(new String[]{cursor.getString(DetailFragment.COL_TRAILER_URL),cursor.getString(DetailFragment.COL_TRAILER_NAME)});
                trailerPlayView.setOnClickListener(this);

                ImageButton trailerShareView = (ImageButton) view.findViewById(R.id.trailer_share);
                trailerShareView.setTag(new String[]{cursor.getString(DetailFragment.COL_TRAILER_URL),cursor.getString(DetailFragment.COL_TRAILER_NAME)});
                trailerShareView.setOnClickListener(this);
            }
        }

    }

    public void setMovieName(String name){
        movieName = name;
    }

    public void onClick(View v) {

        Log.v(LOG_TAG, "onClick" + v.getTag());
        String[] params = (String[]) v.getTag();
        String url = params[0];
        String name = params[1];

        switch(v.getId()) {
            case R.id.trailer_play:
                Utilities.watchYoutubeVideo(context, url);
                break;
            case R.id.trailer_share:
                Utilities.shareYoutubeVideo(context, url, name, movieName);
                break;
        }
    }



}