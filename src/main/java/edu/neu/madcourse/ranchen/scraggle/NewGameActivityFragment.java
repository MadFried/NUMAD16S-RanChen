package edu.neu.madcourse.ranchen.scraggle;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import edu.neu.madcourse.ranchen.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewGameActivityFragment extends Fragment {


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_game2, container, false);
        View Pause = rootView.findViewById(R.id.pause_button);
        View quit = rootView.findViewById(R.id.quit_button);


        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PauseGame.class);
                getActivity().startActivity(intent);
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return rootView;
    }
}
