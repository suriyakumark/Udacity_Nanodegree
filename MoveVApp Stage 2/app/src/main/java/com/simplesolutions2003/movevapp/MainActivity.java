package com.simplesolutions2003.movevapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.simplesolutions2003.movevapp.service.MoveVAppService;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback  {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    //used to check if its tablet or phone
    private boolean mTwoPane;
    MoviesFragment moviesFragment;
    DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.detail_container) != null){
            mTwoPane = true;
            Log.v(LOG_TAG, "Mode - Tablet");

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, new DetailFragment())
                        .commit();
            }

        }else{
            mTwoPane = false;
            Log.v(LOG_TAG, "Mode - Phone");
            if(getSupportActionBar() != null) {
                getSupportActionBar().setElevation(0f);
            }
        }

        moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        if(mTwoPane) {
            moviesFragment.setTwoPane();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemSelected(String movieId) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(DetailFragment.DETAIL_MOVIE_ID, movieId);

            detailFragment = new DetailFragment();
            detailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, detailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailFragment.DETAIL_MOVIE_ID, movieId);
            startActivity(intent);
        }
    }

    //handle mark favorite for tablet
    public void onClickMarkFavorite(View view){
        if(view != null) {
            detailFragment.onClickMarkFavorite(view);
        }
    }
}
