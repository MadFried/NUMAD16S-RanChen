package edu.neu.madcourse.ranchen.asn1;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.ranchen.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MeActivityFragment extends Fragment {

    public MeActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }
}
