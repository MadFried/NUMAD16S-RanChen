package edu.neu.madcourse.ranchen.twoPlayerScraggle;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.communication.GcmNotification;
import edu.neu.madcourse.ranchen.communication.RemoteClient;

public class PickPlayer extends Activity {
    TextView cognitoPlayerName;
    Context mContext;

    private static final String FIREBASE_DB = "https://radiant-fire-8767.firebaseio.com/";


    EditText playerName;

    public String getPlayerUsingName() {
        return playerUsingName;
    }

    public void setPlayerUsingName(String playerUsingName) {
        this.playerUsingName = playerUsingName;
    }

    private String playerUsingName;

    String imeistring = null;

    RemoteClient remoteClient;

    private Button submit;
    private Button startGameWith;


    Timer timer;
    TimerTask timerTask;

    String message;

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCM Sample Demo";
    TextView mDisplay;
    EditText mMessage;
    EditText getPlayerName;
    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    private String findPlayerName;
    final android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_player);

        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(this);

        cognitoPlayerName = (TextView)findViewById(R.id.cognitoplayerNameEditText);
        getPlayerName = (EditText)findViewById(R.id.getPlayerName);

        remoteClient = new RemoteClient(this);

        playerName = (EditText)findViewById(R.id.playerNameEditText);

        submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPlayServices()) {
                    //regid = getRegistrationId(context);
                    //if (TextUtils.isEmpty(regid)) {
                    registerInBackground();
                    //}
                }
                //registerInBackground();
                playerUsingName = playerName.getText().toString();
                remoteClient.saveValue(playerUsingName, regid);
                //remoteClient.fetchValue(playerName.getText().toString());
            }
        });

        startGameWith = (Button) findViewById(R.id.start_game_with_button);
        startGameWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isOnline()) {
                    Toast.makeText(context,"Internet Failed, You Can Play Single Player mode", Toast.LENGTH_LONG).show();
                }

                message = playerUsingName + "invite you to start a scraggle game";
                if (message.equals("")) {
                    Toast.makeText(context, "Sending Message Empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                findPlayerName = getPlayerName.getText().toString();
                Log.d("youmiyou", findPlayerName);
                remoteClient.fetchValue(findPlayerName);
                startTimer(findPlayerName);
                //sendMessage(message);
            }
        });


        Firebase ref = new Firebase(FIREBASE_DB);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot.getValue().toString());
                Map<String, String> td = (HashMap<String, String>) snapshot.getValue();
                Set<String> keys = td.keySet();
                String names = "";
                for(String key : keys) {
//                    cognitoPlayerName.append(key + "\n");
                    names += key +"\n";
                }
                cognitoPlayerName.setText(names);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void sendMessage(final String message) {
       /* if (regid == null || regid.equals("")) {
            Toast.makeText(this, "You must register first", Toast.LENGTH_LONG).show();
            return;
        }*/
        if (message.isEmpty()) {
            Toast.makeText(this, "NoData", Toast.LENGTH_LONG).show();
            return;
        }

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                List<String> regIds = new ArrayList<String>();
                String reg_device = remoteClient.getValue(findPlayerName);
                //Log.d("checkcheck", reg_device);
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.message", message);
                msgParams.put("data.p1Name", playerUsingName);
                GcmNotification gcmNotification = new GcmNotification();
                regIds.clear();
                regIds.add(reg_device);
                gcmNotification.sendNotification(msgParams, regIds,PickPlayer.this);
                return "Message Sent - " + message;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        Log.i(TAG, String.valueOf(registeredVersion));
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(PickPlayer.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(CommunicationConstants.GCM_SENDER_ID);

                    // implementation to store and keep track of registered devices here
                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG, msg);
                //mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

/*    private void unregister() {
        Log.d(CommunicationConstants.TAG, "UNREGISTER USERID: " + regid);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    msg = "Sent unregistration";
                    gcm.unregister();
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                removeRegistrationId(getApplicationContext());
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
((TextView) findViewById(R.id.communication_display))
						.setText(regid);

            }
        }.execute();
    }*/

    private void removeRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(CommunicationConstants.TAG, "Removig regId on app version "
                + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.commit();
        regid = null;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
                            sendMessage(message);
                        }
                    });

                    stoptimertask();
                }

            }
        };
    }

}

