package com.simplesolutions2003.happybabycare.data;

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
 * Created by SuriyaKumar on 8/16/2016.
 */
public class AppProvider extends ContentProvider {
    private final String LOG_TAG = AppProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private AppDBHelper mOpenHelper;

    private static final SQLiteQueryBuilder sSettingsQueryBuilder;
    private static final SQLiteQueryBuilder sUserQueryBuilder;
    private static final SQLiteQueryBuilder sUserPrefQueryBuilder;
    private static final SQLiteQueryBuilder sSyncLogQueryBuilder;
    private static final SQLiteQueryBuilder sBabyQueryBuilder;
    private static final SQLiteQueryBuilder sFeedingQueryBuilder;
    private static final SQLiteQueryBuilder sDiaperQueryBuilder;
    private static final SQLiteQueryBuilder sSleepingQueryBuilder;
    private static final SQLiteQueryBuilder sHealthQueryBuilder;
    private static final SQLiteQueryBuilder sArticleQueryBuilder;
    private static final SQLiteQueryBuilder sArticleDetailQueryBuilder;
    private static final SQLiteQueryBuilder sMediaQueryBuilder;

    private static final SQLiteQueryBuilder sActivitiesQueryBuilder;
    private static final SQLiteQueryBuilder sArticleWithDetailQueryBuilder;

    static final int SETTINGS = 100;
    static final int USER = 200;
    static final int USER_PREF = 201;
    static final int SYNC_LOG = 202;
    static final int BABY = 300;
    static final int ACTIVITIES = 301;
    static final int FEEDING = 302;
    static final int DIAPER = 303;
    static final int SLEEPING = 304;
    static final int HEALTH = 305;
    static final int ARTICLE = 400;
    static final int ARTICLE_DETAIL = 401;
    static final int ARTICLE_WITH_DETAIL = 402;
    static final int STORIES = 403;
    static final int RHYMES = 405;
    static final int MEDIA = 500;

    static{
        sSettingsQueryBuilder = new SQLiteQueryBuilder();

        sSettingsQueryBuilder.setTables(
                AppContract.SettingsEntry.TABLE_NAME);
    }

    static{
        sUserQueryBuilder = new SQLiteQueryBuilder();

        sUserQueryBuilder.setTables(
                AppContract.UserEntry.TABLE_NAME);
    }

    static{
        sUserPrefQueryBuilder = new SQLiteQueryBuilder();

        sUserPrefQueryBuilder.setTables(
                AppContract.UserPreferenceEntry.TABLE_NAME);
    }

    static{
        sSyncLogQueryBuilder = new SQLiteQueryBuilder();

        sSyncLogQueryBuilder.setTables(
                AppContract.SyncLogEntry.TABLE_NAME);
    }

    static{
        sBabyQueryBuilder = new SQLiteQueryBuilder();

        sBabyQueryBuilder.setTables(
                AppContract.BabyEntry.TABLE_NAME);
    }

    static{
        sFeedingQueryBuilder = new SQLiteQueryBuilder();

        sFeedingQueryBuilder.setTables(
                AppContract.FeedingEntry.TABLE_NAME);
    }

    static{
        sDiaperQueryBuilder = new SQLiteQueryBuilder();

        sDiaperQueryBuilder.setTables(
                AppContract.DiaperEntry.TABLE_NAME);
    }

    static{
        sSleepingQueryBuilder = new SQLiteQueryBuilder();

        sSleepingQueryBuilder.setTables(
                AppContract.SleepingEntry.TABLE_NAME);
    }

    static{
        sHealthQueryBuilder = new SQLiteQueryBuilder();

        sHealthQueryBuilder.setTables(
                AppContract.HealthEntry.TABLE_NAME);
    }

    static{
        sArticleQueryBuilder = new SQLiteQueryBuilder();

        sArticleQueryBuilder.setTables(
                AppContract.ArticleEntry.TABLE_NAME);
    }

    static{
        sArticleDetailQueryBuilder = new SQLiteQueryBuilder();

        sArticleDetailQueryBuilder.setTables(
                AppContract.ArticleDetailEntry.TABLE_NAME);
    }

    static{
        sMediaQueryBuilder = new SQLiteQueryBuilder();

        sMediaQueryBuilder.setTables(
                AppContract.MediaEntry.TABLE_NAME);
    }

