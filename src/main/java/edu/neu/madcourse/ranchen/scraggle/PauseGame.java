package edu.neu.madcourse.ranchen.scraggle;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.ranchen.R;

public class PauseGame extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause_game);

        View Resume = (Button) findViewById(R.id.resume_button);
        View quit = (Button)findViewById(R.id.quit_button);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
