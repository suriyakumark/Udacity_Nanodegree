package com.simplesolutions2003.movevapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.simplesolutions2003.movevapp.data.MoviesContract.MoviesEntry;
import com.simplesolutions2003.movevapp.data.MoviesContract.TrailersEntry;
import com.simplesolutions2003.movevapp.data.MoviesContract.ReviewsEntry;
import com.simplesolutions2003.movevapp.data.MoviesContract.SortingEntry;

/**
 * Created by Suriya on 11/28/2015.
 */
public class MoviesDBHelper extends SQLiteOpenHelper  {
    private final String LOG_TAG = MoviesDBHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movevapp.db";
    static final String SORT_TYPE_FAVORITE = "favorite";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                MoviesEntry.COLUMN_RELEASE_DT + " TEXT, " +
                MoviesEntry.COLUMN_RATING + " TEXT, " +
                MoviesEntry.COLUMN_CAST + " TEXT, " +
                MoviesEntry.COLUMN_PLOT + " TEXT, " +
                MoviesEntry.COLUMN_THUMB_IMG_URL + " TEXT, " +
                MoviesEntry.COLUMN_FULL_IMG_URL + " TEXT, " +
                MoviesEntry.COLUMN_DURATION + " TEXT " +
                MoviesEntry.COLUMN_IMAGE_BLOB + " BLOB " +
                " );";

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + TrailersEntry.TABLE_NAME + " (" +
                TrailersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TrailersEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                TrailersEntry.COLUMN_TYPE + " TEXT, " +
                TrailersEntry.COLUMN_SOURCE + " TEXT, " +
                TrailersEntry.COLUMN_NAME + " TEXT, " +
                TrailersEntry.COLUMN_URL + " TEXT NOT NULL, " +

                // referencial integrity between movies and trailers table
                " FOREIGN KEY (" + TrailersEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + "), " +

                // avoid duplicate trailer entries
                " UNIQUE (" + TrailersEntry.COLUMN_MOVIE_ID + ", " +
                TrailersEntry.COLUMN_URL + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +
                ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ReviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewsEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ReviewsEntry.COLUMN_URL + " TEXT, " +

                // referencial integrity between movies and trailers table
                " FOREIGN KEY (" + ReviewsEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + "), " +

                // avoid duplicate trailer entries
                " UNIQUE (" + ReviewsEntry.COLUMN_MOVIE_ID + ", " +
                ReviewsEntry.COLUMN_DESCRIPTION + ", " +
                ReviewsEntry.COLUMN_AUTHOR + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_SORTING_TABLE = "CREATE TABLE " + SortingEntry.TABLE_NAME + " (" +
                SortingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SortingEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                SortingEntry.COLUMN_SORT_TYPE + " TEXT NOT NULL, " +
                SortingEntry.COLUMN_SORT_ORDER + " TEXT NOT NULL, " +

                // referencial integrity between movies and trailers table
                " FOREIGN KEY (" + SortingEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + "), " +

                // avoid duplicate trailer entries
                " UNIQUE (" + SortingEntry.COLUMN_MOVIE_ID  + ", " +
                SortingEntry.COLUMN_SORT_TYPE + ") ON CONFLICT REPLACE);";

        Log.v(LOG_TAG, "SQL_CREATE_MOVIES_TABLE" + SQL_CREATE_MOVIES_TABLE);
        Log.v(LOG_TAG, "SQL_CREATE_TRAILERS_TABLE" + SQL_CREATE_TRAILERS_TABLE);
        Log.v(LOG_TAG, "SQL_CREATE_REVIEWS_TABLE" + SQL_CREATE_REVIEWS_TABLE);
        Log.v(LOG_TAG, "SQL_CREATE_SORTING_TABLE" + SQL_CREATE_SORTING_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SORTING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // On upgrade, the tables can be altered to accommodate new columns
        // We don't want to delete the favorites

        /*
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SortingEntry.TABLE_NAME);
        */

        onCreate(sqLiteDatabase);
    }
}