    static{
        sActivitiesQueryBuilder = new SQLiteQueryBuilder();

        sActivitiesQueryBuilder.setTables(
                AppContract.BabyEntry.TABLE_NAME +
                        " LEFT OUTER JOIN " + AppContract.DiaperEntry.TABLE_NAME +
                        " ON " + AppContract.UserEntry.TABLE_NAME +
                        "." + AppContract.UserEntry._ID +
                        " = " + AppContract.DiaperEntry.TABLE_NAME +
                        "." + AppContract.DiaperEntry.COLUMN_USER_ID +
                        " AND " + AppContract.BabyEntry.TABLE_NAME +
                        "." + AppContract.BabyEntry._ID +
                        " = " + AppContract.DiaperEntry.TABLE_NAME +
                        "." + AppContract.DiaperEntry.COLUMN_BABY_ID +
                        " LEFT OUTER JOIN " + AppContract.FeedingEntry.TABLE_NAME +
                        " ON " + AppContract.UserEntry.TABLE_NAME +
                        "." + AppContract.UserEntry._ID +
                        " = " + AppContract.FeedingEntry.TABLE_NAME +
                        "." + AppContract.FeedingEntry.COLUMN_USER_ID +
                        " AND " + AppContract.BabyEntry.TABLE_NAME +
                        "." + AppContract.BabyEntry._ID +
                        " = " + AppContract.FeedingEntry.TABLE_NAME +
                        "." + AppContract.FeedingEntry.COLUMN_BABY_ID +
                        " LEFT OUTER JOIN " + AppContract.SleepingEntry.TABLE_NAME +
                        " ON " + AppContract.UserEntry.TABLE_NAME +
                        "." + AppContract.UserEntry._ID +
                        " = " + AppContract.SleepingEntry.TABLE_NAME +
                        "." + AppContract.SleepingEntry.COLUMN_USER_ID +
                        " AND " + AppContract.BabyEntry.TABLE_NAME +
                        "." + AppContract.BabyEntry._ID +
                        " = " + AppContract.SleepingEntry.TABLE_NAME +
                        "." + AppContract.SleepingEntry.COLUMN_BABY_ID +
                        " LEFT OUTER JOIN " + AppContract.HealthEntry.TABLE_NAME +
                        " ON " + AppContract.UserEntry.TABLE_NAME +
                        "." + AppContract.UserEntry._ID +
                        " = " + AppContract.HealthEntry.TABLE_NAME +
                        "." + AppContract.HealthEntry.COLUMN_USER_ID +
                        " AND " + AppContract.BabyEntry.TABLE_NAME +
                        "." + AppContract.BabyEntry._ID +
                        " = " + AppContract.HealthEntry.TABLE_NAME +
                        "." + AppContract.HealthEntry.COLUMN_BABY_ID);
    }

    static{
        sArticleWithDetailQueryBuilder = new SQLiteQueryBuilder();

        sActivitiesQueryBuilder.setTables(
                AppContract.ArticleEntry.TABLE_NAME +
                        " INNER JOIN " + AppContract.ArticleDetailEntry.TABLE_NAME +
                        " ON " + AppContract.ArticleEntry.TABLE_NAME +
                        "." + AppContract.ArticleEntry._ID +
                        " = " + AppContract.ArticleDetailEntry.TABLE_NAME +
                        "." + AppContract.ArticleDetailEntry.COLUMN_ARTICLE_ID);
    }

    private static final String sSettingsSelection =
            AppContract.SettingsEntry.TABLE_NAME +
                    "." + AppContract.SettingsEntry.COLUMN_VERSION + " = ? ";

    private static final String sUserSelection =
            AppContract.UserEntry.TABLE_NAME+
                    "." + AppContract.UserEntry._ID + " = ? ";

    private static final String sUserPrefByUserIdSelection =
            AppContract.UserPreferenceEntry.TABLE_NAME +
                    "." + AppContract.UserPreferenceEntry.COLUMN_USER_ID + " = ? ";

    private static final String sSyncLogByUserIdSelection =
            AppContract.SyncLogEntry.TABLE_NAME +
                    "." + AppContract.SyncLogEntry.COLUMN_USER_ID + " = ? ";

    private static final String sBabyByUserIdSelection =
            AppContract.BabyEntry.TABLE_NAME +
                    "." + AppContract.BabyEntry.COLUMN_USER_ID + " = ? ";

    private static final String sFeedingByBabyIdSelection =
            AppContract.FeedingEntry.TABLE_NAME +
                    "." + AppContract.FeedingEntry.COLUMN_USER_ID + " = ? " +
            " AND " + AppContract.FeedingEntry.TABLE_NAME +
                    "." + AppContract.FeedingEntry.COLUMN_BABY_ID + " = ? ";

