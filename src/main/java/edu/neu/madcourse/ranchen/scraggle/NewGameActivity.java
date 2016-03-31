package edu.neu.madcourse.ranchen.scraggle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.communication.RemoteClient;
import edu.neu.madcourse.ranchen.communication.GcmNotification;

public class NewGameActivity extends Activity {
    Context context;

    Timer timer;
    TimerTask timerTask;

    private int score = 0;

    static private int GridIds[] = {R.id.grid1_layout, R.id.grid2_layout,R.id.grid3_layout,R.id.grid4_layout,R.id.grid5_layout,
    R.id.grid6_layout, R.id.grid7_layout, R.id.grid8_layout, R.id.grid9_layout};

    LetterButton[][] buttons = new LetterButton[9][9];
    ArrayAdapter<String> wordListAdapter;
    WordBuilder wordBuilder;

    //SharedPreferences preferences;

    LetterButtonTouchListener letterButtonListener;
    ReuseButtonTouchListener reuseButtonTouchListener;

    Handler handler = new Handler();

    public final static int LETTER_BUTTON_BACKGROUND = Color.rgb(144, 239, 226);

    private CountDownTimer countDownTimer;
    long finishTime = 180;
    long nMillisUntilFinished = 0;

    private int flag = 0;

    TextView txtTimer;
    MyTimer mTimer;
    long remainMilli = 0;
    boolean isRunning=false;
    Button pauseButton;

    private MediaPlayer mMediaPlayer;

    ArrayList<String> scrambleList = new ArrayList<>();
    ArrayList<String> temp = new ArrayList<>();

    private String phaseTwoString = null;

    public String getPhaseTwoString() {
        return phaseTwoString;
    }

    public void setPhaseTwoString(String phaseTwoString) {
        this.phaseTwoString = phaseTwoString;
    }

    private TextView textView;

    private GridLayout grid;

    int n = 1;
    int m = 0;

    ArrayList<LetterButton> selectedWords = new ArrayList<>();

