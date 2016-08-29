package com.simplesolutions2003.happybabycare;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import com.simplesolutions2003.happybabycare.R;

/**
 * Created by SuriyaKumar on 8/21/2016.
 */
public class StoryDetailAdapter extends CursorAdapter {
    private final String TAG = StoryDetailAdapter.class.getSimpleName();
    private Context context;
/*
    public static class ViewHolder {

        public final ImageView imageView;
        public final TextView textView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.story_list_image);
            textView = (TextView) view.findViewById(R.id.story_list_text);
        }

    }
*/
    public StoryDetailAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
/*
        Log.v(TAG, "newView");
        int layoutId = R.layout.story_item;
        View view = (View) LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
*/
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
/*
        Log.v(TAG, "bindView");
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(cursor != null){
            if(cursor.getString(StoryDetailFragment.COL_ARTICLE_DETAIL_TYPE).equals("IMAGE")) {
                Picasso.with(context)
                        .load(cursor.getString(StoryDetailFragment.COL_LOG_TIMESTAMP))
                        .placeholder(R.raw.loading)
                        .error(R.raw.no_image)
                        .noFade().resize(342,491)
                        .centerCrop()
                        .into(viewHolder.imageView);

            }
            if(cursor.getString(StoryDetailFragment.COL_ARTICLE_DETAIL_TYPE).equals("TEXT")) {
                viewHolder.textView.setText(cursor.getString(StoryDetailFragment.COL_ARTICLE_DETAIL_CONTENT));
            }
        }
        */
    }
}
