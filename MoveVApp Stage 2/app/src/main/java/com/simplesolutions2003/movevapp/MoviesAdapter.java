package com.simplesolutions2003.movevapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.support.v4.widget.CursorAdapter;

import com.simplesolutions2003.movevapp.data.MoviesContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Suriya on 12/10/2015.
 */
public class MoviesAdapter extends CursorAdapter {
    private final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.list_item_movies_imgview);
        }
    }

    public MoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.v(LOG_TAG, "newView");
        int layoutId = R.layout.list_item_movies;
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(imageView);
        imageView.setTag(viewHolder);

        return imageView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.v(LOG_TAG, "bindView");
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        ImageView imageView = viewHolder.imageView;
        imageView.setAdjustViewBounds(true);

        Picasso.with(context)
                .load(MoviesFragment.BASE_THUMB_IMAGE_URL + cursor.getString(MoviesFragment.COL_MOVIE_THUMB_IMG_URL))
                .placeholder(R.raw.loading)
                .error(R.raw.no_image)
                .noFade().resize(342,491)
                .centerCrop()
                .into(imageView);

    }

}