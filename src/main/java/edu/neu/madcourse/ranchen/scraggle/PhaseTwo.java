package edu.neu.madcourse.ranchen.scraggle;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.neu.madcourse.ranchen.R;

public class PhaseTwo extends Activity {
    LetterButton[] Buttons = new LetterButton[9];
    NewGameActivity newGameActivity;
    LetterButton previouslyPressed = null;
    WordBuilder wordBuilder;
    TextView txtTimer;
    StringBuilder sb = new StringBuilder();
    ArrayList<String>dictionary = new ArrayList<>();
    String result = "";
    MyTimer mTimer;
    long remainMilli = 0;
    boolean isRunning=false;
    int score;
    TextView scoreButton;
    ArrayList<String> output = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase_two);



        String data = getIntent().getExtras().getString("arg");
        score = getIntent().getIntExtra("score", 0);
        remainMilli = getIntent().getLongExtra("remainTime",0);
        Log.d("DATA?", ""+data);

        mTimer = new MyTimer(remainMilli, 1000);
        mTimer.start();

        /*nTimer = new NewGameActivity.MyTimer(remainMills,1000);*/

        final TextView textView = (TextView) findViewById(R.id.PhaseTwolist_view);

        Button oopsButton = (Button)this.findViewById(R.id.oops_button);
        oopsButton.setOnTouchListener(new OopsButtonListener());

        scoreButton =(TextView)findViewById(R.id.nscoreboard);
        scoreButton.setText(""+score);


        txtTimer = (TextView) findViewById(R.id.timer);


        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int halfScreenSize = (int)(screenWidth * 0.5);
        int quaterScreenSize = (int)(halfScreenSize * 0.5);
        GridLayout grid = (GridLayout) findViewById(R.id.phasetwo_grid_layout);
        for(int i = 0; i < 9; i++) {
            GridLayout.LayoutParams all = new GridLayout.LayoutParams();
            all.width = quaterScreenSize;
            all.height = quaterScreenSize;
            final LetterButton newButton = new LetterButton(this, i / 3, i % 3, false);
            Buttons[i] = newButton;
            newButton.setLayoutParams(all);
            grid.addView(newButton,all);
            newButton.setBackgroundColor(newGameActivity.LETTER_BUTTON_BACKGROUND);
            Buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LetterButton source = (LetterButton) v;

                    if (source.buttonSelected) {

                    }
                    if (previouslyPressed == null ||
                            (Math.abs(previouslyPressed.x - source.x) == 0 && Math.abs(previouslyPressed.y - source.y) == 1) ||
                            (Math.abs(previouslyPressed.x - source.x) == 1 && Math.abs(previouslyPressed.y - source.y) == 0) ||
                            (Math.abs(previouslyPressed.x - source.x) == 1 && Math.abs(previouslyPressed.y - source.y) == 1)) {
                        if (previouslyPressed != null)
                            previouslyPressed.setBackgroundColor(Color.rgb(52, 209, 178));
                        previouslyPressed = source;
                        source.setBackgroundColor(Color.rgb(0, 163, 131));
                        source.buttonSelected = true;
                        result += source.getText().charAt(0);
                        //sb.append(source.getText().charAt(0));
                        //result = sb.toString();
                        Log.d("result", result);
                        if (result.length() >= 2) {
                            read(result.substring(0, 2).toUpperCase());
                            if (dictionary.contains(result) && !(output.indexOf(result) >= 0)) {
                                output.add(result);
                                textView.append("\n" + result);
                                addScore(result);
                                scoreButton.setText("" + score);
                                clearSelections();
                                clearWord();
                            }
                        }
                    }

                }
            });
        }

        for (int i = 0; i < data.length(); i++) {
            char a[] = data.toCharArray();
            char c = a[i];
            Button b = Buttons[i];
            if((int)c == 0 ){
                b.setText("");
            }
            b.setText(String.valueOf(c));
        }

    }

    public void read(String s) {
        BufferedReader reader = null;
        AssetManager assetManager = this.getResources().getAssets() ;
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open(s);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader, 1024);
            String receiveString = null;
            while ((receiveString = reader.readLine()) != null) {
                dictionary.add(receiveString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearSelections() {
        previouslyPressed = null;
        for (int i =0; i < 9; i++) {
                LetterButton b = Buttons[i];
                b.setBackgroundColor(newGameActivity.LETTER_BUTTON_BACKGROUND);
                b.buttonSelected = false;
        }
    }

    public void clearWord() {
        result = "";
    }

    private class OopsButtonListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                clearSelections();
                clearWord();
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


}
