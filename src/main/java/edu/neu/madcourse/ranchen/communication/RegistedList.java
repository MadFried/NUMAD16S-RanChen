package edu.neu.madcourse.ranchen.communication;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.regions.Regions;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import edu.neu.madcourse.ranchen.R;

public class RegistedList extends Activity {
    TextView cognitoPlayerName;
    CognitoSyncManager syncClient;

    Context mContext;

    private static final String FIREBASE_DB = "https://radiant-fire-8767.firebaseio.com/";


    RemoteClient remoteClient;

    String imeistring = null;



    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registed_list);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        imeistring = telephonyManager.getDeviceId();
        cognitoPlayerName = (TextView)findViewById(R.id.cognitoplayerNameEditText);

        remoteClient = new RemoteClient(this);
        Firebase ref = new Firebase(FIREBASE_DB);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue().toString());
                cognitoPlayerName.append(snapshot.getValue().toString() + "\n");
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        /*// COGNITO CODE
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

/*
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        Log.d("Check", "" + dataset);
        String name = dataset.get("playerName");
        Log.d("Name", ""+name);*/

        /*Collection<String> dataset = remoteClient.getFireBaseData().values();
        for (String data : dataset) {
            Log.d("data", data);
            cognitoPlayerName.setText(data + "/n");
        }*/
//        syncClient.listDatasets();
    }

}
