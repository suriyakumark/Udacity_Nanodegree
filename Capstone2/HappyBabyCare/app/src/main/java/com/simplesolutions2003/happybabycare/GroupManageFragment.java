package com.simplesolutions2003.happybabycare;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SuriyaKumar on 8/23/2016.
 */
public class GroupManageFragment extends Fragment {
    public final static boolean KEEP_IN_STACK = false;
    public final static String TAG = GroupManageFragment.class.getSimpleName();

    public GroupManageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.manage_group, container, false);

        return rootView;
    }

}
