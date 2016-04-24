package com.simplesolutions2003.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.simplesolutions2003.gcemodule.jokesApi.JokesApi;
import com.simplesolutions2003.jokeslibrary.JokesActivity;

import java.io.IOException;
import com.simplesolutions2003.builditbigger.MainActivity;
/**
 * Created by Suriya on 4/12/2016.
 */
public class GetJokeFromGce  extends AsyncTask<Context, Void, String> {
    private final String LOG_TAG = GetJokeFromGce.class.getSimpleName();
    private static JokesApi myApiService = null;
    private Context context;
    public boolean testMode = false;
    private GetJokeFromGceListener mListener = null;
    private String mJoke = null;
    private String mError = null;

    @Override
    protected String doInBackground(Context... params) {
        Log.v(LOG_TAG, "doInBackground");
        if(myApiService == null) {  // Only do this once
            JokesApi.Builder builder = new JokesApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://192.168.1.6:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0];

        try {
            mJoke = myApiService.getJoke("Joke").execute().getData();
            return mJoke;
        } catch (IOException e) {
            Log.v(LOG_TAG,"IOException"+e.getMessage());
            mError = "Error: " + e.getMessage();
            return mError;
        }
    }

    public GetJokeFromGce setListener(GetJokeFromGceListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.v(LOG_TAG,"onPostExecute "+result);
        Log.v(LOG_TAG,"onPostExecute "+mError);

        if(!testMode) {
            //start the JokesActivity from jokeslibrary
            //pass the joke along with the intent
            MainActivity.spinner.setVisibility(View.GONE);
            Intent myIntent = new Intent(context, JokesActivity.class);
            myIntent.putExtra("Joke", result);
            context.startActivity(myIntent);
        }
        if (this.mListener != null)
            this.mListener.onComplete(result, mError);

    }

    @Override
    protected void onCancelled() {
        if (this.mListener != null) {
            mError = "Error: AsyncTask cancelled";
            this.mListener.onComplete(null, mError);
        }
    }

    public static interface GetJokeFromGceListener {
        public void onComplete(String joke, String err);
    }
}

