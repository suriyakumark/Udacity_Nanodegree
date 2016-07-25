package com.example.android.sunshine.app.sync;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by SuriyaKumar on 7/17/2016.
 */
public class WearableSync implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String LOG_TAG = WearableSync.class.getSimpleName();

    GoogleApiClient mGoogleApiClient;
    private Context context;
    private String path;
    private String message;

    WearableSync(Context context,String path,String message ) {
        this.context = context;
        this.path = path;
        this.message = message;
        initializeApiClient();
    }

    public void initializeApiClient() {
        if (null == mGoogleApiClient) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            Log.v(LOG_TAG, "GoogleApiClient created");
        }

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            Log.v(LOG_TAG, "Connecting to GoogleApiClient..");
        }

    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.v(LOG_TAG, "onConnectionSuspended called " + cause);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(LOG_TAG, "onConnectionFailed called " + connectionResult);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v(LOG_TAG, "onConnected called " + connectionHint);
        new sendWeatherDataToWearable(path,message).start();
    }

    class sendWeatherDataToWearable extends Thread {
        String path;
        String message;

        // Constructor to send a message to the data layer
        sendWeatherDataToWearable(String p, String msg) {
            path = p;
            message = msg;
            Log.v(LOG_TAG, "sendWeatherDataToWearable - " + path + "," + message);
        }

        public void run() {
            NodeApi.GetConnectedNodesResult  nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            Log.v(LOG_TAG, "nodes : " + nodes + "~" + nodes.getNodes().size());
            for(Node node : nodes.getNodes()) {

                //Node node = nodes.getNode();
                Log.v(LOG_TAG, "Activity Node is : " + node.getId() + " - " + node.getDisplayName());
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path, message.getBytes()).await();
                if (result.getStatus().isSuccess()) {
                    Log.v(LOG_TAG, "Activity Message: {" + message + "} sent to: " + node.getDisplayName());
                } else {
                    // Log an error
                    Log.v(LOG_TAG, "ERROR: failed to send Activity Message");
                }
            }
        }
    }
}