    RemoteClient remoteClient;
    String gameData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game2);

        remoteClient = new RemoteClient(this);
        //p1Name = getIntent().getExtras().getString("p1Name");






        readNineWords();
        textView = (TextView)findViewById(R.id.scoreboard);

        //preferences = this.getSharedPreferences("edu.neu.madcourse.ranchen.scraggle", Context.MODE_PRIVATE);

        //clear button
        Button oopsButton = (Button) this.findViewById(R.id.oops_button);
        oopsButton.setOnTouchListener(new OopsButtonListener());

        //user submit selection
        Button submitButton = (Button) this.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*textView.setText("" + score);
                letterButtonListener.previouslyPressed = null;
                wordBuilder.clearWord();*/
                submitSelections();
            }
        });

        //phasetwo button
        Button phaseTwoButton = (Button) this.findViewById(R.id.phaseTwo_button);
        phaseTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    phaseTwo();
                    flag = 1;
                }else {
                    Intent intent = new Intent();
                    intent.setClass(NewGameActivity.this, PhaseTwo.class);
                    intent.putExtra("arg", phaseTwoString);
                    intent.putExtra("score",score);
                    intent.putExtra("remainTime",remainMilli);
                    startActivity(intent);
                    Log.d("PhaseTwo", ""+phaseTwoString);
                }
            }
        });

        //set timer
        txtTimer = (TextView) findViewById(R.id.timer);
        mTimer = new MyTimer(finishTime*1000,1000);
        mTimer.start();


        //mute box
        final CheckBox muteCheckBox = (CheckBox) findViewById(R.id.mute_box);
        muteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(muteCheckBox.isChecked()) {
                    mMediaPlayer.setVolume(0,0);
                }
                else {
                    mMediaPlayer.setVolume(1,1);
                }
            }
        });


        ArrayList<String> wordList = new ArrayList<String>();

        // if game was previously saved, reload the list of words already made
        String savedWord;
        int key = 0;
        while ((savedWord = getPreferences(MODE_PRIVATE).getString("l" + String.valueOf(key), null)) != null) {
            wordList.add(savedWord);
            key++;
        }
        wordListAdapter = new ArrayAdapter(this.findViewById(R.id.list_view).getContext(), R.layout.wordlist_cell, R.id.wordlist_cell_textview, wordList);
        ((ListView) this.findViewById(R.id.list_view)).setAdapter(wordListAdapter);

        wordBuilder = new WordBuilder(wordListAdapter, this);
        letterButtonListener = new LetterButtonTouchListener(wordBuilder);

        // makes the boggle buttons
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            int halfScreenSize = (int)(screenWidth * 0.5);
            int quaterScreenSize = (int)(halfScreenSize * 0.21);

            for (int i=0; i < 9; i++) {
                grid = (GridLayout)findViewById(GridIds[i]) ;
                for(int j= 0; j < 9; j++) {
                    GridLayout.LayoutParams all = new GridLayout.LayoutParams();
                    all.width = quaterScreenSize;
                    all.height = quaterScreenSize;
                    LetterButton newButton = new LetterButton(this, j / 3, j % 3, false);
                    buttons[i][j] = newButton;
                    newButton.setLayoutParams(all);
                    grid.addView(newButton, all);
                    newButton.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
                    //newButton.setOnTouchListener(letterButtonListener);
                    buttons[0][j].setOnTouchListener(letterButtonListener);
                }
            }

        //show score
       /* String scr = String.valueOf(score);
        Log.d("score", scr);
        TextView textView = (TextView)findViewById(R.id.scoreboard);
        textView.setText(String.valueOf(scr));*/
       /* Bundle extras = getIntent().getExtras();
        if (extras.isEmpty()) {
            startNewGame();
        }

        if (!extras.isEmpty()) {
            accepted = getIntent().getExtras().getBoolean("accepted");
            startByP2 = getIntent().getExtras().getBoolean("startByP2");
            gameData = getIntent().getExtras().getString("gameData");
            p1Name = getIntent().getExtras().getString("p1Name");

            if (accepted && p1Name != null) {
                startNewGame();
                sendGameData(getGameData(),p1Name);
            }

            if (startByP2) {
                putgameData(gameData);
            }
        }*/




        /*if (getPreferences(MODE_PRIVATE).getString(String.valueOf(0), null) == null) {
            startNewGame();
        }*/
        boolean startByP2 = getIntent().getExtras().getBoolean("startByP2");
        if(!startByP2) {
            startNewGame();
            gameData = getGameData();

            Log.d("accepted?", getIntent().getExtras().getBoolean("accepted") + "");
            boolean acceptedFlag = getIntent().getBooleanExtra("accepted", false);
            String p1name = getIntent().getStringExtra("p1name");
            Log.d("p1ID Passed?", p1name);
            remoteClient.fetchValue(p1name);
            startTimer(acceptedFlag, p1name);
        } /*else {
        }*/
        else if(startByP2) {
            gameData = getIntent().getStringExtra("gameData");
            putgameData(gameData);
        }
       /* if(getIntent().getExtras().getBoolean("accepted")) {
            startNewGame();
            p1Name = getIntent().getExtras().getString("p1Name");
            Log.d("accepted P1Name?", p1Name);
            sendGameData(getGameData(), p1Name);
        }*/

      /*  if(getIntent().getExtras().getBoolean("startByP2")) {
            gameData = getIntent().getExtras().getString("gameData");
            putgameData(gameData);
        }*/
     /*   else {
            int index = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    buttons[i][j].setText(getPreferences(MODE_PRIVATE).getString(String.valueOf(index++), "-"));
                }
            }
        }*/

    }

        /**
         * Saves the current board and word state whenever the activity pauses.
         */
        @Override
        protected void onPause () {
            super.onPause();
            mTimer.cancel();
            mTimer = null;
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();


            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.clear();
         /*   int index = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    editor.putString(String.valueOf(index++), buttons[i][j].getText().toString());
                }
            }*/
            for (int i = 0; i < wordListAdapter.getCount(); i++) {
                editor.putString("l" + i, wordListAdapter.getItem(i));
            }
            editor.commit();
        }

    @Override
    public void onResume () {
        super.onResume();
        mMediaPlayer = MediaPlayer.create(this, R.raw.rx0);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        mTimer = new MyTimer(remainMilli, 1000);
        //mTimer.start();
    }

    @Override
    public void onStop () {
        super.onStop();
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


        /**
         * Inflates the options menu.
         * @param menu The menu to inflate
         */
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

        /**
         * Enables the handling of action bar item clicks
         * @param item
         */
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.new_game) {
            startNewGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

        /**
         * Start a new game, clearing the list of found words and resetting the board.
         */

        private void startNewGame() {
            clearSelections();
            //readNineWords();
//            ArrayList<String> receiveDictionary = wordBuilder.getDictionary();
//            ArrayList<String> temp = new ArrayList<>();
//            for (int i = 0; i < receiveDictionary.size(); i++) {
//                String s  = receiveDictionary.get(i);
//                if(s.length() == 9) {
//                    temp.add(s);
//                }
//            }
            for (int i = 0; i < 9; i++) {
                int x = Math.abs(new Random().nextInt(temp.size()));
                String NineChar = temp.get(x);
                scrambleList.add(NineChar);
                //scrambleList.add(temp.remove(Math.abs(new Random().nextInt()) % temp.size()));
                String s = scrambleList.get(i);
                char a[] = s.toCharArray();
                for (int j = 0; j < 9; j++) {
                    //int x = Math.abs(new Random().nextInt(s.length()));
                    char c = a[j];
                    Button b = buttons[i][j];
                    b.setText(String.valueOf(c));
                }
            }
            wordBuilder.clearWord();
            wordListAdapter.clear();
        }


       /* private void startNewGame() {
            clearSelections();
            int y = Math.abs(new Random().nextInt(8));
            int z = 0;
            for (int i = 0; i < 9; i++) {
                int x = Math.abs(new Random().nextInt(temp.size()));
                String NineChar = temp.get(x);
                scrambleList.add(NineChar);
                //scrambleList.add(temp.remove(Math.abs(new Random().nextInt()) % temp.size()));
                String s = scrambleList.get(i);
                char a[] = s.toCharArray();
                char c = a[0];
                LetterButton source = buttons[i][y];
                source.setText(String.valueOf(c));
                int n = 0;
                for (int j = 0; j < 9; j++) {
                    LetterButton next = buttons[i][j];
//                            if (next.getText() != null) {
//
//                            }
*//*
                            if (next.getText() == null || !(Math.abs(source.x - next.x) == 0 && Math.abs(source.y - next.y) == 1) ||
                                    !(Math.abs(source.x - next.x) == 1 && Math.abs(source.y - next.y) == 0) ||
                                    !(Math.abs(source.x - next.x) == 1 && Math.abs(source.y - next.y) == 1)) {
                                char d = a[++n];
                                next.setText(String.valueOf(d));
                                source = next;
                            }*//*
                    if (next.getText() == null || (Math.abs(source.x - next.x) == 0 && Math.abs(source.y - next.y) == 1) ||
                                    (Math.abs(source.x - next.x) == 1 && Math.abs(source.y - next.y) == 0) ||
                                    (Math.abs(source.x - next.x) == 1 && Math.abs(source.y - next.y) == 1)) {
                                char d = a[++n];
                                next.setText(String.valueOf(d));
                                source = next;
                                Log.d("n的值", ""+n);
                    }
                }
                z = n;
                Log.d("youzhima", ""+z);

                for (int m = 0; m < 9; m++) {
                    if (buttons[i][m].getText() == null) {
                        char w = a[++z];
                        buttons[i][m].setText(String.valueOf(w));
                    }
                }

                   *//* if((Math.abs(source.x  - next.x) == 0 && Math.abs(source.y  - next.y) == 1) ||
                            (Math.abs(source.x  - next.x) == 1 && Math.abs(source.y - next.y) == 0) ||
                            (Math.abs(source.x  - next.x) ==1 && Math.abs(source.y - next.y) ==1)){
                        char d = a[++j];
                        next.setText(String.valueOf(d));
                        source = next;
                        }*//*
            }
            wordBuilder.clearWord();
            wordListAdapter.clear();
        }*/

    /**
     * Clears all selected letters
     */
    public void clearSelections() {
        letterButtonListener.previouslyPressed = null;
        for (int i =0; i < 9; i++) {
            for (int j=0; j < 9; j++) {
                LetterButton b = buttons[i][j];
                b.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
                b.buttonSelected = false;
            }
        }
    }

    public void submitSelections() {
        if(letterButtonListener.previouslyPressed != null) {
           if(wordBuilder.getDictionary().contains(wordBuilder.getWordInProgress())) {
               if (n < 9) {
                   for (int i = 0; i < 9; i++) {
                       LetterButton b = buttons[n][i];
                       b.setOnTouchListener(letterButtonListener);
                   }
                   for (int j = 0; j < 9; j++) {
                       LetterButton b = buttons[m][j];
                       b.setOnTouchListener(null);
                   }
                   n++;
                   m++;
               }

                addScore(wordBuilder.getWordInProgress());
                Log.d("21321313", wordBuilder.getWordInProgress());
                letterButtonListener.previouslyPressed = null;
//              String scr = String.valueOf(score);
                Log.d("score", "" + score);
                textView.setText("" + score);
               for(int i = 0; i < 9; i++) {
                   for (int j = 0; j < 9; j++) {
                       LetterButton b = buttons[i][j];
                       if (b.buttonSelected) {
                           selectedWords.add(b);
                           b.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
                           b.buttonSelected = false;
                       }
                   }
               }
            }
        }
        wordBuilder.clearWord();
    }

    public void phaseTwo() {
                final StringBuilder sb = new StringBuilder();
                for (int x = 0; x < selectedWords.size(); x++) {
                  final LetterButton  b = selectedWords.get(x);
                    b.buttonSelected = false;
                    b.setBackgroundColor(Color.RED);
                    b.setOnTouchListener(null);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            b.setBackgroundColor(Color.rgb(0, 163, 131));
                            b.buttonSelected = true;
                            sb.append(b.getText());
                            phaseTwoString = sb.toString();
                        }
                    });
                }
               /* if(b.buttonSelected) {
                    LetterButton nButton = b;
                    nButton.buttonSelected = false;
                    nButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LetterButton now = (LetterButton) v;
                            now.setBackgroundColor(Color.BLUE);
                            sb.append(now.getText().charAt(0));
                            phaseTwoString = sb.toString();
                        }
                    });
                }*/
    }

