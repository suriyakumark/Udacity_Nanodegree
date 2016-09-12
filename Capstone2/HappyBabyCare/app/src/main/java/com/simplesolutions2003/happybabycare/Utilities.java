package com.simplesolutions2003.happybabycare;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SuriyaKumar on 9/3/2016.
 */
public class Utilities {

    Context context;

    public Utilities(){}

    Utilities(Context context){
        this.context = context;
    }

    SimpleDateFormat disp_date_fmt = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat disp_time_fmt = new SimpleDateFormat("HH:mm");
    SimpleDateFormat db_date_fmt = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat db_time_fmt = new SimpleDateFormat("HH:mm");

    public String getCurrentDateDB(){
        return db_date_fmt.format(new Date());
    }

    public String getCurrentDateDisp(){
        return disp_date_fmt.format(new Date());
    }

    public String convDateDb2Disp(String dateIn ){
        try {
            return disp_date_fmt.format((Date) db_date_fmt.parse(dateIn));
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String convDateDisp2Db(String dateIn ){
        try {
            return db_date_fmt.format((Date) disp_date_fmt.parse(dateIn));
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCurrentTimeDB(){
        return db_time_fmt.format(new Date().getTime());
    }

    public void resetFocus(View view){
        view.setFocusableInTouchMode(false);
        view.setFocusable(false);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
    }

    public boolean isInternetOn(){

        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }
}
