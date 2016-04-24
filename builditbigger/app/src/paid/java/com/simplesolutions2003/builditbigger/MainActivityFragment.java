package com.simplesolutions2003.builditbigger;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.simplesolutions2003.builditbigger.MainActivity;
/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        MainActivity.spinner = (ProgressBar) root.findViewById(R.id.loadingSpinner);
        MainActivity.spinner.setVisibility(View.GONE);
        return root;
    }
}
