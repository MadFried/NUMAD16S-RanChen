package edu.neu.madcourse.ranchen.communication;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.regions.Regions;

import edu.neu.madcourse.ranchen.R;

public class RegistedList extends Activity {
    EditText cognitoPlayerName;
    CognitoSyncManager syncClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registed_list);

        cognitoPlayerName = (EditText)findViewById(R.id.cognitoplayerNameEditText);

        // COGNITO CODE
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
                credentialsProvider);

        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        Log.d("Check", "" + dataset);
        String name = dataset.get("playerName");
        Log.d("Name", ""+name);
        cognitoPlayerName.setText(dataset.get("playerName"));
    }

}
