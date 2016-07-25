package com.example.android.sunshine.wearable;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by SuriyaKumar on 7/15/2016.
 */
public class WeatherService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = WeatherService.class.getSimpleName();

    GoogleApiClient mApiClient;
    private static final String WEAR_MESSAGE_PATH = "/sunshine";
    private static final String SUNSHINE_PREF = "SunshinePref";


    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate();
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
    }

    @Override // GoogleApiClient.ConnectionCallbacks
    public void onConnected(Bundle connectionHint) {
        Log.v(LOG_TAG, "onConnected: " + connectionHint);
    }

    @Override  // GoogleApiClient.ConnectionCallbacks
    public void onConnectionSuspended(int cause) {
        Log.d(LOG_TAG, "onConnectionSuspended: " + cause);
    }

    @Override  // GoogleApiClient.ConnectionCallbacks
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed: " + connectionResult);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(LOG_TAG, "onMessageReceived: " + messageEvent);
        if (messageEvent.getPath().equalsIgnoreCase(WEAR_MESSAGE_PATH)) {
            String weather = new String(messageEvent.getData());
            if(weather != null) {
                Log.d(LOG_TAG, "weather data : " + weather);
                String[] weatherData = weather.split(";");
                if( weatherData.length >= 4) {
                    String weatherHi = weatherData[0];
                    String weatherLo = weatherData[1];
                    String weatherId = weatherData[2];
                    String weatherTS = weatherData[3];

                    SharedPreferences.Editor editor = getSharedPreferences(SUNSHINE_PREF, MODE_PRIVATE).edit();
                    editor.putString("weatherHi", weatherHi + (char) 0x00B0);
                    editor.putString("weatherLo", weatherLo + (char) 0x00B0);
                    try {
                        editor.putInt("weatherId", Integer.parseInt(weatherId));
                    }catch (NumberFormatException e){
                        Log.e(LOG_TAG, e.getMessage());
                    }
                    try {
                        editor.putLong("weatherTS", Long.parseLong(weatherTS));
                    }catch (NumberFormatException e){
                        Log.e(LOG_TAG, e.getMessage());
                    }
                    editor.commit();
                }
            }
        }
    }
}