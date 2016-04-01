package edu.neu.madcourse.ranchen.twoPlayerScraggle;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.communication.RegistedList;
import edu.neu.madcourse.ranchen.scraggle.NewGameActivity;


public class GameIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private static final String TAG = "GcmIntentService";

    Handler mhandler;


    public GameIntentService() {
        super("GcmIntentService");
        mhandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.d(TAG, extras.toString());
        if (!extras.isEmpty()) {
            String message = extras.getString("message");
            String p1name = extras.getString("p1name");
            if(p1name != null) {
                Log.d("Intend P1name get?", p1name);
            }

           /* String p2name = extras.getString("p2name");
            if(p2name != null) {
                Log.d("Intent p2name get?", p2name);
            }*/
            String p2Started = extras.getString("p2Started");
            String gameData = extras.getString("gameData");

            String score = extras.getString("score");
            String opponentName = extras.getString("opponentName");

            if (message != null && p1name!= null) {
                sendNotification(message, p1name);
                GameBroadcastReceiver.completeWakefulIntent(intent);
            }
            // Release the wake lock provided by the WakefulBroadcastReceiver.


            if ( p2Started!= null && gameData != null) {
                if(p2Started.equals("p2Started")){
                    Intent gameIntent = new Intent(this,NewGameActivity.class);
                    gameIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    gameIntent.putExtra("startByP2", true);
                    gameIntent.putExtra("gameData", gameData);

                    Log.d(TAG, "gameData" + gameData);
                    getApplication().startActivity(gameIntent);
                }
                GameBroadcastReceiver.completeWakefulIntent(intent);
            }

            if (score !=null && opponentName != null) {
                boolean active;
                active = getSharedPreferences("RanChen", MODE_PRIVATE).getBoolean("active", false);
                Log.d("active or not", active+"");
                if (active) {
                    mhandler.post(new DisplayToast(this, "your opponent current score is:" + score));
                }
                if (!active) {
                    String scoreMessage = "your Opponent current score is:" + score;
                    mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent notificationIntent;

                    notificationIntent = new Intent(this,NewGameActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    notificationIntent.putExtra("show_response", "show_response");
                    PendingIntent scoreIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                            this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Time to your move")
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(scoreMessage))
                            .setContentText(scoreMessage).setTicker(scoreMessage)
                            .setAutoCancel(true);
                    mBuilder.setContentIntent(scoreIntent);
                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                }
                GameBroadcastReceiver.completeWakefulIntent(intent);
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        //GameBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    public void sendNotification(String message, String p1name) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;

        notificationIntent = new Intent(this,NewGameActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("show_response", "show_response");
        notificationIntent.putExtra("accepted", true);
        notificationIntent.putExtra("p1name", p1name);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Let the game begin")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

   /* public void sendBack(String gameData) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;

        notificationIntent = new Intent(this, NewGameActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("gameData", gameData);
        notificationIntent.putExtra("startByP2", true);
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Let the game begin")
        *//*        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)*//*
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }*/

}