package com.simplesolutions2003.happybabycare;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import com.simplesolutions2003.happybabycare.data.AppContract;

/**
 * Created by SuriyaKumar on 8/20/2016.
 */
public class ActivitiesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public final static boolean KEEP_IN_STACK = true;
    public final static String TAG = ActivitiesFragment.class.getSimpleName();

    private final static int ACTIVITIES_LOADER = 0;
    private static String ACTIVITIES_DATE = "";
    private int dPosition;
    private ActivitiesAdapter activitiesListAdapter;
    ListView activitiesListView;
    EditText activityFilterDate;

    private static final String[] ACTIVITY_COLUMNS = {
            AppContract.ActivitiesEntry.COLUMN_ACTIVITY_TYPE,
            AppContract.ActivitiesEntry.COLUMN_ACTIVITY_ID,
            AppContract.ActivitiesEntry.COLUMN_USER_ID,
            AppContract.ActivitiesEntry.COLUMN_BABY_ID,
            AppContract.ActivitiesEntry.COLUMN_DATE,
            AppContract.ActivitiesEntry.COLUMN_TIME,
            AppContract.ActivitiesEntry.COLUMN_SUMMARY,
            AppContract.ActivitiesEntry.COLUMN_DETAIL
    };


    static final int COL_ACTIVITY_TYPE = 0;
    static final int COL_ACTIVITY_ID = 1;
    static final int COL_ACTIVITY_USER_ID = 2;
    static final int COL_ACTIVITY_BABY_ID = 3;
    static final int COL_ACTIVITY_DATE = 4;
    static final int COL_ACTIVITY_TIME = 5;
    static final int COL_ACTIVITY_SUMMARY = 6;
    static final int COL_ACTIVITY_DETAIL = 7;


    private static final String ACTIVITY_SORT =
            AppContract.ActivitiesEntry.COLUMN_TIME + " ASC, " +
                    AppContract.ActivitiesEntry.COLUMN_ACTIVITY_TYPE + " ASC ";

    public interface Callback {
    }

    public ActivitiesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activities, container, false);

        activitiesListView = (ListView) rootView.findViewById(R.id.activities_listview);
        activityFilterDate = (EditText) rootView.findViewById(R.id.activities_filter_date);
        activitiesListAdapter = new ActivitiesAdapter(getActivity(),null,0);
        activitiesListView.setAdapter(activitiesListAdapter);

        activityFilterDate.setInputType(InputType.TYPE_NULL);
        if(ACTIVITIES_DATE.isEmpty()){
            ACTIVITIES_DATE = new Utilities(getActivity()).getCurrentDateDisp();
        }
        activityFilterDate.setText(ACTIVITIES_DATE);

        activityFilterDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                new Utilities(getActivity()).resetFocus(activityFilterDate);
                ACTIVITIES_DATE = activityFilterDate.getText().toString();
                refreshData();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        SetDateEditText setActivityFilterDate = new SetDateEditText(activityFilterDate, getActivity());

        activitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //need to handle scroll to previous position when returning from youtube/share
                dPosition = activitiesListView.getScrollY();
                Log.v(TAG,"setOnItemClickListener - dPosition " + dPosition);

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                switch(cursor.getString(COL_ACTIVITY_TYPE)){
                    case "Feeding":
                        FeedingFragment.FEEDING_ID = cursor.getLong(COL_ACTIVITY_ID);
                        ((MainActivity) getActivity()).handleFragments(new FeedingFragment(),FeedingFragment.TAG,FeedingFragment.KEEP_IN_STACK);
                        break;
                    case "Diaper":
                        DiaperFragment.DIAPER_ID = cursor.getLong(COL_ACTIVITY_ID);
                        ((MainActivity) getActivity()).handleFragments(new DiaperFragment(),DiaperFragment.TAG,DiaperFragment.KEEP_IN_STACK);
                        break;
                    case "Sleeping":
                        SleepingFragment.SLEEPING_ID = cursor.getLong(COL_ACTIVITY_ID);
                        ((MainActivity) getActivity()).handleFragments(new SleepingFragment(),SleepingFragment.TAG,SleepingFragment.KEEP_IN_STACK);
                        break;
                    case "Health":
                        HealthFragment.HEALTH_ID = cursor.getLong(COL_ACTIVITY_ID);
                        ((MainActivity) getActivity()).handleFragments(new HealthFragment(),HealthFragment.TAG,HealthFragment.KEEP_IN_STACK);
                        break;
                    default:
                        break;
                }

            }
        });

        return rootView;
    }


    public void onResume()
    {
        super.onResume();
        ((MainActivity) getActivity()).updateToolbarTitle(MainActivity.ACTIVE_BABY_NAME);
        getLoaderManager().initLoader(ACTIVITIES_LOADER, null, this);
    }

    //check which loader is initiated and get appropriate cursor using content provider
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(TAG, "onCreateLoader - " + i + " loader");

        Uri buildActivitiesUri = AppContract.ActivitiesEntry.buildActivitiesByUserIdBabyIdUri(MainActivity.LOGGED_IN_USER_ID,MainActivity.ACTIVE_BABY_ID,
                new Utilities(getActivity()).convDateDisp2Db(activityFilterDate.getText().toString()));

        return new CursorLoader(getActivity(),
                buildActivitiesUri,
                ACTIVITY_COLUMNS,
                null,
                null,
                ACTIVITY_SORT);

    }

    //check which loader has completed and use the data accordingly
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(TAG, "onLoadFinished - " + loader.getId() + " loader - " + cursor.getCount() + " rows retrieved");
        activitiesListAdapter.swapCursor(null);
        if(cursor != null){
            if (cursor.getCount() > 0) {
                activitiesListAdapter.swapCursor(cursor);
            }
        }

        //scroll to top, after listview are loaded it focuses on listview
        Log.v(TAG, "onLoadFinished - dPosition " + dPosition);
        if(dPosition > 0) {
            activitiesListView.scrollTo(0, dPosition);
        }else{
            activitiesListView.scrollTo(0, 0);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activitiesListAdapter.swapCursor(null);
    }

    private void refreshData(){
        getLoaderManager().restartLoader(ACTIVITIES_LOADER, null, this);
        //activitiesListAdapter.notifyDataSetChanged();
    }
}
