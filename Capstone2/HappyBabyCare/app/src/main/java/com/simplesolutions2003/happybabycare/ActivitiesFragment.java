package com.simplesolutions2003.happybabycare;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.simplesolutions2003.happybabycare.data.AppContract;

/**
 * Created by SuriyaKumar on 8/20/2016.
 */
public class ActivitiesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public final static boolean KEEP_IN_STACK = true;
    public final static String TAG = ActivitiesFragment.class.getSimpleName();

    private final static int ACTIVITIES_LOADER = 0;
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

    public interface Callback {
    }

    public ActivitiesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activities, container, false);

        activitiesListView = (ListView) rootView.findViewById(R.id.activities_listview);
        activityFilterDate = (EditText) rootView.findViewById(R.id.activities_filter_date);
        activityFilterDate.setText(new Utilities(getActivity()).getCurrentDateDB());
        activitiesListAdapter = new ActivitiesAdapter(getActivity(),null,0);
        activitiesListView.setAdapter(activitiesListAdapter);

        return rootView;
    }


    public void onResume()
    {
        super.onResume();
        getLoaderManager().initLoader(ACTIVITIES_LOADER, null, this);
    }

    //check which loader is initiated and get appropriate cursor using content provider
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(TAG, "onCreateLoader - " + i + " loader");

        Uri buildActivitiesUri = AppContract.ActivitiesEntry.buildActivitiesByUserIdBabyIdUri(MainActivity.LOGGED_IN_USER_ID,MainActivity.ACTIVE_BABY_ID,activityFilterDate.getText().toString());

        return new CursorLoader(getActivity(),
                buildActivitiesUri,
                ACTIVITY_COLUMNS,
                null,
                null,
                null);

    }

    //check which loader has completed and use the data accordingly
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(TAG, "onLoadFinished - " + loader.getId() + " loader - " + cursor.getCount() + " rows retrieved");
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

}
