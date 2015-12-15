package com.simplesolutions2003.movevapp;

import com.simplesolutions2003.movevapp.data.MoviesContract;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;

import com.simplesolutions2003.movevapp.DetailActivity;
import com.simplesolutions2003.movevapp.service.MoveVAppService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private MoviesAdapter mMoviesAdapter;
    private GridView gridView;
    private int mPosition = GridView.INVALID_POSITION;
    private String sortType = "most_popular";
    private static final String SELECTED_KEY = "selected_position";
    boolean mTwoPane = false;
    static final String BASE_THUMB_IMAGE_URL = new String("http://image.tmdb.org/t/p/w342/");
    static final String BASE_FULL_IMAGE_URL = new String("http://image.tmdb.org/t/p/w500/");

    private static final int MOVIES_LOADER = 0;

    private static final String[] MOVIES_COLUMNS = {
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_THUMB_IMG_URL
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_THUMB_IMG_URL = 1;

    public interface Callback {
        public void onItemSelected(String movieId);
    }

    //constructor
    public MoviesFragment() {
    }

    //run the service to get movies for the filter chosen
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        runMovieService(sortType);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    //depending on the option selected, restart the loader
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_high_rating) {
            sortType = "high_rating";
            runMovieService(sortType);
        }else if (id == R.id.action_most_popular) {
            sortType = "most_popular";
            runMovieService(sortType);
        }else if (id == R.id.action_most_rating) {
            sortType = "most_rating";
            runMovieService(sortType);
        }else if (id == R.id.action_favorite) {
            sortType = "favorite";
        }
        Log.v(LOG_TAG, "Sort Type - " + sortType);

        onSortTypeChanged();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");

        mMoviesAdapter = new MoviesAdapter(getActivity(),null,0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMoviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null && cursor.getCount() > 0) {
                    ((Callback) getActivity())
                            .onItemSelected(Long.toString(cursor.getLong(COL_MOVIE_ID)));
                }
                mPosition = position;
                Log.v(LOG_TAG, "Position selected by user - " + position);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    void onSortTypeChanged( ) {
        mPosition = 0;
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    //check data connection and then run the service, to save some time
    private void runMovieService(String sortType) {

        if(Utilities.checkNow(getActivity())) {
            Log.v(LOG_TAG, "Service starting - MoviesSync " + sortType);
            Intent serviceIntent = new Intent(getActivity(), MoveVAppService.class);
            serviceIntent.putExtra("MoveVAppService-Action", "MoviesSync");
            serviceIntent.putExtra("MoveVAppService-SortType", sortType);
            getActivity().startService(serviceIntent);
        }else{
            Toast.makeText(getActivity(), "No data connection, only offline data is shown",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader");
        Uri buildMoviesBySort = MoviesContract.SortingEntry.buildMoviesBySort(sortType);

        return new CursorLoader(getActivity(),
                buildMoviesBySort,
                MOVIES_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "onLoadFinished - " + cursor.getCount() + " rows retrieved");
        mMoviesAdapter.swapCursor(cursor);
        if(cursor.getCount()>0 && cursor != null) {
            if (mPosition != GridView.INVALID_POSITION) {
                gridView.smoothScrollToPosition(mPosition);
            } else {
                mPosition = 0;
            }
            if (mTwoPane) {
                selectDefaultOnSortChange();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(LOG_TAG, "onLoaderReset");
        mMoviesAdapter.swapCursor(null);
    }

    //activity will tell the fragment if its tablet or phone
    public void setTwoPane(){
        mTwoPane = true;
    }

    //select the passed item in the gridview to display in tablet detail pane to avoid showing empty view
    public void selectDefaultOnSortChange(){
        gridView.postDelayed(new Runnable() {
            @Override
            public void run() {
                gridView.setSelection(mPosition);
                gridView.performItemClick(gridView.getChildAt(mPosition), mPosition, gridView.getAdapter().getItemId(mPosition));
            }
        }, 500);
    }

}