    private static final String sDiaperByBabyIdSelection =
            AppContract.DiaperEntry.TABLE_NAME +
                    "." + AppContract.DiaperEntry.COLUMN_USER_ID + " = ? " +
                    " AND " + AppContract.DiaperEntry.TABLE_NAME +
                    "." + AppContract.DiaperEntry.COLUMN_BABY_ID + " = ? ";

    private static final String sSleepingByBabyIdSelection =
            AppContract.SleepingEntry.TABLE_NAME +
                    "." + AppContract.SleepingEntry.COLUMN_USER_ID + " = ? " +
                    " AND " + AppContract.SleepingEntry.TABLE_NAME +
                    "." + AppContract.SleepingEntry.COLUMN_BABY_ID + " = ? ";

    private static final String sHealthByBabyIdSelection =
            AppContract.HealthEntry.TABLE_NAME +
                    "." + AppContract.HealthEntry.COLUMN_USER_ID + " = ? " +
                    " AND " + AppContract.HealthEntry.TABLE_NAME +
                    "." + AppContract.HealthEntry.COLUMN_BABY_ID + " = ? ";

    private static final String sArticleSelection =
            AppContract.ArticleEntry.TABLE_NAME +
                    "." + AppContract.ArticleEntry._ID + " = ? ";

    private static final String sArticleDetailByArticleIdSelection =
            AppContract.ArticleDetailEntry.TABLE_NAME +
                    "." + AppContract.ArticleDetailEntry._ID + " = ? ";

    private static final String sMediaSelection =
            AppContract.MediaEntry.TABLE_NAME +
                    "." + AppContract.MediaEntry._ID + " = ? ";

    private static final String sActivitiesByUserIdBabyIdSelection =
            AppContract.UserEntry.TABLE_NAME +
                    "." + AppContract.UserEntry._ID + " = ? " +
            " AND " + AppContract.BabyEntry.TABLE_NAME +
                    "." + AppContract.BabyEntry._ID + " = ? ";

    private static final String sArticleByTypeSelection =
            AppContract.ArticleEntry.TABLE_NAME +
                    "." + AppContract.ArticleEntry.COLUMN_TYPE + " = ? ";

