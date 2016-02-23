package edu.neu.madcourse.ranchen.scraggle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import edu.neu.madcourse.ranchen.R;

public class NewGameActivity extends Activity {
    Context context;

    private int score = 0;

    static private int GridIds[] = {R.id.grid1_layout, R.id.grid2_layout,R.id.grid3_layout,R.id.grid4_layout,R.id.grid5_layout,
    R.id.grid6_layout, R.id.grid7_layout, R.id.grid8_layout, R.id.grid9_layout};

    LetterButton[][] buttons = new LetterButton[9][9];
    ArrayAdapter<String> wordListAdapter;
    WordBuilder wordBuilder;

    SharedPreferences preferences;

    LetterButtonTouchListener letterButtonListener;

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

    StringBuilder sb = new StringBuilder();

    public String getPhaseTwoString() {
        return phaseTwoString;
    }

    private String phaseTwoString = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game2);

        preferences = this.getSharedPreferences("edu.neu.madcourse.ranchen.scraggle", Context.MODE_PRIVATE);

        //clear button
        Button oopsButton = (Button) this.findViewById(R.id.oops_button);
        oopsButton.setOnTouchListener(new OopsButtonListener());

        //user submit selection
        Button submitButton = (Button) this.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    startActivity(intent);
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
        while ((savedWord = preferences.getString("l" + String.valueOf(key), null)) != null) {
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
            int quaterScreenSize = (int)(halfScreenSize * 0.2);

            for (int i=0; i < 9; i++) {
                GridLayout grid = (GridLayout)findViewById(GridIds[i]) ;
                for(int j= 0; j < 9; j++) {
                    GridLayout.LayoutParams all = new GridLayout.LayoutParams();
                    all.width = quaterScreenSize;
                    all.height = quaterScreenSize;
                    LetterButton newButton = new LetterButton(this, j / 3, j % 3, false);
                    buttons[i][j] = newButton;
                    newButton.setLayoutParams(all);
                    grid.addView(newButton, all);
                    newButton.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
                    newButton.setOnTouchListener(letterButtonListener);
                }
            }

        //show score
        String scr = String.valueOf(score);
        TextView textView = (TextView)findViewById(R.id.scoreboard);
        textView.setText(String.valueOf(scr));


        if (preferences.getString(String.valueOf(0), null) == null) {
            startNewGame();
        }
//        else {
//            int index = 0;
//            for (int i = 0; i < 9; i++) {
//                for (int j = 0; j < 9; j++) {
//                    buttons[i][j].setText(preferences.getString(String.valueOf(index++), "-"));
//                }
//            }
//        }

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

            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
//            int index = 0;
//            for (int i = 0; i < 9; i++) {
//                for (int j = 0; j < 9; j++) {
//                    editor.putString(String.valueOf(index++), buttons[i][j].getText().toString());
//                }
//            }
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
            pickLetters();
       /* dic = wordBuilder.getDictionary();

            for (String s : dic) {
                if (s.length() == 9) {
                    temp.add(s);
                }
            }*/
            for (int i = 0; i < 9; i++) {
                int x = Math.abs(new Random().nextInt(temp.size()));
                scrambleList.add(temp.get(x));
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
            pickLetters();
       *//* dic = wordBuilder.getDictionary();

            for (String s : dic) {
                if (s.length() == 9) {
                    temp.add(s);
                }
            }*//*
            int y = Math.abs(new Random().nextInt(8));

            for (int i = 0; i < 9; i++) {
                int x = Math.abs(new Random().nextInt(temp.size()));
                scrambleList.add(temp.get(x));
                //scrambleList.add(temp.remove(Math.abs(new Random().nextInt()) % temp.size()));
                String s = scrambleList.get(i);
                char a[] = s.toCharArray();
                char c = a[0];
                LetterButton source = buttons[i][y];
                source.setText(String.valueOf(c));
                for (int j = 0; j < 9; j++) {
                    LetterButton next = buttons[i][j];
                   if ((Math.abs(source.x - next.x) == 0 && Math.abs(source.y - next.y) == 1) ||
                            (Math.abs(source.x - next.x) == 1 && Math.abs(source.y - next.y) == 0) ||
                            (Math.abs(source.x - next.x) == 1 && Math.abs(source.y - next.y) == 1)) {
                        char d = a[++j];
                        next.setText(String .valueOf(d));
                        source = next;
                    }
                }
            }
            wordBuilder.clearWord();
            wordListAdapter.clear();
        }*/

    private void pickLetters() {
        ArrayList<String> receiveDictionary = wordBuilder.getDictionary();

        for (String s : receiveDictionary) {
            if(s.length() == 9) {
                temp.add(s);
            }
        }
    }

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
            addScore(wordBuilder.getWordInProgress());
            letterButtonListener.previouslyPressed = null;
        }
        wordBuilder.clearWord();
    }

    public void phaseTwo() {
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                LetterButton b = buttons[i][j];
                if(b.buttonSelected == true) {
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
                }
            }
        }
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
}




