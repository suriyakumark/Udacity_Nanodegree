package it.jaschke.alexandria;

/**
 * Created by Suriya on 1/15/2016.
 */
import android.content.Intent;
import android.support.v4.app.Fragment;

import it.jaschke.alexandria.IntentIntegrator;

public final class FragmentIntentIntegrator extends IntentIntegrator {

    private final Fragment fragment;

    public FragmentIntentIntegrator(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
    }

    @Override
    protected void startActivityForResult(Intent intent, int code) {
        fragment.startActivityForResult(intent, code);
    }
}