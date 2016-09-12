package com.simplesolutions2003.happybabycare;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.simplesolutions2003.happybabycare.data.AppContract;

/**
 * Created by SuriyaKumar on 8/20/2016.
 */
public class FeedingFragment extends Fragment {
    public final static boolean KEEP_IN_STACK = false;
    public final static String TAG = FeedingFragment.class.getSimpleName();

    public static long FEEDING_ID = -1;

    MenuItem saveMenuItem;
    MenuItem deleteMenuItem;

    private static final String[] FEEDING_COLUMNS = {
            AppContract.FeedingEntry.TABLE_NAME + "." + AppContract.FeedingEntry._ID,
            AppContract.FeedingEntry.TABLE_NAME + "." + AppContract.FeedingEntry.COLUMN_USER_ID,
            AppContract.FeedingEntry.TABLE_NAME + "." + AppContract.FeedingEntry.COLUMN_BABY_ID,
            AppContract.FeedingEntry.TABLE_NAME + "." + AppContract.FeedingEntry.COLUMN_DATE,
            AppContract.FeedingEntry.TABLE_NAME + "." + AppContract.FeedingEntry.COLUMN_TIME,
            AppContract.FeedingEntry.TABLE_NAME + "." + AppContract.FeedingEntry.COLUMN_TYPE,
            AppContract.FeedingEntry.TABLE_NAME + "." + AppContract.FeedingEntry.COLUMN_QUANTITY,
            AppContract.FeedingEntry.TABLE_NAME + "." + AppContract.FeedingEntry.COLUMN_NOTES
    };


    static final int COL_FEEDING_ID = 0;
    static final int COL_FEEDING_USER_ID = 1;
    static final int COL_FEEDING_BABY_ID = 2;
    static final int COL_FEEDING_DATE = 3;
    static final int COL_FEEDING_TIME = 4;
    static final int COL_FEEDING_TYPE = 5;
    static final int COL_FEEDING_QUANTITY = 6;
    static final int COL_FEEDING_NOTES = 7;

    EditText activityDate;
    EditText activityTime;
    EditText activityNotes;

    Spinner feedingType;
    EditText feedingQuantity;

    public FeedingFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.feeding, container, false);

        activityDate = (EditText) rootView.findViewById(R.id.activity_date);
        activityTime = (EditText) rootView.findViewById(R.id.activity_time);
        activityNotes = (EditText) rootView.findViewById(R.id.activity_notes);

        feedingType = (Spinner) rootView.findViewById(R.id.feeding_type);
        feedingQuantity = (EditText) rootView.findViewById(R.id.feeding_quantity);


        feedingQuantity.addTextChangedListener(new TextWatcher() {

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
        activityDate.setText(new Utilities(getActivity()).getCurrentDateDisp());
        activityTime.setText(new Utilities(getActivity()).getCurrentTimeDB());

        if(FEEDING_ID != -1) {
            Uri uri = AppContract.FeedingEntry.buildFeedingUri(FEEDING_ID);
            Cursor activityEntry = getActivity().getContentResolver().query(uri,FEEDING_COLUMNS,null,null,null);
            if(activityEntry != null){
                if(activityEntry.getCount() > 0){
                    Log.v(TAG,"got feeding entry");
                    activityEntry.moveToFirst();
                    activityDate.setText(new Utilities(getActivity()).convDateDb2Disp(activityEntry.getString(COL_FEEDING_DATE)));
                    activityTime.setText(activityEntry.getString(COL_FEEDING_TIME));
                    activityNotes.setText(activityEntry.getString(COL_FEEDING_NOTES));
                    for(int iType = 0; iType < feedingType.getCount(); iType++){
                        if(activityEntry.getString(COL_FEEDING_TYPE).equals(feedingType.getItemAtPosition(iType).toString())){
                            feedingType.setSelection(iType);
                            break;
                        }
                    }

                    feedingQuantity.setText(activityEntry.getString(COL_FEEDING_QUANTITY));
                }
            }else{
                FEEDING_ID = -1;
            }
        }
        return rootView;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        ((MainActivity) getActivity()).updateToolbarTitle("Feeding - " + MainActivity.ACTIVE_BABY_NAME);
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
            if(FEEDING_ID != -1) {
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
                    !feedingQuantity.getText().toString().isEmpty()){

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
            if (FEEDING_ID != -1) {
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
        Uri uri = AppContract.FeedingEntry.CONTENT_URI;

        ContentValues newValues = new ContentValues();
        newValues.put(AppContract.FeedingEntry.COLUMN_USER_ID, MainActivity.LOGGED_IN_USER_ID);
        newValues.put(AppContract.FeedingEntry.COLUMN_BABY_ID, MainActivity.ACTIVE_BABY_ID);
        newValues.put(AppContract.FeedingEntry.COLUMN_DATE, new Utilities(getActivity()).convDateDisp2Db(activityDate.getText().toString()));
        newValues.put(AppContract.FeedingEntry.COLUMN_TIME, activityTime.getText().toString());
        newValues.put(AppContract.FeedingEntry.COLUMN_TYPE, feedingType.getSelectedItem().toString());
        newValues.put(AppContract.FeedingEntry.COLUMN_QUANTITY, feedingQuantity.getText().toString());
        newValues.put(AppContract.FeedingEntry.COLUMN_NOTES, activityNotes.getText().toString());
        Log.v(TAG,"newValues-"+newValues);
        if(FEEDING_ID == -1) {
            FEEDING_ID = AppContract.FeedingEntry.getIdFromUri(getActivity().getContentResolver().insert(uri, newValues));
            if(FEEDING_ID == -1){
                Toast.makeText(getActivity(), "Could not save feeding entry", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "Feeding entry added", Toast.LENGTH_LONG).show();
            }
        }else{
            String sWhere = AppContract.FeedingEntry.COLUMN_USER_ID + " = ? AND " + AppContract.FeedingEntry.COLUMN_BABY_ID + " = ? AND " + AppContract.FeedingEntry._ID + " = ?";
            String[] sWhereArgs = new String[]{MainActivity.LOGGED_IN_USER_ID,Long.toString(MainActivity.ACTIVE_BABY_ID),Long.toString(FEEDING_ID)};
            getActivity().getContentResolver().update(uri, newValues, sWhere,sWhereArgs);
            Toast.makeText(getActivity(), "Feeding entry saved", Toast.LENGTH_LONG).show();
        }
        goBack();
    }

    public void actionDelete(){
        Log.v(TAG, "actionDelete");

        Uri uri = AppContract.FeedingEntry.CONTENT_URI;
        String sWhere = AppContract.FeedingEntry.COLUMN_USER_ID + " = ? AND " + AppContract.FeedingEntry.COLUMN_BABY_ID + " = ? AND " + AppContract.FeedingEntry._ID + " = ?";
        String[] sWhereArgs = new String[]{MainActivity.LOGGED_IN_USER_ID,Long.toString(MainActivity.ACTIVE_BABY_ID),Long.toString(FEEDING_ID)};
        getActivity().getContentResolver().delete(uri, sWhere, sWhereArgs);
        Toast.makeText(getActivity(), "Feeding entry deleted", Toast.LENGTH_LONG).show();
        FEEDING_ID = -1;
        goBack();
    }


    public void goBack(){
        Log.v(TAG, "goBack");
        FEEDING_ID = -1;
        ((MainActivity) getActivity()).handleFragments(new ActivitiesFragment(),ActivitiesFragment.TAG,ActivitiesFragment.KEEP_IN_STACK);
        //((MainActivity) getActivity()).handleFragments(true);
    }
}
