package com.simplesolutions2003.happybabycare;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SuriyaKumar on 8/20/2016.
 */
public class StoriesFragment extends Fragment {
    public final static boolean KEEP_IN_STACK = true;
    public final static String TAG = StoriesFragment.class.getSimpleName();

    public StoriesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.stories, container, false);

        return rootView;
    }
}
