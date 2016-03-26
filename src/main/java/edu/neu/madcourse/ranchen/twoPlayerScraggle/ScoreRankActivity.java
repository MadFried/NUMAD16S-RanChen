package edu.neu.madcourse.ranchen.twoPlayerScraggle;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.neu.madcourse.ranchen.R;

public class ScoreRankActivity extends Activity {

    private static final String FIREBASE_DB = "https://radiant-fire-8767.firebaseio.com/";

    TextView scoreRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_rank);

        scoreRank.findViewById(R.id.top_score_text);

        Firebase ref = new Firebase(FIREBASE_DB);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot.getValue().toString());
                Map<String, String> td = (HashMap<String, String>) snapshot.getValue();
                Set<String> keys = td.keySet();
                String names = "";
                for (String key : keys) {
//                    cognitoPlayerName.append(key + "\n");
                    names += key + "\n";
                }
                scoreRank.setText(names);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

}
