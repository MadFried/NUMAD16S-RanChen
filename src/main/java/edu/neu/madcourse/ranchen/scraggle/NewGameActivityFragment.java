package edu.neu.madcourse.ranchen.scraggle;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.ranchen.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewGameActivityFragment extends Fragment {

    public NewGameActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_game2, container, false);
    }
}
