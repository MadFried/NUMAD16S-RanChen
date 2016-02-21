package edu.neu.madcourse.ranchen.asn5;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.asn3.Data;
import edu.neu.madcourse.ranchen.asn3.Dictionary;

public class NewGame extends Activity {
    private NewGameFragment mNewGameFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        mNewGameFragment = (NewGameFragment) getFragmentManager().findFragmentById(R.id.fragment_new_game);


    }

}
