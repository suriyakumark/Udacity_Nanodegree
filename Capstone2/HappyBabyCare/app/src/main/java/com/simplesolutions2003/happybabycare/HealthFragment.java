package com.simplesolutions2003.happybabycare;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Spinner;
import android.widget.Toast;

import com.simplesolutions2003.happybabycare.data.AppContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import com.simplesolutions2003.happybabycare.data.AppContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
/**
 * Created by SuriyaKumar on 8/20/2016.
 */
public class HealthFragment extends Fragment {
    public final static boolean KEEP_IN_STACK = false;
    public final static String TAG = HealthFragment.class.getSimpleName();

    public static long HEALTH_ID = -1;

    MenuItem saveMenuItem;
    MenuItem deleteMenuItem;

    private static final String[] HEALTH_COLUMNS = {
            AppContract.HealthEntry.TABLE_NAME + "." + AppContract.HealthEntry._ID,
            AppContract.HealthEntry.TABLE_NAME + "." + AppContract.HealthEntry.COLUMN_USER_ID,
            AppContract.HealthEntry.TABLE_NAME + "." + AppContract.HealthEntry.COLUMN_BABY_ID,
            AppContract.HealthEntry.TABLE_NAME + "." + AppContract.HealthEntry.COLUMN_DATE,
            AppContract.HealthEntry.TABLE_NAME + "." + AppContract.HealthEntry.COLUMN_TIME,
            AppContract.HealthEntry.TABLE_NAME + "." + AppContract.HealthEntry.COLUMN_TYPE,
            AppContract.HealthEntry.TABLE_NAME + "." + AppContract.HealthEntry.COLUMN_VALUE,
            AppContract.HealthEntry.TABLE_NAME + "." + AppContract.HealthEntry.COLUMN_NOTES
    };


    static final int COL_HEALTH_ID = 0;
    static final int COL_HEALTH_USER_ID = 1;
    static final int COL_HEALTH_BABY_ID = 2;
    static final int COL_HEALTH_DATE = 3;
    static final int COL_HEALTH_TIME = 4;
    static final int COL_HEALTH_TYPE = 5;
    static final int COL_HEALTH_VALUE = 6;
    static final int COL_HEALTH_NOTES = 7;

    EditText activityDate;
    EditText activityTime;
    EditText activityNotes;

    Spinner healthType;
    EditText healthValue;

