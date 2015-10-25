package com.simplesolutions2003.movevapp;

/**
 * Created by Suriya on 10/22/2015.
 */

import android.support.v7.app.ActionBarActivity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetailActivity extends ActionBarActivity {

    //start the detail fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailfragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //display the movie details here
    public static class DetailFragment extends Fragment {
        private final String LOG_TAG = DetailFragment.class.getSimpleName();
        SimpleDateFormat DATE_API_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat DATE_DISP_FORMAT = new SimpleDateFormat("MMMM dd, yyyy");

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            //get the intent and see what is being passed
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

                ArrayList<String> movieDetail = intent.getStringArrayListExtra(Intent.EXTRA_TEXT);
                Log.v(LOG_TAG, "Movie Details: " + movieDetail);

                getActivity().setTitle(movieDetail.get(0));
                //set all the view with the data retrieved
                ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_poster);
                Picasso.with(getActivity())
                        .load(movieDetail.get(1).replace("w342","w500"))
                        .placeholder(R.raw.loading)
                        .error(R.raw.no_image)
                        .noFade().resize(500,750)
                        .centerCrop()
                        .into(imageView);
                ((TextView) rootView.findViewById(R.id.movie_desc)).setText(movieDetail.get(2));
                ((RatingBar) rootView.findViewById(R.id.movie_rating)).setRating(Float.parseFloat(movieDetail.get(3)));
                try {
                    ((TextView) rootView.findViewById(R.id.movie_releasedate)).setText(DATE_DISP_FORMAT.format(DATE_API_FORMAT.parse(movieDetail.get(4))));
                }catch(ParseException e) {
                    e.printStackTrace();
                }

            }

            return rootView;
        }
    }
}