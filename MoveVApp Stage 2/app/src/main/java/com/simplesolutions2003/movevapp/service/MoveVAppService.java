package com.simplesolutions2003.movevapp.service;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.simplesolutions2003.movevapp.BuildConfig;
import com.simplesolutions2003.movevapp.R;
import com.simplesolutions2003.movevapp.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by Suriya on 12/6/2015.
 */
public class MoveVAppService extends IntentService{

    private final String LOG_TAG = MoveVAppService.class.getSimpleName();
    public MoveVAppService() {
        super("MoveVApp");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String serviceAction = intent.getStringExtra("MoveVAppService-Action");
        Log.v(LOG_TAG, "Service Action - " + serviceAction);

        switch(serviceAction){
            case "MoviesSync":
                String sortType = intent.getStringExtra("MoveVAppService-SortType");
                if(sortType == null){
                    sortType = "";
                }
                getMoviesBySortFromAPI(sortType);
                break;
            case "AllTrailersSync":
            case "AllReviewsSync":
            case "AllMovieAddlDataSync":
            case "AllOtherDataSync":
                getOtherDataFromAPI(serviceAction);
                break;
            case "MovieOtherDataSync":
                String movieId = intent.getStringExtra("MoveVAppService-MovieId");
                getTrailersFromAPI (movieId);
                getReviewsFromAPI(movieId);
                getMoviesWithDetailFromAPI(movieId);
                break;
            default:
                break;
        }

    }

