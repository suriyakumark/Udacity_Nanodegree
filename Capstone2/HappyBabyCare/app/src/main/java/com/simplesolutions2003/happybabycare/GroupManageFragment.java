package com.simplesolutions2003.happybabycare;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.simplesolutions2003.happybabycare.data.AppContract;

/**
 * Created by SuriyaKumar on 8/23/2016.
 */
public class GroupManageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public final static boolean KEEP_IN_STACK = false;
    public final static String TAG = GroupManageFragment.class.getSimpleName();

    private final static int GROUP_LOADER = 0;
    private int dPosition;
    private GroupManageAdapter groupListAdapter;
    ListView groupListView;
    EditText memberId;
    Button addMember;

    private static final String[] GROUP_COLUMNS = {
            AppContract.GroupEntry.TABLE_NAME + "." + AppContract.GroupEntry._ID,
            AppContract.GroupEntry.TABLE_NAME + "." + AppContract.GroupEntry.COLUMN_GROUP_ID,
            AppContract.GroupEntry.TABLE_NAME + "." + AppContract.GroupEntry.COLUMN_MEMBER_ID
    };


    static final int COL_GROUP_ID = 0;
    static final int COL_GROUP_GROUP_ID = 1;
    static final int COL_GROUP_MEMBER_ID = 2;

    public interface Callback {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dPosition = 0;
    }

    public GroupManageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.manage_group, container, false);
        groupListView = (ListView) rootView.findViewById(R.id.group_members_list);
        memberId = (EditText) rootView.findViewById(R.id.new_member);
        addMember = (Button) rootView.findViewById(R.id.group_add_member);

        addMember.setEnabled(false);

        memberId.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!memberId.getText().toString().isEmpty()){
                    addMember.setEnabled(true);
                }else{
                    addMember.setEnabled(false);
                }
            }
        });


        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "addMember");
                Uri uri = AppContract.GroupEntry.CONTENT_URI;

                ContentValues newValues = new ContentValues();
                newValues.put(AppContract.GroupEntry.COLUMN_GROUP_ID, MainActivity.GROUP_ID);
                newValues.put(AppContract.GroupEntry.COLUMN_MEMBER_ID, memberId.getText().toString());
                getActivity().getContentResolver().insert(uri, newValues);
                memberId.setText("");
                refreshData();
            }
        });

        groupListAdapter = new GroupManageAdapter(getActivity(),null,0);
        groupListView.setAdapter(groupListAdapter);
        return rootView;
    }

    public void onResume()
    {
        super.onResume();
        getLoaderManager().initLoader(GROUP_LOADER, null, this);
    }

    private void refreshData(){
        getLoaderManager().restartLoader(GROUP_LOADER, null, this);
        //groupListAdapter.notifyDataSetChanged();
    }

    //check which loader is initiated and get appropriate cursor using content provider
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(TAG, "onCreateLoader - " + i + " loader");

        Uri buildGroup = AppContract.GroupEntry.buildGroupByGroupIdUri(MainActivity.GROUP_ID);

        return new CursorLoader(getActivity(),
                buildGroup,
                GROUP_COLUMNS,
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
                groupListAdapter.swapCursor(cursor);
            }
        }else{
            groupListAdapter.swapCursor(null);
        }

        //scroll to top, after listview are loaded it focuses on listview
        Log.v(TAG, "onLoadFinished - dPosition " + dPosition);
        if(dPosition > 0) {
            groupListView.scrollTo(0, dPosition);
        }else{
            groupListView.scrollTo(0, 0);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        groupListAdapter.swapCursor(null);
    }


}
