package edu.neu.madcourse.ranchen.communication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.scraggle.NewGameActivity;

public class Communication extends Activity {
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        Button singlePlayerButton = (Button)this.findViewById(R.id.singleP_button);
        Button comTestButton = (Button) findViewById(R.id.communication_test_button);
        Button quitButton = (Button) this.findViewById(R.id.quit_button);
        Button ackButton = (Button) this.findViewById(R.id.ack_button);

        final Context context = this;


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
                intent.setClass(Communication.this, RegistedList.class);
                startActivity(intent);
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Link of the App's picture: \n" +
                        "maniacpaint.tumblr.com\n" +
                        "Reading file is inspired by:\n " +
                        "thenewboston.com");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();
            }
        });

    }

}