    private void getOtherDataFromAPI(String serviceAction){

        Uri moviesUri = MoviesContract.MoviesEntry.CONTENT_URI;
        Cursor mCursor = getContentResolver().query(
                moviesUri,
                new String[]{MoviesContract.MoviesEntry._ID},
                null,
                null,
                null);

        String movieId;
        mCursor.moveToFirst();
        while(mCursor.isAfterLast() == false){
            movieId = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry._ID));
            switch(serviceAction){
                case "TrailersSync":
                    getTrailersFromAPI(movieId);
                    break;
                case "ReviewsSync":
                    getReviewsFromAPI(movieId);
                    break;
                case "MovieAddlDataSync":
                    getMoviesWithDetailFromAPI(movieId);
                    break;
                case "OtherDataSync":
                    getTrailersFromAPI (movieId);
                    getReviewsFromAPI(movieId);
                    getMoviesWithDetailFromAPI(movieId);
                    break;
            }
            mCursor.moveToNext();
        }
    }
    private void getMoviesBySortFromAPI(String sortType){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesBySortJsonStr = null;
        String sort_by = new String("default");

        switch(sortType){
            case "high_rating":
                sort_by = "vote_average.desc";
                break;
            case "most_popular":
                sort_by = "popularity.desc";
                break;
            case "most_rating":
                sort_by = "vote_count.desc";
                break;
            default:
                sortType = "most_popular";
                sort_by = "popularity.desc";
                break;
        }

        try {
            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
            final String SORT_PARAM = "sort_by";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, getString(R.string.api_key))
                    .appendQueryParameter(SORT_PARAM, sort_by)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }
            moviesBySortJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movies string: " + moviesBySortJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            try {
                Log.v(LOG_TAG, "ErrorStream " + urlConnection.getErrorStream().toString());
            }catch (NullPointerException f){
                Log.e(LOG_TAG, "Error ", f);
            }
            return;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getMoviesBySortDataFromJson(moviesBySortJsonStr, sortType);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getMoviesWithDetailFromAPI(String mId){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesWithDetailJsonStr = null;

        try {
            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(mId)
                    .appendQueryParameter(API_KEY, getString(R.string.api_key))
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }
            moviesWithDetailJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movies Detail string: " + moviesWithDetailJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            try{
                Log.v(LOG_TAG, "ErrorStream " + urlConnection.getErrorStream().toString());
            }catch (NullPointerException f){
                Log.e(LOG_TAG, "Error ", f);
            }
            return;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getMoviesWithDetailDataFromJson(moviesWithDetailJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


    private void getTrailersFromAPI(String mId){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailersJsonStr = null;

        try {
            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String PATH_MOVIE_TRAILERS = "videos";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(mId)
                    .appendPath(PATH_MOVIE_TRAILERS)
                    .appendQueryParameter(API_KEY, getString(R.string.api_key))
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }
            trailersJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Trailers string: " + trailersJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            try{
                Log.v(LOG_TAG, "ErrorStream " + urlConnection.getErrorStream().toString());
            }catch (NullPointerException f){
                Log.e(LOG_TAG, "Error ", f);
            }
            return;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getTrailersDataFromJson(trailersJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getReviewsFromAPI(String mId){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String reviewsJsonStr = null;

        try {
            final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String PATH_MOVIE_REVIEWS = "reviews";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(mId)
                    .appendPath(PATH_MOVIE_REVIEWS)
                    .appendQueryParameter(API_KEY, getString(R.string.api_key))
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }
            reviewsJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Reviews string: " + reviewsJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            try {
                Log.v(LOG_TAG, "ErrorStream " + urlConnection.getErrorStream().toString());
            }catch (NullPointerException f){
                Log.e(LOG_TAG, "Error ", f);
            }
            return;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getReviewsDataFromJson(reviewsJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getMoviesBySortDataFromJson(String moviesJsonStr, String sortType)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray movieArray = moviesJson.getJSONArray("results");

        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
        Vector<ContentValues> cVVector1 = new Vector<ContentValues>(movieArray.length());

        for(int i = 0; i < movieArray.length(); i++) {

            JSONObject currMovie = movieArray.getJSONObject(i);

            String mId = currMovie.getString("id");
            String mOriginalTitle = currMovie.getString("original_title");
            String mReleaseDate = currMovie.getString("release_date");
            String mVoteAverage = currMovie.getString("vote_average");
            String mCast = null;
            String mOverview = currMovie.getString("overview");
            String mPosterPath = currMovie.getString("poster_path");
            String mDuration = null;

            String sId = mId;
            String sSortType = sortType;
            String sSortOrder = new String("1");

            ContentValues moviesValues = new ContentValues();
            ContentValues sortingValues = new ContentValues();

            moviesValues.put(MoviesContract.MoviesEntry._ID, mId);
            moviesValues.put(MoviesContract.MoviesEntry.COLUMN_NAME, mOriginalTitle);
            moviesValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DT, mReleaseDate);
            moviesValues.put(MoviesContract.MoviesEntry.COLUMN_RATING, mVoteAverage);
            moviesValues.put(MoviesContract.MoviesEntry.COLUMN_CAST, mCast);
            moviesValues.put(MoviesContract.MoviesEntry.COLUMN_PLOT, mOverview);
            moviesValues.put(MoviesContract.MoviesEntry.COLUMN_THUMB_IMG_URL, mPosterPath);
            moviesValues.put(MoviesContract.MoviesEntry.COLUMN_FULL_IMG_URL, mPosterPath);
            moviesValues.put(MoviesContract.MoviesEntry.COLUMN_DURATION, mDuration);

            sortingValues.put(MoviesContract.SortingEntry.COLUMN_MOVIE_ID, sId);
            sortingValues.put(MoviesContract.SortingEntry.COLUMN_SORT_TYPE, sSortType);
            sortingValues.put(MoviesContract.SortingEntry.COLUMN_SORT_ORDER, sSortOrder);

            cVVector.add(moviesValues);
            cVVector1.add(sortingValues);

            Log.v(LOG_TAG, "Movies data: " + new ArrayList<String>(Arrays.asList(mId, mOriginalTitle, mVoteAverage, mOverview, mPosterPath, mReleaseDate, sSortType, sSortOrder)));

        }

        int inserted = 0;
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            this.getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);
        }

        int inserted1 = 0;
        if ( cVVector1.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector1.size()];
            cVVector1.toArray(cvArray);
            this.getContentResolver().bulkInsert(MoviesContract.SortingEntry.CONTENT_URI, cvArray);
        }

        Log.v(LOG_TAG, "MoveVApp - Movies Service Complete. " + cVVector.size() + "," + cVVector1.size() + " Inserted");

    }

    private void getMoviesWithDetailDataFromJson(String moviesWithDetailJsonStr)
            throws JSONException {

        JSONObject currMovie = new JSONObject(moviesWithDetailJsonStr);

        String mId = currMovie.getString("id");
        String mDuration = currMovie.getString("runtime");

        ContentValues moviesValues = new ContentValues();
        String updateMoviesSelection = new String("_ID = ?");
        String[] updateMoviesSelectionArgs = null;

        moviesValues.put(MoviesContract.MoviesEntry._ID, mId);
        moviesValues.put(MoviesContract.MoviesEntry.COLUMN_DURATION, mDuration);

        updateMoviesSelectionArgs = new String[]{mId};

        Log.v(LOG_TAG, "Movies details data: " + new ArrayList<String>(Arrays.asList(mId, mDuration)));

        this.getContentResolver().update(MoviesContract.MoviesEntry.CONTENT_URI, moviesValues, updateMoviesSelection, updateMoviesSelectionArgs);
    }

    private void getTrailersDataFromJson(String trailersJsonStr)
            throws JSONException {

        JSONObject trailersJson = new JSONObject(trailersJsonStr);
        JSONArray trailersArray = trailersJson.getJSONArray("results");

        String mId = trailersJson.getString("id");
        Vector<ContentValues> cVVector = new Vector<ContentValues>(trailersArray.length());

        for(int i = 0; i < trailersArray.length(); i++) {

            JSONObject currTrailer = trailersArray.getJSONObject(i);

            String tType = currTrailer.getString("type");
            String tSource = currTrailer.getString("site");
            String tName = currTrailer.getString("name");
            String tURL = currTrailer.getString("key");

            ContentValues trailersValues = new ContentValues();

            trailersValues.put(MoviesContract.TrailersEntry.COLUMN_MOVIE_ID, mId);
            trailersValues.put(MoviesContract.TrailersEntry.COLUMN_TYPE, tType);
            trailersValues.put(MoviesContract.TrailersEntry.COLUMN_SOURCE, tSource);
            trailersValues.put(MoviesContract.TrailersEntry.COLUMN_NAME, tName);
            trailersValues.put(MoviesContract.TrailersEntry.COLUMN_URL, tURL);

            cVVector.add(trailersValues);

            Log.v(LOG_TAG, "Trailers data: " + new ArrayList<String>(Arrays.asList(mId, tType, tSource, tName, tURL)));

        }

        int inserted = 0;
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            this.getContentResolver().bulkInsert(MoviesContract.TrailersEntry.CONTENT_URI, cvArray);
        }

        Log.v(LOG_TAG, "MoveVApp - Trailers for movie : " + cVVector.size() + " Inserted");

    }

    private void getReviewsDataFromJson(String reviewsJsonStr)
            throws JSONException {

        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
        JSONArray reviewsArray = reviewsJson.getJSONArray("results");

        String mId = reviewsJson.getString("id");
        Vector<ContentValues> cVVector = new Vector<ContentValues>(reviewsArray.length());

        for(int i = 0; i < reviewsArray.length(); i++) {

            JSONObject currReview = reviewsArray.getJSONObject(i);

            String tAuthor = currReview.getString("author");
            String tDescription = currReview.getString("content");
            String tURL = currReview.getString("url");

            ContentValues reviewsValues = new ContentValues();

            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID, mId);
            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_AUTHOR, tAuthor);
            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_DESCRIPTION, tDescription);
            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_URL, tURL);

            cVVector.add(reviewsValues);

            Log.v(LOG_TAG, "Reviews data: " + new ArrayList<String>(Arrays.asList(mId, tAuthor, tDescription, tURL)));

        }

        int inserted = 0;
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            this.getContentResolver().bulkInsert(MoviesContract.ReviewsEntry.CONTENT_URI, cvArray);
        }

        Log.v(LOG_TAG, "MoveVApp - Reviews for movie : " + cVVector.size() + " Inserted");

    }
}
