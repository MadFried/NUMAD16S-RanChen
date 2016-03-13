package edu.neu.madcourse.ranchen.communication;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;

import java.util.List;
import java.util.logging.Handler;

import edu.neu.madcourse.ranchen.R;

public class UserRegistration extends Activity {
    EditText playerName;
    CognitoSyncManager syncClient;
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



/*        // COGNITO CODE
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:479c09b9-c25b-4aa5-8a2b-6c8c124a03fd", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);*/
    }

    /*public void submit(View v) {
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        Log.d("checkDataset","" + dataset);
        dataset.put("playerName", playerName.getText().toString());
        dataset.put("imei", imeistring);

        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords){
                Log.d("checkSuccess", "SUCCESS>>>>>>>");
            }
        });
    }*/

}