//add score
    public void addScore(String s) {
        if (s.length() == 3) {
            score += 1;
        }
        if(s.length() == 4) {
            score += 2;
        }
        if(s.length() == 5) {
            score += 3;
        }
        if (s.length()== 6) {
            score += 4;
        }
        if (s.length() == 7) {
            score += 5;
        }
        if (s.length() == 8) {
            score += 6;
        }
        if (s.length() == 9) {
            score += 7;
        }
    }

    /**
     * This listener clears the current letter selections when clicked
     */
    private class OopsButtonListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                clearSelections();
                wordBuilder.clearWord();
            }
            return true;
        }
    }


    public class MyTimer extends CountDownTimer {

        //constructor for timer class
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        // this method called when timer is finished
        @Override
        public void onFinish() {
            // reset all variables
            txtTimer.setText("Opps!! Time Up..");
            isRunning=false;
            remainMilli = 0;
            finish();
        }

        //this method is called for every iteration of time interval
        @Override
        public void onTick(long millisUntilFinished) {

            remainMilli = millisUntilFinished;

            //calculate minutes and seconds from milliseconds
            String minute=""+(millisUntilFinished/1000)/60;
            String second=""+(millisUntilFinished/1000)%60;

            //apply style to minute and second
            if((millisUntilFinished/1000)/60<10)
                minute="0"+(millisUntilFinished/1000)/60;
            if((millisUntilFinished/1000)%60<10)
                second="0"+(millisUntilFinished/1000)%60;

            // update textview with remaining time
            txtTimer.setText(minute+":"+second);
        }
    }

    public void readNineWords() {
        BufferedReader reader = null;
        AssetManager assetManager = getResources().getAssets() ;
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open("NineWords");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader, 1024);
            String receiveString = null;
            while ((receiveString = reader.readLine()) != null) {
                temp.add(receiveString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTimer(boolean acceptedFlag,String p1name) {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask(acceptedFlag, p1name);
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

    public void initializeTimerTask(final boolean acceptedFlag, final String p1name) {
        timerTask = new TimerTask() {
            public void run() {
                Log.d("gameActivity", "isDataFetched >>>>" + remoteClient.isDataFetched());
                if(remoteClient.isDataFetched())
                {
                    handler.post(new Runnable() {

                        public void run() {
                            notifyOtherPlayer(acceptedFlag, p1name);
                        }
                    });

                    stoptimertask();
                }

            }
        };
    }

    public String getGameData() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                builder.append(buttons[i][j].getText().toString());
                builder.append(',');
            }
        }
        return builder.toString();
    }

    public void putgameData(String gameData) {
        String[] fields = gameData.split(",");
        int index = 0;
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                Button b = buttons[i][j];
                b.setText(fields[index++].toString());
            }
        }
    }

    private void notifyOtherPlayer(boolean acceptedFlag, String p1name) {
        if (acceptedFlag && ! p1name.isEmpty()) {
            Log.d("p1ID get?", p1name);
            sendGameData(p1name);
        }
    }

    private void sendGameData(final String p1name) {
       /* if (regid == null || regid.equals("")) {
            Toast.makeText(this, "You must register first", Toast.LENGTH_LONG).show();
            return;
        }*/
     /*   if (gameData.isEmpty()) {
            Toast.makeText(this, "NoData", Toast.LENGTH_LONG).show();
            return;
        }*/

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                List<String> regIds = new ArrayList<String>();
                String reg_device = remoteClient.getValue(p1name);
                /*Log.d("checkcheck", reg_device);*/

                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.gameData", gameData);
                msgParams.put("data.p2Started","p2Started");

                GcmNotification gcmNotification = new GcmNotification();
                regIds.clear();
                regIds.add(reg_device);
                gcmNotification.sendNotification(msgParams, regIds,NewGameActivity.this);

                return "Message Sent - " + p1name;
            }

          /*  @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }*/
        }.execute(null, null, null);
    }
}