    public HealthFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.health, container, false);
        activityDate = (EditText) rootView.findViewById(R.id.activity_date);
        activityTime = (EditText) rootView.findViewById(R.id.activity_time);
        activityNotes = (EditText) rootView.findViewById(R.id.activity_notes);

        healthType = (Spinner) rootView.findViewById(R.id.health_title);
        healthValue = (EditText) rootView.findViewById(R.id.health_summary);


        healthValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }
        });

        activityDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                new Utilities(getActivity()).resetFocus(activityDate);
                validateInputs();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        activityTime.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                new Utilities(getActivity()).resetFocus(activityTime);
                validateInputs();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        SetDateEditText setActivityDate = new SetDateEditText(activityDate, getActivity());
        SetTimeEditText setActivityTime = new SetTimeEditText(activityTime, getActivity());
        activityDate.setText(new Utilities(getActivity()).getCurrentDateDB());
        activityTime.setText(new Utilities(getActivity()).getCurrentTimeDB());

        if(HEALTH_ID != -1) {
            Uri uri = AppContract.HealthEntry.buildHealthUri(HEALTH_ID);
            Cursor activityEntry = getActivity().getContentResolver().query(uri,HEALTH_COLUMNS,null,null,null);
            if(activityEntry != null){
                if(activityEntry.getCount() > 0){
                    Log.v(TAG,"got health entry");
                    activityEntry.moveToFirst();
                    activityDate.setText(activityEntry.getString(COL_HEALTH_DATE));
                    activityTime.setText(activityEntry.getString(COL_HEALTH_TIME));
                    activityNotes.setText(activityEntry.getString(COL_HEALTH_NOTES));
                    for(int iType = 0; iType < healthType.getCount(); iType++){
                        if(activityEntry.getString(COL_HEALTH_TYPE).equals(healthType.getItemAtPosition(iType).toString())){
                            healthType.setSelection(iType);
                            break;
                        }
                    }

                    healthValue.setText(activityEntry.getString(COL_HEALTH_VALUE));
                }
            }else{
                HEALTH_ID = -1;
            }
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_delete, menu);
        saveMenuItem = (MenuItem)  menu.findItem(R.id.action_save);
        deleteMenuItem = (MenuItem)  menu.findItem(R.id.action_delete);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_save){
            actionSave();
            return true;
        }
        if(id == R.id.action_delete){
            if(HEALTH_ID != -1) {
                actionDelete();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu){
        validateInputs();
        super.onPrepareOptionsMenu(menu);
    }

    public void validateInputs() {
        if(saveMenuItem != null) {
            if(!activityDate.getText().toString().isEmpty() &
                    !activityTime.getText().toString().isEmpty() &
                    !healthType.getSelectedItem().toString().isEmpty() &
                    !healthValue.getText().toString().isEmpty()){

                saveMenuItem.setEnabled(true);
                saveMenuItem.getIcon().setAlpha(255);
                Log.v(TAG, "save enabled");
            }else{
                saveMenuItem.setEnabled(false);
                saveMenuItem.getIcon().setAlpha(100);
                Log.v(TAG, "save disabled");
            }
        }
        if(deleteMenuItem != null) {
            if (HEALTH_ID != -1) {
                deleteMenuItem.setEnabled(true);
                deleteMenuItem.getIcon().setAlpha(255);
                Log.v(TAG, "delete enabled");
            } else {
                deleteMenuItem.setEnabled(false);
                deleteMenuItem.getIcon().setAlpha(100);
                Log.v(TAG, "delete disabled");
            }
        }
        ActivityCompat.invalidateOptionsMenu(getActivity());
    }

    public void actionSave(){
        Log.v(TAG, "actionSave");
        Uri uri = AppContract.HealthEntry.CONTENT_URI;

        ContentValues newValues = new ContentValues();
        newValues.put(AppContract.HealthEntry.COLUMN_USER_ID, MainActivity.LOGGED_IN_USER_ID);
        newValues.put(AppContract.HealthEntry.COLUMN_BABY_ID, MainActivity.ACTIVE_BABY_ID);
        newValues.put(AppContract.HealthEntry.COLUMN_DATE, activityDate.getText().toString());
        newValues.put(AppContract.HealthEntry.COLUMN_TIME, activityTime.getText().toString());
        newValues.put(AppContract.HealthEntry.COLUMN_TYPE, healthType.getSelectedItem().toString());
        newValues.put(AppContract.HealthEntry.COLUMN_VALUE, healthValue.getText().toString());
        newValues.put(AppContract.HealthEntry.COLUMN_NOTES, activityNotes.getText().toString());
        //Log.v(TAG,"newValues-"+newValues);
        if(HEALTH_ID == -1) {
            HEALTH_ID = AppContract.HealthEntry.getIdFromUri(getActivity().getContentResolver().insert(uri, newValues));
            if(HEALTH_ID == -1){
                Toast.makeText(getActivity(), "Could not save health entry", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "Health entry added", Toast.LENGTH_LONG).show();
            }
        }else{
            String sWhere = AppContract.HealthEntry.COLUMN_USER_ID + " = ? AND " + AppContract.HealthEntry.COLUMN_BABY_ID + " = ? AND " + AppContract.HealthEntry._ID + " = ?";
            String[] sWhereArgs = new String[]{MainActivity.LOGGED_IN_USER_ID,Long.toString(MainActivity.ACTIVE_BABY_ID),Long.toString(HEALTH_ID)};
            getActivity().getContentResolver().update(uri, newValues, sWhere,sWhereArgs);
            Toast.makeText(getActivity(), "Health entry saved", Toast.LENGTH_LONG).show();
        }
        goBack();
    }

    public void actionDelete(){
        Log.v(TAG, "actionDelete");

        Uri uri = AppContract.HealthEntry.CONTENT_URI;
        String sWhere = AppContract.HealthEntry.COLUMN_USER_ID + " = ? AND " + AppContract.HealthEntry.COLUMN_BABY_ID + " = ? AND " + AppContract.HealthEntry._ID + " = ?";
        String[] sWhereArgs = new String[]{MainActivity.LOGGED_IN_USER_ID,Long.toString(MainActivity.ACTIVE_BABY_ID),Long.toString(HEALTH_ID)};
        getActivity().getContentResolver().delete(uri, sWhere, sWhereArgs);
        Toast.makeText(getActivity(), "Health entry deleted", Toast.LENGTH_LONG).show();
        HEALTH_ID = -1;
        goBack();
    }


    public void goBack(){
        Log.v(TAG, "goBack");
        HEALTH_ID = -1;
        ((MainActivity) getActivity()).handleFragments(new ActivitiesFragment(),ActivitiesFragment.TAG,ActivitiesFragment.KEEP_IN_STACK);
        //((MainActivity) getActivity()).handleFragments(true);
    }
}
