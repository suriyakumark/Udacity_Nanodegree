package com.simplesolutions2003.movevapp;

/**
 * Created by Suriya on 10/22/2015.
 */

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.simplesolutions2003.movevapp.service.MoveVAppService;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetailActivity extends ActionBarActivity implements DetailFragment.Callback  {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    DetailFragment detailFragment;

    //start the detail fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            //get the selected movie id passed from main activity and pass it to the fragment.
            //if no movie id, do not call the fragment piece
            Bundle arguments = new Bundle();
            String movieId = getIntent().getStringExtra(DetailFragment.DETAIL_MOVIE_ID);
            arguments.putString(DetailFragment.DETAIL_MOVIE_ID, movieId );

            if(movieId != null) {
                detailFragment = new DetailFragment();
                detailFragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_container, detailFragment)
                        .commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailfragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //handle the mark favorite action
    public void onClickMarkFavorite(View view){
        detailFragment.onClickMarkFavorite(view);
    }

}