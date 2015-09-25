package com.simplesolutions2003.wapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Suriya on 9/24/2015.
 */
public class ApiHandler extends AsyncTask<URL, Integer, Long> {

    private Context context;
    String App_Settings_File = null;

    String Api_Url = null;

    String Api_Var_Location = null;
    String Api_Var_Unit = null;
    String Api_Var_Count = null;
    String Api_Var_ApiId = null;

    String Api_Default_Location = null;
    String Api_Default_Unit = null;
    String Api_Default_Count = null;
    String Api_Default_ApiId = null;

    String Api_Settings_Location = null;
    String Api_Settings_Unit = null;
    String Api_Settings_Count = null;
    String Api_Settings_ApiId = null;

    SharedPreferences settings = null;

    ApiHandler(Context context, String Api_Settings_Count){
        this.context = context;
        this.Api_Settings_Count = Api_Settings_Count;
        Resources resources = this.context.getResources();
        Log.d(">","In constructor");
        initializeVars();
        getUserPref();
        buildApiUrl();
    }

    protected Long doInBackground(URL... urls){
        getData();
        return Long.getLong("100");
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    }

    private void initializeVars(){
        App_Settings_File = new String(context.getResources().getString(R.string.App_Settings_File));

        Api_Url = context.getResources().getString(R.string.Api_Url);

        Api_Var_Location = context.getResources().getString(R.string.Api_Var_Location);
        Api_Var_Unit = context.getResources().getString(R.string.Api_Var_Unit);
        Api_Var_Count = context.getResources().getString(R.string.Api_Var_Count);
        Api_Var_ApiId = context.getResources().getString(R.string.Api_Var_ApiId);

        Api_Default_Location = context.getResources().getString(R.string.Api_Default_Location);
        Api_Default_Unit = context.getResources().getString(R.string.Api_Default_Unit);
        Api_Default_Count = context.getResources().getString(R.string.Api_Default_Count);
        Api_Default_ApiId = context.getResources().getString(R.string.Api_Default_ApiId);

        Api_Settings_ApiId = Api_Default_ApiId;
    }

    private void getUserPref(){
        settings = context.getSharedPreferences(App_Settings_File, 0);
        Api_Settings_Location = settings.getString("Api_Settings_Location", null);
        Api_Settings_Unit = settings.getString("Api_Settings_Unit",null);
        Log.d(">","In getUserPref");
    }
    private void buildApiUrl(){
        if(Api_Settings_Location == null) {
            Api_Url = Api_Url.replace(Api_Var_Location, Api_Default_Location);
        }else{
            Api_Url = Api_Url.replace(Api_Var_Location, Api_Settings_Location);
        }
        if(Api_Settings_Unit == null) {
            Api_Url = Api_Url.replace(Api_Var_Unit, Api_Default_Unit);
        }else{
            Api_Url = Api_Url.replace(Api_Var_Unit, Api_Settings_Unit);
        }
        if(Api_Settings_Count == null) {
            Api_Url = Api_Url.replace(Api_Var_Count, Api_Default_Count);
        }else{
            Api_Url = Api_Url.replace(Api_Var_Count, Api_Settings_Count);
        }
        if(Api_Settings_ApiId == null) {
            Api_Url = Api_Url.replace(Api_Var_ApiId, Api_Default_ApiId);
        }else{
            Api_Url = Api_Url.replace(Api_Var_ApiId, Api_Settings_ApiId);
        }
        Log.d(">", Api_Url);
        Log.d(">", "In buildApiUrl");
    }

    public String getData() {
        URL url = null;
        HttpURLConnection urlConnection = null;
        String dataResponse = null;
        try {
            url = new URL(Api_Url);
        } catch (MalformedURLException ex){
            Log.d(">","Error while retrieving data from API ");
            ex.printStackTrace();
        }
        Log.d(">", "In getData.");
        try{
        urlConnection = (HttpURLConnection) url.openConnection();
        }catch (IOException ex) {
            Log.d(">", "Error while retrieving data from API ");
            ex.printStackTrace();
        }
        Log.d(">", "In getData..");
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            dataResponse = readStream(in);

        }catch (IOException ex){
            Log.d(">", "Error while retrieving data from API ");
            ex.printStackTrace();
        }
        Log.d(">", "In getData...");
        try{
            urlConnection.disconnect();
        }catch (NullPointerException ex){

        }
        Log.d(">", ">> " + dataResponse + " <<");
        return dataResponse;
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        Log.d(">", "In readStream.");
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        Log.d(">", "In readStream...");
        return sb.toString();
    }

}
