package com.simplesolutions2003.jokeslibrary;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Suriya on 4/10/2016.
 */
public class JokesFragment extends Fragment{

    public String jokeText;
    public JokesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.lib_fragment_main, container, false);
        TextView jokeTextView = (TextView) root.findViewById(R.id.jokeText);

        jokeTextView.setText(getActivity().getIntent().getExtras().getString("Joke"));

        return root;
    }

    public void setJokeText(String jokeText){
        this.jokeText = jokeText;
    }
}
