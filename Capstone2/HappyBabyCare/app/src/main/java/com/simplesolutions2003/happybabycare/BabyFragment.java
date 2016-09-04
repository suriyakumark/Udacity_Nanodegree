package com.simplesolutions2003.happybabycare;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.simplesolutions2003.happybabycare.data.AppContract;

/**
 * Created by SuriyaKumar on 8/23/2016.
 */
public class BabyFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public final static boolean KEEP_IN_STACK = true;
    public final static String TAG = BabyFragment.class.getSimpleName();
    private final static int BABY_LOADER = 0;
    private int dPosition;
    private BabyAdapter babyListAdapter;
    ListView babyListView;

    private static final String[] BABY_COLUMNS = {
            AppContract.BabyEntry.TABLE_NAME + "." + AppContract.BabyEntry._ID,
            AppContract.BabyEntry.TABLE_NAME + "." + AppContract.BabyEntry.COLUMN_USER_ID,
            AppContract.BabyEntry.TABLE_NAME + "." + AppContract.BabyEntry.COLUMN_NAME,
            AppContract.BabyEntry.TABLE_NAME + "." + AppContract.BabyEntry.COLUMN_BIRTH_DATE,
            AppContract.BabyEntry.TABLE_NAME + "." + AppContract.BabyEntry.COLUMN_DUE_DATE,
            AppContract.BabyEntry.TABLE_NAME + "." + AppContract.BabyEntry.COLUMN_GENDER,
            AppContract.BabyEntry.TABLE_NAME + "." + AppContract.BabyEntry.COLUMN_PHOTO,
            AppContract.BabyEntry.TABLE_NAME + "." + AppContract.BabyEntry.COLUMN_ACTIVE
    };


    static final int COL_BABY_ID = 0;
    static final int COL_BABY_USER_ID = 1;
    static final int COL_BABY_NAME = 2;
    static final int COL_BABY_BIRTH_DATE = 3;
    static final int COL_BABY_DUE_DATE = 4;
    static final int COL_BABY_GENDER = 5;
    static final int COL_BABY_PHOTO = 6;
    static final int COL_BABY_ACTIVE = 7;

    public interface Callback {
    }

    public BabyFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dPosition = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.baby, container, false);
        babyListView = (ListView) rootView.findViewById(R.id.baby_listview);

        babyListAdapter = new BabyAdapter(getActivity(),null,0);
        babyListView.setAdapter(babyListAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add){
            MainActivity.ACTIVE_BABY_ID = -1;
            ((MainActivity) getActivity()).handleFragments(new BabyProfileFragment(),BabyProfileFragment.TAG,BabyProfileFragment.KEEP_IN_STACK);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume()
    {
        super.onResume();
        getLoaderManager().initLoader(BABY_LOADER, null, this);
    }

    //check which loader is initiated and get appropriate cursor using content provider
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(TAG, "onCreateLoader - " + i + " loader");

        Uri buildBaby = AppContract.BabyEntry.buildBabyByUserIdUri(MainActivity.LOGGED_IN_USER_ID);

        return new CursorLoader(getActivity(),
                buildBaby,
                BABY_COLUMNS,
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
                babyListAdapter.swapCursor(cursor);
            }
        }

        //scroll to top, after listview are loaded it focuses on listview
        Log.v(TAG, "onLoadFinished - dPosition " + dPosition);
        if(dPosition > 0) {
            babyListView.scrollTo(0, dPosition);
        }else{
            babyListView.scrollTo(0, 0);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        babyListAdapter.swapCursor(null);
    }

}
