package com.simplesolutions2003.happybabycare;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by SuriyaKumar on 8/23/2016.
 */
public class BabyFragment extends Fragment{
    public final static boolean KEEP_IN_STACK = true;
    public final static String TAG = BabyFragment.class.getSimpleName();

    public BabyFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.baby, container, false);
        Button addBabyButton = (Button) rootView.findViewById(R.id.action_add_baby);
        addBabyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) getActivity()).handleFragments(new BabyProfileFragment(),BabyProfileFragment.TAG,BabyProfileFragment.KEEP_IN_STACK);
            }
        });
        return rootView;
    }
}
