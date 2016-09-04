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
 * Created by SuriyaKumar on 8/30/2016.
 */
public class BabyAdapter extends CursorAdapter implements View.OnClickListener {

    private final String TAG = BabyAdapter.class.getSimpleName();
    private Context context;

    public static class ViewHolder {

        public final ImageView babyProfilePhoto;
        public final TextView babyName;
        public final TextView babyBirthDate;
        public final TextView babyGender;
        public final ImageButton babySelect;
        public final ImageButton babyEdit;

        public ViewHolder(View view) {
            babyProfilePhoto = (ImageView) view.findViewById(R.id.baby_prof_image);
            babyName = (TextView) view.findViewById(R.id.baby_name);
            babyBirthDate = (TextView) view.findViewById(R.id.baby_birthdate);
            babyGender = (TextView) view.findViewById(R.id.baby_gender);
            babySelect = (ImageButton) view.findViewById(R.id.baby_select);
            babyEdit = (ImageButton) view.findViewById(R.id.baby_edit);
        }
    }


    public BabyAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.v(TAG, "newView");
        int layoutId = R.layout.baby_list_item;
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
            Uri babyProfilePhotoUri = null;
            if(cursor.getString(BabyFragment.COL_BABY_PHOTO) != null) {
                babyProfilePhotoUri = Uri.parse(cursor.getString(BabyFragment.COL_BABY_PHOTO));
            }else{
                babyProfilePhotoUri = null;
            }
            Log.v(TAG, "babyProfilePhotoUri - " + babyProfilePhotoUri);
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e)
                {
                    e.printStackTrace();
                }
            });

            builder.build()
                    .load(babyProfilePhotoUri)
                    .transform(new BitmapTransform((int) context.getResources().getDimension(R.dimen.profile_photo_width),(int) context.getResources().getDimension(R.dimen.profile_photo_height)))
                    .resize((int)context.getResources().getDimension(R.dimen.profile_photo_width),(int)context.getResources().getDimension(R.dimen.profile_photo_height))
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .skipMemoryCache()
                    .centerCrop()
                    .into(viewHolder.babyProfilePhoto);
            viewHolder.babyName.setText(cursor.getString(BabyFragment.COL_BABY_NAME));
            viewHolder.babyBirthDate.setText(cursor.getString(BabyFragment.COL_BABY_BIRTH_DATE));
            viewHolder.babyGender.setText(cursor.getString(BabyFragment.COL_BABY_GENDER));

            viewHolder.babySelect.setTag(new String[]{Long.toString(cursor.getLong(BabyFragment.COL_BABY_ID))});
            viewHolder.babySelect.setOnClickListener(this);

            viewHolder.babyEdit.setTag(new String[]{Long.toString(cursor.getLong(BabyFragment.COL_BABY_ID))});
            viewHolder.babyEdit.setOnClickListener(this);
        }

    }

    public void onClick(View v) {

        Log.v(TAG, "onClick" + v.getTag());
        String[] params = (String[]) v.getTag();
        Long babyId = Long.parseLong(params[0]);

        switch(v.getId()) {
            case R.id.baby_select:
                MainActivity.ACTIVE_BABY_ID = babyId;
                ((MainActivity) context).handleFragments(new ActivitiesFragment(), ActivitiesFragment.TAG, ActivitiesFragment.KEEP_IN_STACK);
                break;
            case R.id.baby_edit:
                MainActivity.ACTIVE_BABY_ID = babyId;
                ((MainActivity) context).handleFragments(new BabyProfileFragment(),BabyProfileFragment.TAG,BabyProfileFragment.KEEP_IN_STACK);
                break;
        }
    }

}
