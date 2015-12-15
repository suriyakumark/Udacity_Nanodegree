package com.simplesolutions2003.movevapp.data;
import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Suriya on 11/28/2015.
 */
public class MoviesProvider extends ContentProvider {
    private final String LOG_TAG = MoviesProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mOpenHelper;

    private static final SQLiteQueryBuilder sMoviesWithDetailsQueryBuilder;
    private static final SQLiteQueryBuilder sMoviesBySortQueryBuilder;
    private static final SQLiteQueryBuilder sTrailersByMovieIdQueryBuilder;
    private static final SQLiteQueryBuilder sReviewsByMovieIdQueryBuilder;
    private static final SQLiteQueryBuilder sSortingByMovieIdQueryBuilder;

    static final int MOVIES = 100;
    static final int MOVIES_BY_SORT = 101;
    static final int MOVIES_WITH_DETAILS = 102;
    static final int REVIEWS = 200;
    static final int REVIEWS_BY_MOVIE = 201;
    static final int TRAILERS = 300;
    static final int TRAILERS_BY_MOVIE = 301;
    static final int SORTING = 400;
    static final int SORTING_BY_MOVIE = 401;

    static{
        sMoviesWithDetailsQueryBuilder = new SQLiteQueryBuilder();

        sMoviesWithDetailsQueryBuilder.setTables(
                MoviesContract.MoviesEntry.TABLE_NAME);
    }

    static{
        sTrailersByMovieIdQueryBuilder = new SQLiteQueryBuilder();

        sTrailersByMovieIdQueryBuilder.setTables(
                MoviesContract.TrailersEntry.TABLE_NAME);
    }

    static{
        sReviewsByMovieIdQueryBuilder = new SQLiteQueryBuilder();

        sReviewsByMovieIdQueryBuilder.setTables(
                MoviesContract.ReviewsEntry.TABLE_NAME);
    }


    static{
        sMoviesBySortQueryBuilder = new SQLiteQueryBuilder();

        sMoviesBySortQueryBuilder.setTables(
                MoviesContract.MoviesEntry.TABLE_NAME +
                        " LEFT OUTER JOIN " + MoviesContract.SortingEntry.TABLE_NAME +
                        " ON " + MoviesContract.MoviesEntry.TABLE_NAME +
                        "." + MoviesContract.MoviesEntry._ID +
                        " = " + MoviesContract.SortingEntry.TABLE_NAME +
                        "." + MoviesContract.SortingEntry.COLUMN_MOVIE_ID);
    }

    static{
        sSortingByMovieIdQueryBuilder = new SQLiteQueryBuilder();

        sSortingByMovieIdQueryBuilder.setTables(
                MoviesContract.SortingEntry.TABLE_NAME);
    }


    private static final String sMoviesWithDetailsSelection =
            MoviesContract.MoviesEntry.TABLE_NAME+
                    "." + MoviesContract.MoviesEntry._ID + " = ? ";

    private static final String sTrailersByMovieIdSelection =
            MoviesContract.TrailersEntry.TABLE_NAME+
                    "." + MoviesContract.TrailersEntry.COLUMN_MOVIE_ID + " = ? ";

    private static final String sReviewsByMovieIdSelection =
            MoviesContract.ReviewsEntry.TABLE_NAME+
                    "." + MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID + " = ? ";

    private static final String sSortingByMovieIdSelection =
            MoviesContract.SortingEntry.TABLE_NAME+
                    "." + MoviesContract.SortingEntry.COLUMN_MOVIE_ID + " = ? ";

    private static final String sMoviesBySortSelection =
            MoviesContract.SortingEntry.TABLE_NAME+
                    "." + MoviesContract.SortingEntry.COLUMN_SORT_TYPE + " = ? ";

