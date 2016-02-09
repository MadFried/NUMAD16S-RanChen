package edu.neu.madcourse.ranchen.asn1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.ranchen.R;

/**
 * Created by FredChen on 1/21/16.
 */
public class MyPicFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_picture, container, false);
        return view;
    }
}

