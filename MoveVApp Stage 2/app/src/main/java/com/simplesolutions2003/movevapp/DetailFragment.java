package com.simplesolutions2003.movevapp;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.simplesolutions2003.movevapp.data.MoviesContract;
import com.simplesolutions2003.movevapp.service.MoveVAppService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Suriya on 12/9/2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    private int dPosition;

    SimpleDateFormat DATE_API_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat DATE_DISP_FORMAT = new SimpleDateFormat("MMMM dd, yyyy");

    static final String DETAIL_MOVIE_ID = "MOVIE_ID";

    static final String BASE_THUMB_IMAGE_URL = new String("http://image.tmdb.org/t/p/w342/");
    static final String BASE_FULL_IMAGE_URL = new String("http://image.tmdb.org/t/p/w500/");

    private static final int DETAIL_LOADER = 0;
    private static final int TRAILER_LOADER = 1;
    private static final int REVIEW_LOADER = 2;
    private static final int SORTING_LOADER = 3;

    private static final String[] DETAIL_COLUMNS = {
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_NAME,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_RELEASE_DT,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_RATING,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_CAST,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_PLOT,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_FULL_IMG_URL,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_DURATION
    };

    private static final String[] TRAILER_COLUMNS = {
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry._ID,
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry.COLUMN_TYPE,
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry.COLUMN_SOURCE,
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry.COLUMN_NAME,
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry.COLUMN_URL
    };

    private static final String[] REVIEW_COLUMNS = {
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry._ID,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_AUTHOR,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_DESCRIPTION,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_URL
    };

    private static final String[] SORTING_COLUMNS = {
            MoviesContract.SortingEntry.TABLE_NAME + "." + MoviesContract.SortingEntry._ID,
            MoviesContract.SortingEntry.TABLE_NAME + "." + MoviesContract.SortingEntry.COLUMN_SORT_TYPE
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_NAME = 1;
    static final int COL_MOVIE_RELEASE_DT = 2;
    static final int COL_MOVIE_RATING = 3;
    static final int COL_MOVIE_CAST = 4;
    static final int COL_MOVIE_PLOT = 5;
    static final int COL_MOVIE_FULL_IMG_URL = 6;
    static final int COL_MOVIE_DURATION = 7;

    static final int COL_TRAILER_ID = 0;
    static final int COL_TRAILER_TYPE = 1;
    static final int COL_TRAILER_SOURCE = 2;
    static final int COL_TRAILER_NAME = 3;
    static final int COL_TRAILER_URL = 4;

    static final int COL_REVIEW_ID = 0;
    static final int COL_REVIEW_AUTHOR = 1;
    static final int COL_REVIEW_DESCRIPTION = 2;
    static final int COL_REVIEW_URL = 3;

    static final int COL_SORTING_ID = 0;
    static final int COL_SORTING_SORT_TYPE = 1;

    private String movieId;

    private ScrollView detailScrollView;
    private ImageView mImageView;
    private TextView mPlotView;
    private TextView mReleaseDateView;
    private TextView mDurationView;
    private RatingBar mRatingBarView;
    private ToggleButton markFavoriteButton;
    private TextView mLabel_empty_trailers;
    private TextView mLabel_empty_reviews;

    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private NonScrollListView mTrailerNonScrollListView;
    private NonScrollListView mReviewsNonScrollListView;

    public interface Callback {
    }

    //constructor
    public DetailFragment() {
    }

    //start the service here to get movie - trailers, reviews and other data
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dPosition = 0;
        Bundle arguments = getArguments();
        if (arguments != null) {
            movieId = arguments.getString(DETAIL_MOVIE_ID);
            runMovieService();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
    }

    //depending on the option selected, execute the async task passing choice of user
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTrailersAdapter = new TrailersAdapter(getActivity(), null, 0);
        mReviewsAdapter = new ReviewsAdapter(getActivity(), null, 0);

        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        detailScrollView = (ScrollView) rootView.findViewById(R.id.movie_detail);

        markFavoriteButton = (ToggleButton) rootView.findViewById(R.id.movie_favorite);
        mImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        mPlotView = (TextView) rootView.findViewById(R.id.movie_desc);
        mReleaseDateView = (TextView) rootView.findViewById(R.id.movie_releasedate);
        mRatingBarView = (RatingBar) rootView.findViewById(R.id.movie_rating);
        mDurationView = (TextView) rootView.findViewById(R.id.movie_duration);
        mTrailerNonScrollListView = (NonScrollListView) rootView.findViewById(R.id.movie_trailers);
        mReviewsNonScrollListView = (NonScrollListView) rootView.findViewById(R.id.movie_reviews);

        mTrailerNonScrollListView.setAdapter(mTrailersAdapter);
        mTrailerNonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //need to handle scroll to previous position when returning from youtube/share
                dPosition = detailScrollView.getScrollY();
                Log.v(LOG_TAG,"setOnItemClickListener - dPosition " + dPosition);

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                Utilities.watchYoutubeVideo(getActivity(), cursor.getString(COL_TRAILER_URL));

            }
        });

        mReviewsNonScrollListView.setAdapter(mReviewsAdapter);

        mLabel_empty_trailers = (TextView) rootView.findViewById(R.id.label_empty_trailers);
        mLabel_empty_reviews = (TextView) rootView.findViewById(R.id.label_empty_reviews);

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //initiate all loaders here
    public void onResume()
    {
        super.onResume();
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(SORTING_LOADER, null, this);
    }

    //check internet connection and then run the service to save time
    private void runMovieService() {
        if(Utilities.checkNow(getActivity())) {
            Intent serviceIntent = new Intent(getActivity(), MoveVAppService.class);
            serviceIntent.putExtra("MoveVAppService-Action", "MovieOtherDataSync");
            serviceIntent.putExtra("MoveVAppService-MovieId", movieId);
            getActivity().startService(serviceIntent);
        }else{
            Toast.makeText(getActivity(), "No data connection, only offline data is shown",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //check which loader is initiated and get appropriate cursor using content provider
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader - " + i + " loader");
        if(movieId != null){

            switch (i) {
                case DETAIL_LOADER:
                    Uri buildMoviesWithDetails = MoviesContract.MoviesEntry.buildMovieWithDetailUri(movieId);

                    return new CursorLoader(getActivity(),
                            buildMoviesWithDetails,
                            DETAIL_COLUMNS,
                            null,
                            null,
                            null);

                case TRAILER_LOADER:
                    Uri buildTrailersByMovieId = MoviesContract.TrailersEntry.buildTrailersByMovieId(movieId);

                    return new CursorLoader(getActivity(),
                            buildTrailersByMovieId,
                            TRAILER_COLUMNS,
                            null,
                            null,
                            null);

                case REVIEW_LOADER:
                    Uri buildReviewsByMovieId = MoviesContract.ReviewsEntry.buildReviewsByMovieId(movieId);

                    return new CursorLoader(getActivity(),
                            buildReviewsByMovieId,
                            REVIEW_COLUMNS,
                            null,
                            null,
                            null);

                case SORTING_LOADER:
                    Uri buildSortingByMovieId = MoviesContract.SortingEntry.buildSortingByMovieId(movieId);

                    return new CursorLoader(getActivity(),
                            buildSortingByMovieId,
                            SORTING_COLUMNS,
                            null,
                            null,
                            null);

                default:
                    break;
            }
        }
        return null;

    }

    //check which loader has completed and use the data accordingly
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "onLoadFinished - " + loader.getId() + " loader - " + cursor.getCount() + " rows retrieved");

        switch (loader.getId()) {
            case DETAIL_LOADER:
                if (cursor.getCount() > 0 && cursor != null) {
                    cursor.moveToFirst();
                    Log.v(LOG_TAG, "onLoadFinished Title - " + getActivity().getTitle());
                    if(getActivity().getTitle().toString().contains(getString(R.string.app_name))) {
                        getActivity().setTitle(getString(R.string.app_name) + " -> " + cursor.getString(COL_MOVIE_NAME));
                    }else{
                        getActivity().setTitle(cursor.getString(COL_MOVIE_NAME));
                    }
                    mTrailersAdapter.setMovieName(cursor.getString(COL_MOVIE_NAME));

                    Picasso.with(getActivity())
                            .load(BASE_FULL_IMAGE_URL + cursor.getString(COL_MOVIE_FULL_IMG_URL))
                            .placeholder(R.raw.loading)
                            .error(R.raw.no_image)
                            .noFade().resize(500, 750)
                            .centerCrop()
                            .into(mImageView);

                    mPlotView.setText(cursor.getString(COL_MOVIE_PLOT));
                    if(cursor.getString(COL_MOVIE_DURATION) != null) {
                        mDurationView.setText(cursor.getString(COL_MOVIE_DURATION) + getActivity().getString(R.string.units_duration));
                    }else{
                        mDurationView.setText("N/A");
                    }

                    try {
                        mReleaseDateView.setText(DATE_DISP_FORMAT.format(DATE_API_FORMAT.parse(cursor.getString(COL_MOVIE_RELEASE_DT))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        mReleaseDateView.setText("N/A");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        mReleaseDateView.setText("N/A");
                    }

                    if(cursor.getString(COL_MOVIE_RATING) != null){
                        mRatingBarView.setRating(Float.parseFloat(cursor.getString(COL_MOVIE_RATING)) / 2);
                    }
                }
                break;
            case TRAILER_LOADER:
                if(cursor.getCount() > 0){
                    mLabel_empty_trailers.setVisibility(View.GONE);
                }
                mTrailersAdapter.swapCursor(cursor);
                break;
            case REVIEW_LOADER:
                if(cursor.getCount() > 0){
                    mLabel_empty_reviews.setVisibility(View.GONE);
                }
                mReviewsAdapter.swapCursor(cursor);
                break;
            case SORTING_LOADER:
                cursor.moveToFirst();
                do {
                    Log.v(LOG_TAG, "onLoadFinished - " + cursor.getString(COL_SORTING_SORT_TYPE));
                    if (cursor.getString(COL_SORTING_SORT_TYPE).equals("favorite")) {
                        markFavoriteButton.setChecked(true);
                        break;
                    }
                } while (cursor.moveToNext());
                break;
        }

        //scroll to top, after listview are loaded it focuses on listview
        Log.v(LOG_TAG, "onLoadFinished - dPosition " + dPosition);
        if(dPosition > 0) {
            detailScrollView.scrollTo(0, dPosition);
        }else{
            detailScrollView.scrollTo(0, 0);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTrailersAdapter.swapCursor(null);
        mReviewsAdapter.swapCursor(null);
    }

    //handle mark as favorite action.
    //insert a row into sorting table for the movie id with type=favorite
    //sort_order is for future use
    public void onClickMarkFavorite(View view){
        ToggleButton markFavoriteButton = (ToggleButton) view.findViewById(R.id.movie_favorite);
        if(markFavoriteButton.isChecked()) {
            Log.v(LOG_TAG, "onClickMarkFavorite - ON " + movieId);

            ContentValues sortingValues = new ContentValues();

            sortingValues.put(MoviesContract.SortingEntry.COLUMN_MOVIE_ID, movieId);
            sortingValues.put(MoviesContract.SortingEntry.COLUMN_SORT_TYPE, "favorite");
            sortingValues.put(MoviesContract.SortingEntry.COLUMN_SORT_ORDER, "1");

            getActivity().getContentResolver().insert(MoviesContract.SortingEntry.CONTENT_URI, sortingValues);

        }else{
            Log.v(LOG_TAG, "onClickMarkFavorite - OFF " + movieId);

            String deleteFavoriteWhere;
            String[] deleteFavoriteArgs;

            deleteFavoriteWhere = MoviesContract.SortingEntry.COLUMN_MOVIE_ID + " = ? AND " + MoviesContract.SortingEntry.COLUMN_SORT_TYPE + " = ? ";
            deleteFavoriteArgs = new String[]{movieId, "favorite"};
            getActivity().getContentResolver().delete(MoviesContract.SortingEntry.CONTENT_URI, deleteFavoriteWhere,deleteFavoriteArgs);

        }
    }

}
