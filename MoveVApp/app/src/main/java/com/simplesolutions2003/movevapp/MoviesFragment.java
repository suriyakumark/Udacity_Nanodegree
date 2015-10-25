package com.simplesolutions2003.movevapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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

public class MoviesFragment extends Fragment {

    // store thumbs, movie details to be passed to details screen
    private ImageAdapter mMoviesAdapter = new ImageAdapter(getActivity());
    private ArrayList<String> mThumbIds = new ArrayList<String>();
    private ArrayList<ArrayList<String>> mMovieDtls = new ArrayList<ArrayList<String>>();

    //constructor
    public MoviesFragment() {
    }

    //this screen has options menu, set it to true
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    //depending on the option selected, execute the async task passing choice of user
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_high_rating) {
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute("HighRating");
            return true;
        }else if (id == R.id.action_most_popular) {
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute("MostPopular");
            return true;
        }else if (id == R.id.action_most_rating) {
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute("MostRating");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //default screen when user sees it the first time
    //lets use the default sort
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMoviesAdapter);
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute("Default");

        //set a listener for the gridview to start the detail activity
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ArrayList<String> movieDetail = mMoviesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movieDetail);
                startActivity(intent);
            }
        });

        return rootView;
    }

    //this will handle the gridview using picasso
    public class ImageAdapter extends BaseAdapter {

        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.size();
        }

        void add(String path) {
            mThumbIds.add(path);
        }

        void clear() {
            mThumbIds.clear();
        }

        void remove(int index){
            mThumbIds.remove(index);
        }

        @Override
        public ArrayList<String> getItem(int position) {
            return mMovieDtls.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_movies, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_movies_imgview);
            Picasso.with(getActivity())
                    .load(mThumbIds.get(position))
                    .placeholder(R.raw.loading)
                    .error(R.raw.no_image)
                    .noFade().resize(342,491)
                    .centerCrop()
                    .into(imageView);
            return imageView;
        }

    }

    //async task should be used to get any data from the web
    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private ArrayList<String> getMovieDataFromJson(String moviesJsonStr)
                throws JSONException {
            String Base_Image_URL = new String("http://image.tmdb.org/t/p/w342/");
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = moviesJson.getJSONArray("results");
            mThumbIds.clear();
            mMovieDtls.clear();
            for(int i = 0; i < movieArray.length(); i++) {

                JSONObject currMovie = movieArray.getJSONObject(i);
                String mPosterPath = currMovie.getString("poster_path");
                String mOriginalTitle = currMovie.getString("original_title");
                String mOverview = currMovie.getString("overview");
                String mVoteAverage = currMovie.getString("vote_average");
                String mReleaseDate = currMovie.getString("release_date");
                mThumbIds.add(Base_Image_URL + mPosterPath);
                mMovieDtls.add(i, new ArrayList<String>(Arrays.asList(mOriginalTitle,Base_Image_URL + mPosterPath,mOverview,mVoteAverage,mReleaseDate)));
                Log.v(LOG_TAG, "Movies entry: " + Base_Image_URL + mPosterPath);
                Log.v(LOG_TAG, "Movies dtls: " + new ArrayList<String>(Arrays.asList(mOriginalTitle,mOverview,mVoteAverage,mReleaseDate)));
            }

            return mThumbIds;

        }

        //background task for the async task, actual data fetch is done here
        @Override
        protected ArrayList<String> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;
            String sort_by = null;

            switch(params[0]){
                case "HighRating":
                    sort_by = "vote_average.desc";
                    break;
                case "MostPopular":
                    sort_by = "popularity.desc";
                    break;
                case "MostRating":
                    sort_by = "vote_count.desc";
                    break;
                default:
                    sort_by = "popularity.desc";
                    break;
            }

            try {
                final String MOVIES_BASE_URL =
                        "https://api.themoviedb.org/3/discover/movie";
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
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                moviesJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Movies string: " + moviesJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                return null;
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
                return getMovieDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }
        //notify the adapter that the images have changed
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {
                mMoviesAdapter.notifyDataSetChanged();
                Log.v(LOG_TAG, "notified dataset change");
            }
        }
    }
}
