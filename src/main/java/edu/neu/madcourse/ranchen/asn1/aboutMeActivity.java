package edu.neu.madcourse.ranchen.asn1;

import android.os.Bundle;
import android.app.Activity;


import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.asn3.Dictionary;

public class aboutMeActivity extends Activity {
    Dictionary dictionary = new Dictionary();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
