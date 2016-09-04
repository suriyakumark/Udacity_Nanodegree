package com.simplesolutions2003.happybabycare;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SuriyaKumar on 9/3/2016.
 */
public class Utilities {

    Context context;
    Utilities(Context context){
        this.context = context;
    }

    SimpleDateFormat db_date_fmt = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat db_time_fmt = new SimpleDateFormat("HH:mm");

    public String getCurrentDateDB(){
        return db_date_fmt.format(new Date());
    }

    public String getCurrentTimeDB(){
        return db_time_fmt.format(new Date().getTime());
    }
}
