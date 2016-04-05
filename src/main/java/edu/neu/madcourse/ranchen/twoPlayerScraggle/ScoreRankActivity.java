package edu.neu.madcourse.ranchen.twoPlayerScraggle;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.communication.RemoteClient;

public class ScoreRankActivity extends Activity {

    private static final String FIREBASE_DB = "https://radiant-fire-8767.firebaseio.com/";

    TextView scoreRank;
    RemoteClient remoteClient;

    private static final String TAG = "ScoreRankAvtivity";


    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;

    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_rank);
        remoteClient = new RemoteClient(this);

        scoreRank = (TextView) findViewById(R.id.top_score_text);

        name = getIntent().getStringExtra("scoreName");
        remoteClient.fetchValue(name);
        startTimer(name);
    }

    public void startTimer(String key) {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask(key);
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        // The values can be adjusted depending on the performance
        timer.schedule(timerTask, 5000, 1000);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask(final String key) {
        timerTask = new TimerTask() {
            public void run() {
                Log.d(TAG, "isDataFetched >>>>" + remoteClient.isDataFetched());
                if(remoteClient.isDataFetched())
                {
                    handler.post(new Runnable() {

                        public void run() {
                            Log.d(TAG, "Value >>>>" + remoteClient.getValue(key));
                            //scoreRank.setText(name + ":" + remoteClient.getValue(key));
                            Toast.makeText(ScoreRankActivity.this, " Your score is:  " + remoteClient.getValue(key), Toast.LENGTH_SHORT).show();
                        }
                    });
                    stoptimertask();
                }

            }
        };
    }

}
