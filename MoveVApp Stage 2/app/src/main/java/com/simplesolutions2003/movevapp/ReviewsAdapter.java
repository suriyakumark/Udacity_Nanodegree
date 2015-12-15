package com.simplesolutions2003.movevapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Suriya on 12/12/2015.
 */
public class ReviewsAdapter extends CursorAdapter{

    private final String LOG_TAG = ReviewsAdapter.class.getSimpleName();

    public static class ViewHolder {

        public final TextView reviewAuthorView;
        public final TextView reviewDescView;

        public ViewHolder(View view) {
            reviewAuthorView = (TextView) view.findViewById(R.id.review_author);
            reviewDescView = (TextView) view.findViewById(R.id.review_desc);
        }
    }

    public ReviewsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.v(LOG_TAG, "newView");
        int layoutId = R.layout.list_item_reviews;
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
            if(!cursor.getString(DetailFragment.COL_REVIEW_DESCRIPTION).isEmpty() && cursor.getString(DetailFragment.COL_REVIEW_DESCRIPTION) != null) {
                viewHolder.reviewAuthorView.setText(cursor.getString(DetailFragment.COL_REVIEW_AUTHOR));
                viewHolder.reviewDescView.setText(cursor.getString(DetailFragment.COL_REVIEW_DESCRIPTION));
            }
        }

    }

}