    private Cursor getActivitiesByBabyId(Uri uri, String[] projection, String sortOrder) {

        Log.v(LOG_TAG, "getActivitiesByBabyId uri - " + uri);
        long userId = AppContract.HealthEntry.getUserIdFromUri(uri);
        long babyId = AppContract.HealthEntry.getBabyIdFromUri(uri);
        Log.v(LOG_TAG, "getActivitiesByBabyId userId - " + userId);
        Log.v(LOG_TAG, "getActivitiesByBabyId babyId - " + babyId);

        return sActivitiesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sActivitiesByUserIdBabyIdSelection,
                new String[]{Long.toString(userId),Long.toString(babyId)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getArticleByType(Uri uri, String[] projection, String sortOrder) {

        Log.v(LOG_TAG, "getArticleByType uri - " + uri);
        String articleType = AppContract.ArticleEntry.getArticleTypeFromUri(uri);
        Log.v(LOG_TAG, "getArticleByType articleType - " + articleType);

        return sArticleQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sArticleByTypeSelection,
                new String[]{articleType},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getArticleWithDetail(Uri uri, String[] projection, String sortOrder) {

        Log.v(LOG_TAG, "getArticleWithDetail uri - " + uri);
        long articleId = AppContract.ArticleDetailEntry.getArticleIdFromUri(uri);
        Log.v(LOG_TAG, "getArticleWithDetail articleId - " + articleId);

        return sArticleQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sArticleSelection,
                new String[]{Long.toString(articleId)},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AppContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, AppContract.PATH_SETTINGS, SETTINGS);
        matcher.addURI(authority, AppContract.PATH_USER, USER);
        matcher.addURI(authority, AppContract.PATH_USER_PREF, USER_PREF);
        matcher.addURI(authority, AppContract.PATH_SYNC_LOG, SYNC_LOG);
        matcher.addURI(authority, AppContract.PATH_BABY, BABY);
        matcher.addURI(authority, AppContract.PATH_FEEDING, FEEDING);
        matcher.addURI(authority, AppContract.PATH_DIAPER, DIAPER);
        matcher.addURI(authority, AppContract.PATH_SLEEPING, SLEEPING);
        matcher.addURI(authority, AppContract.PATH_HEALTH, HEALTH);
        matcher.addURI(authority, AppContract.PATH_ARTICLE, ARTICLE);
        matcher.addURI(authority, AppContract.PATH_ARTICLE_DETAIL, ARTICLE_DETAIL);
        matcher.addURI(authority, AppContract.PATH_MEDIA, MEDIA);
        matcher.addURI(authority, AppContract.PATH_BABY + "/ACTIVITIES/", ACTIVITIES);
        matcher.addURI(authority, AppContract.PATH_ARTICLE + "/ARTICLE/DETAIL/", ARTICLE_WITH_DETAIL);
        matcher.addURI(authority, AppContract.PATH_ARTICLE + "/STORIES/", STORIES);
        matcher.addURI(authority, AppContract.PATH_ARTICLE + "/RHYMES/", RHYMES);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new AppDBHelper(getContext());
        return true;
    }

    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SETTINGS:
                return AppContract.SettingsEntry.CONTENT_TYPE;
            case USER:
                return AppContract.UserEntry.CONTENT_TYPE;
            case USER_PREF:
                return AppContract.UserPreferenceEntry.CONTENT_TYPE;
            case SYNC_LOG:
                return AppContract.SyncLogEntry.CONTENT_TYPE;
            case BABY:
                return AppContract.BabyEntry.CONTENT_TYPE;
            case FEEDING:
                return AppContract.FeedingEntry.CONTENT_TYPE;
            case DIAPER:
                return AppContract.DiaperEntry.CONTENT_TYPE;
            case SLEEPING:
                return AppContract.SleepingEntry.CONTENT_TYPE;
            case HEALTH:
                return AppContract.HealthEntry.CONTENT_TYPE;
            case ARTICLE:
                return AppContract.ArticleEntry.CONTENT_TYPE;
            case ARTICLE_DETAIL:
                return AppContract.ArticleDetailEntry.CONTENT_TYPE;
            case MEDIA:
                return AppContract.MediaEntry.CONTENT_TYPE;
            case ACTIVITIES:
                return AppContract.HealthEntry.CONTENT_ITEM_TYPE;
            case ARTICLE_WITH_DETAIL:
                return AppContract.ArticleDetailEntry.CONTENT_ITEM_TYPE;
            case STORIES:
                return AppContract.ArticleDetailEntry.CONTENT_ITEM_TYPE;
            case RHYMES:
                return AppContract.ArticleDetailEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        String tableName = getTableById(sUriMatcher.match(uri));
        if(!tableName.equals("Others")){
            retCursor = mOpenHelper.getReadableDatabase().query(
                    getTableById(sUriMatcher.match(uri)),
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
        }else {
            switch (sUriMatcher.match(uri)) {

                case ACTIVITIES: {
                    retCursor = getActivitiesByBabyId(uri, projection, sortOrder);
                    break;
                }

                case ARTICLE_WITH_DETAIL: {
                    retCursor = getArticleWithDetail(uri, projection, sortOrder);
                    break;
                }

                case STORIES: {
                    retCursor = getStories(uri, projection, sortOrder);
                    break;
                }

                case RHYMES: {
                    retCursor = getRhymes(uri, projection, sortOrder);
                    break;
                }

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
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

    private String getTableById(int tableId){
        switch (tableId){
            case SETTINGS:
                return AppContract.SettingsEntry.TABLE_NAME;
            case USER:
                return AppContract.UserEntry.TABLE_NAME;
            case USER_PREF:
                return AppContract.UserPreferenceEntry.TABLE_NAME;
            case SYNC_LOG:
                return AppContract.SyncLogEntry.TABLE_NAME;
            case BABY:
                return AppContract.BabyEntry.TABLE_NAME;
            case FEEDING:
                return AppContract.FeedingEntry.TABLE_NAME;
            case DIAPER:
                return AppContract.DiaperEntry.TABLE_NAME;
            case SLEEPING:
                return AppContract.SleepingEntry.TABLE_NAME;
            case HEALTH:
                return AppContract.HealthEntry.TABLE_NAME;
            case ARTICLE:
                return AppContract.ArticleEntry.TABLE_NAME;
            case ARTICLE_DETAIL:
                return AppContract.ArticleDetailEntry.TABLE_NAME;
            case MEDIA:
                return AppContract.MediaEntry.TABLE_NAME;
            default:
                return (new String("Others"));
        }
    }

}
