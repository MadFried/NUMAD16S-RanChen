package edu.neu.madcourse.ranchen;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.ranchen.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class aboutMeActivityFragment extends Fragment {

    private AlertDialog mDialog;

    public aboutMeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_about_me, container, false);

        View tictacButton = rootView.findViewById(R.id.tictac_button);
        View aboutButton = rootView.findViewById(R.id.button);
        View quitButton = rootView.findViewById(R.id.quit_button);
        View errorButton = rootView.findViewById(R.id.error_button);
        View dictButton = rootView.findViewById(R.id.dictionary_button);

        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    System.out.println((9727 / 0) + "");
                }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                System.exit(0);
            }
        });

        tictacButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
            }
        });

        dictButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Dictionary.class);
                getActivity().startActivity(intent);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), MeActivity.class);
                getActivity().startActivity(intent);
            }
               /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.aboutMe_text);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                mDialog = builder.show();
            }*/
        });
        return rootView;
    }
    @Override
    public void onPause() {
        super.onPause();

        if (mDialog != null)
            mDialog.dismiss();
    }
}
