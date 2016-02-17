package edu.neu.madcourse.ranchen.asn5;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.ranchen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewgameControlFragment extends Fragment {


    public NewgameControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_newgame_control, container, false);
    }

}
