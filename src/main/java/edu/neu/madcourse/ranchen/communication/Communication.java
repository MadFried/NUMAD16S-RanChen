package edu.neu.madcourse.ranchen.communication;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.scraggle.NewGameActivity;

public class Communication extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        Button singlePlayerButton = (Button)this.findViewById(R.id.singleP_button);
        Button comTestButton = (Button) findViewById(R.id.communication_test_button);
        Button quitButton = (Button) this.findViewById(R.id.quit_button);

        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Communication.this, NewGameActivity.class);
                startActivity(intent);
            }
        });

        comTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Communication.this, CommunicationTest.class);
                startActivity(intent);
            }
        });

    }

}
