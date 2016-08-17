package com.simplesolutions2003.happybabycare.data;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
/**
 * Created by SuriyaKumar on 8/16/2016.
 */
public class AppContract {
    private static final String LOG_TAG = AppContract.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "com.simplesolutions2003.happybabycare";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SETTINGS = "settings";
    public static final String PATH_USER = "user";
    public static final String PATH_USER_PREF = "user_preference";
    public static final String PATH_SYNC_LOG = "sync_log";
    public static final String PATH_BABY = "baby";
    public static final String PATH_FEEDING = "feeding";
    public static final String PATH_DIAPER = "diaper";
    public static final String PATH_SLEEPING = "sleeping";
    public static final String PATH_HEALTH = "health";
    public static final String PATH_ARTICLE = "article";
    public static final String PATH_ARTICLE_DETAIL = "article_detail";
    public static final String PATH_MEDIA = "media";

    public static final class SettingsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SETTINGS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SETTINGS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SETTINGS;

        public static final String TABLE_NAME = "settings";

        public static final String COLUMN_VERSION = "version";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_VALUE = "value";
        public static final String COLUMN_ACTIVE = "active";
        public static final String COLUMN_UPDATED_TS = "updated_timestamp";

        public static Uri buildSettingsUri(long version) {
            return ContentUris.withAppendedId(CONTENT_URI, version);
        }

        public static long getVersionFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

    }

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "user";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_ACTIVE = "active";
        public static final String COLUMN_LAST_SYNC_TS = "last_sync_timestamp";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getUserIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

    }

    public static final class UserPreferenceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_PREF).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_PREF;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER_PREF;

        public static final String TABLE_NAME = "user_preference";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_VALUE = "value";

        public static Uri buildUserPreferenceUri(long user_id) {
            return ContentUris.withAppendedId(CONTENT_URI, user_id);
        }

        public static long getUserIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

    }

    public static final class SyncLogEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SYNC_LOG).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYNC_LOG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYNC_LOG;

        public static final String TABLE_NAME = "sync_log";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_SYNC_TS = "sync_timestamp";
        public static final String COLUMN_REQUEST_TYPE = "request_type";
        public static final String COLUMN_REQUEST_IN = "request_in";
        public static final String COLUMN_REQUEST_OUT = "request_out";

        public static Uri buildSyncLogUri(long user_id) {
            return ContentUris.withAppendedId(CONTENT_URI, user_id);
        }

        public static long getUserIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }
    }

    public static final class BabyEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BABY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BABY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BABY;

        public static final String TABLE_NAME = "baby";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_BIRTH_DATE = "birth_date";
        public static final String COLUMN_DUE_DATE = "due_date";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_ACTIVE = "active";

        public static Uri buildBabyUri(long user_id) {
            return ContentUris.withAppendedId(CONTENT_URI, user_id);
        }

        public static long getUserIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

    }

    public static final class FeedingEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FEEDING).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FEEDING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FEEDING;

        public static final String TABLE_NAME = "feeding";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_BABY_ID = "baby_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_NOTES = "notes";

        public static Uri buildFeedingUri(long user_id, long baby_id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(user_id)).appendPath(Long.toString(baby_id)).build();
        }

        public static long getBabyIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

    }

    public static final class DiaperEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DIAPER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DIAPER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DIAPER;

        public static final String TABLE_NAME = "diaper";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_BABY_ID = "baby_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_CREAM = "cream";
        public static final String COLUMN_NOTES = "notes";

        public static Uri buildDiaperUri(long user_id, long baby_id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(user_id)).appendPath(Long.toString(baby_id)).build();
        }

        public static long getBabyIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }
    }

    public static final class SleepingEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SLEEPING).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SLEEPING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SLEEPING;

        public static final String TABLE_NAME = "sleeping";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_BABY_ID = "baby_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_WHERE = "where";
        public static final String COLUMN_NOTES = "notes";

        public static Uri buildSleepingUri(long user_id, long baby_id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(user_id)).appendPath(Long.toString(baby_id)).build();
        }

        public static long getBabyIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }
    }

    public static final class HealthEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HEALTH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEALTH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HEALTH;

        public static final String TABLE_NAME = "health";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_BABY_ID = "baby_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_VALUE = "value";
        public static final String COLUMN_NOTES = "notes";

        public static Uri buildHealthUri(long user_id, long baby_id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(user_id)).appendPath(Long.toString(baby_id)).build();
        }

        public static long getBabyIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }
    }

    public static final class ArticleEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE;

        public static final String TABLE_NAME = "article";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LAST_UPDATED_TS = "last_updated_timestamp";

        public static Uri buildArticleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getArticleIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }
    }

    public static final class ArticleDetailEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLE_DETAIL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE_DETAIL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE_DETAIL;

        public static final String TABLE_NAME = "article_detail";

        public static final String COLUMN_ARTICLE_ID = "article_id";
        public static final String COLUMN_SEQUENCE = "sequence";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_CONTENT = "content";

        public static Uri buildArticleDetailUri(long article_id) {
            return ContentUris.withAppendedId(CONTENT_URI, article_id);
        }

        public static long getArticleIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }
    }

    public static final class MediaEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDIA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDIA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDIA;

        public static final String TABLE_NAME = "media";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_PATH = "path";

        public static Uri buildMediaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getMediaIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }
    }

}