    private Cursor getMoviesWithDetails(Uri uri, String[] projection, String sortOrder) {

        Log.v(LOG_TAG, "getMoviesWithDetails uri - " + uri);
        String movieId = MoviesContract.MoviesEntry.getMovieIdFromUri(uri);
        Log.v(LOG_TAG, "getMoviesWithDetails movieId - " + movieId);

        return sMoviesWithDetailsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMoviesWithDetailsSelection,
                new String[]{movieId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTrailersByMovieId(Uri uri, String[] projection, String sortOrder) {

        Log.v(LOG_TAG, "getTrailersByMovie uri - " + uri);
        String movieId = MoviesContract.TrailersEntry.getMovieIdFromUri(uri);
        Log.v(LOG_TAG, "getTrailersByMovie movieId - " + movieId);

        return sTrailersByMovieIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sTrailersByMovieIdSelection,
                new String[]{movieId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviewsByMovieId(Uri uri, String[] projection, String sortOrder) {

        Log.v(LOG_TAG, "getReviewsByMovie uri - " + uri);
        String movieId = MoviesContract.ReviewsEntry.getMovieIdFromUri(uri);
        Log.v(LOG_TAG, "getReviewsByMovie movieId - " + movieId);

        return sReviewsByMovieIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sReviewsByMovieIdSelection,
                new String[]{movieId},
                null,
                null,
                sortOrder
        );
    }


    private Cursor getMoviesBySort(Uri uri, String[] projection, String sortOrder) {

        Log.v(LOG_TAG, "getMoviesBySort uri - " + uri);
        String sortType = MoviesContract.SortingEntry.getSortTypeFromUri(uri);
        Log.v(LOG_TAG, "getMoviesBySort sortType - " + sortType);

        return sMoviesBySortQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMoviesBySortSelection,
                new String[]{sortType},
                null,
                null,
                sortOrder
        );
    }


    private Cursor getSortingByMovieId(Uri uri, String[] projection, String sortOrder) {

        Log.v(LOG_TAG, "getSortingByMovie uri - " + uri);
        String movieId = MoviesContract.SortingEntry.getMovieIdFromUri(uri);
        Log.v(LOG_TAG, "getSortingByMovie movieId - " + movieId);

        return sSortingByMovieIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sSortingByMovieIdSelection,
                new String[]{movieId},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/DETAILS/*", MOVIES_WITH_DETAILS);
        matcher.addURI(authority, MoviesContract.PATH_TRAILERS, TRAILERS);
        matcher.addURI(authority, MoviesContract.PATH_TRAILERS + "/MOVIE/*", TRAILERS_BY_MOVIE);
        matcher.addURI(authority, MoviesContract.PATH_REVIEWS, REVIEWS);
        matcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/MOVIE/*", REVIEWS_BY_MOVIE);
        matcher.addURI(authority, MoviesContract.PATH_SORTING, SORTING);
        matcher.addURI(authority, MoviesContract.PATH_SORTING + "/SORT/*", MOVIES_BY_SORT);
        matcher.addURI(authority, MoviesContract.PATH_SORTING + "/MOVIE/*", SORTING_BY_MOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());
        return true;
    }

    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            case MOVIES_WITH_DETAILS:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case TRAILERS:
                return MoviesContract.TrailersEntry.CONTENT_TYPE;
            case TRAILERS_BY_MOVIE:
                return MoviesContract.TrailersEntry.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return MoviesContract.ReviewsEntry.CONTENT_TYPE;
            case REVIEWS_BY_MOVIE:
                return MoviesContract.ReviewsEntry.CONTENT_ITEM_TYPE;
            case SORTING:
                return MoviesContract.SortingEntry.CONTENT_TYPE;
            case MOVIES_BY_SORT:
                return MoviesContract.SortingEntry.CONTENT_ITEM_TYPE;
            case SORTING_BY_MOVIE:
                return MoviesContract.SortingEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOVIES_WITH_DETAILS: {
                retCursor = getMoviesWithDetails(uri, projection, sortOrder);
                break;
            }

            case TRAILERS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.TrailersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case TRAILERS_BY_MOVIE:{
                retCursor = getTrailersByMovieId(uri, projection, sortOrder);
                break;
            }
            case REVIEWS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case REVIEWS_BY_MOVIE:{
                retCursor = getReviewsByMovieId(uri, projection, sortOrder);
                break;
            }

            case SORTING: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.SortingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOVIES_BY_SORT:
            {
                retCursor = getMoviesBySort(uri, projection, sortOrder);
                break;
            }

            case SORTING_BY_MOVIE:
            {
                retCursor = getSortingByMovieId(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.MoviesEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILERS: {
                long _id = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.TrailersEntry.buildTrailersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS: {
                long _id = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.ReviewsEntry.buildReviewsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SORTING: {
                long _id = db.insert(MoviesContract.SortingEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.SortingEntry.buildSortingUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(
                        MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILERS:
                rowsDeleted = db.delete(
                        MoviesContract.TrailersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEWS:
                rowsDeleted = db.delete(
                        MoviesContract.ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SORTING:
                rowsDeleted = db.delete(
                        MoviesContract.SortingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TRAILERS:
                rowsUpdated = db.update(MoviesContract.TrailersEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REVIEWS:
                rowsUpdated = db.update(MoviesContract.SortingEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case SORTING:
                rowsUpdated = db.update(MoviesContract.SortingEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case REVIEWS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TRAILERS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case SORTING:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.SortingEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
