package com.simplesolutions2003.happybabycare;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SuriyaKumar on 8/20/2016.
 */
public class FeedingFragment extends Fragment {
    public final static boolean KEEP_IN_STACK = false;
    public final static String TAG = FeedingFragment.class.getSimpleName();

    public FeedingFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.feeding, container, false);

        return rootView;
    }
}
