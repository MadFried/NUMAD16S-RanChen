package edu.neu.madcourse.ranchen.communication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;

import edu.neu.madcourse.ranchen.R;

public class UserRegistration extends Activity {
    EditText playerName;
    String imeistring = null;

    RemoteClient remoteClient;

    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        remoteClient = new RemoteClient(this);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);


        playerName = (EditText)findViewById(R.id.playerNameEditText);
        imeistring = telephonyManager.getDeviceId();

        submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remoteClient.saveValue(imeistring, playerName.getText().toString());
                remoteClient.fetchValue(imeistring);
            }
        });





    }


}
