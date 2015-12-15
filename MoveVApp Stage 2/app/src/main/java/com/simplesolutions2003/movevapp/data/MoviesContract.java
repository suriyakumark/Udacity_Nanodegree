package com.simplesolutions2003.movevapp.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Suriya on 11/27/2015.
 */
public class MoviesContract {

    private static final String LOG_TAG = MoviesContract.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "com.simplesolutions2003.movevapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_SORTING = "sorting";

    // Store all movie details
    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_NAME = "movie_name";
        public static final String COLUMN_RELEASE_DT = "release_dt";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_CAST = "cast";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_THUMB_IMG_URL = "thumb_img_url";
        public static final String COLUMN_FULL_IMG_URL = "full_img_url";
        public static final String COLUMN_DURATION = "duration";

        //future use - download images and save as blob for offline use
        public static final String COLUMN_IMAGE_BLOB = "image_blob";

        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieWithDetailUri(String movie_id) {
            return CONTENT_URI.buildUpon().appendPath("DETAILS").appendPath(movie_id).build();
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }

    //table to store movie trailers
    public static final class TrailersEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String TABLE_NAME = "trailers";

        //foreign key for movies table
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_URL = "url";

        public static Uri buildTrailersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTrailersByMovieId(String movie_id) {
            return CONTENT_URI.buildUpon().appendPath("MOVIE").appendPath(movie_id).build();
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }

    //table to store movie reviews
    public static final class ReviewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String TABLE_NAME = "reviews";

        //foreign key for movies table
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";

        public static Uri buildReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewsByMovieId(String movie_id) {
            return CONTENT_URI.buildUpon().appendPath("MOVIE").appendPath(movie_id).build();
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }

    //table to store movie sorting details
    public static final class SortingEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SORTING).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SORTING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SORTING;

        public static final String TABLE_NAME = "sorting";

        //foreign key for movies table
        public static final String COLUMN_MOVIE_ID = "movie_id";

        //sort_type will be one of the following;
        //most_popular, high_rating, most_rated, favorite
        public static final String COLUMN_SORT_TYPE = "sort_type";
        public static final String COLUMN_SORT_ORDER = "sort_order";

        public static Uri buildSortingUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesBySort(String sortType) {
            return CONTENT_URI.buildUpon().appendPath("SORT").appendPath(sortType).build();
        }

        public static Uri buildSortingByMovieId(String movieId) {
            return CONTENT_URI.buildUpon().appendPath("MOVIE").appendPath(movieId).build();
        }
        
        public static String getSortTypeFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }

}
