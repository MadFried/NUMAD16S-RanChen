package edu.neu.madcourse.ranchen.scraggle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import edu.neu.madcourse.ranchen.R;

public class NewGameActivity extends Activity {
    Context context;

    static private int GridIds[] = {R.id.grid1_layout, R.id.grid2_layout,R.id.grid3_layout,R.id.grid4_layout,R.id.grid5_layout,
    R.id.grid6_layout, R.id.grid7_layout, R.id.grid8_layout, R.id.grid9_layout};

    LetterButton[][] buttons = new LetterButton[9][9];
    ArrayAdapter<String> wordListAdapter;
    WordBuilder wordBuilder;
    int score = 0;

    SharedPreferences preferences;

    LetterButtonTouchListener letterButtonListener;

    Handler handler = new Handler();

    public final static int LETTER_BUTTON_BACKGROUND = Color.rgb(144, 239, 226);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game2);

        preferences = this.getSharedPreferences("edu.neu.madcourse.ranchen.app", Context.MODE_PRIVATE);

        Button oopsButton = (Button) this.findViewById(R.id.oops_button);
        oopsButton.setOnTouchListener(new OopsButtonListener());


        TextView textView = (TextView)findViewById(R.id.scoreboard);
        textView.setText(String.valueOf(score));


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

            for (int i=0; i < 9; i++) {
                GridLayout grid = (GridLayout)findViewById(GridIds[i]) ;
                for(int j= 0; j < 9; j++) {
                    LetterButton newButton = new LetterButton(this, j / 3, j % 3, false);
                    buttons[i][j] = newButton;
                    grid.addView(newButton);
                    newButton.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
                    newButton.setOnTouchListener(letterButtonListener);

                }
            }
//        for (int small = 0; small < 9; small++) {
//            LetterButton newButton = new LetterButton(this, i / 3, i % 3, false);
//            buttons[i] = newButton;
//            grid.addView(newButton,i);
//            //newButton = (LetterButton)findViewById(mSmallIds[i]);
//            newButton.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
//            newButton.setOnTouchListener(letterButtonListener);
//        }

//        GridLayout grid2 = (GridLayout) findViewById(R.id.grid_layout2);
//        for (int i = 9; i < 17; i++) {
//            LetterButton newButton = new LetterButton(this, i / 12, i % 9, false);
//            buttons[i] = newButton;
//            grid2.addView(newButton,i);
//            newButton.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
//            newButton.setOnTouchListener(letterButtonListener);
//        }


        if (preferences.getString(String.valueOf(0), null) == null) {
            startNewGame();
        } else {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    buttons[i][j].setText(preferences.getString(String.valueOf(j), "-"));
                }
            }
        }

    }

    public int addScore(String s) {

        if (s.length() == 3) {
            score += 1;
        }
        if(s.length() == 4) {
            score += 2;
        }
        if(s.length() == 5) {
            score += 3;
        }
        if (s.length() == 6) {
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
        return score;
    }

        /**
         * Saves the current board and word state whenever the activity pauses.
         */
//        @Override
//        protected void onPause () {
//            super.onPause();
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.clear();
//            for (int i = 0; i < 9; i++) {
//                editor.putString(String.valueOf(i), buttons[i].getText().toString());
//            }
//            for (int i = 0; i < wordListAdapter.getCount(); i++) {
//                editor.putString("l" + i, wordListAdapter.getItem(i));
//            }
//
//            editor.commit();
//        }

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
         * Start a new game of Boggle, clearing the list of found words and resetting the board.
         */

    private void startNewGame() {
        clearSelections();
        ArrayList<String> scrambleList = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        for(String s: wordBuilder.getDictionary()) {
            if(s.length() == 9) {
                temp.add(s);
            }
        }

        for(int i = 0; i < 9; i++) {
            scrambleList.add(temp.remove(Math.abs(new Random().nextInt()) % temp.size()));
        }

        for (int i =0; i < 9; i++) {
            String s = scrambleList.get(i);
            for (int j=0; j < 9; j++) {
                char a [] = s.toCharArray();
                char c = a[j];
                Button b = buttons[i][j];
                b.setText(String.valueOf(c));
            }
        }
        wordBuilder.clearWord();
        wordListAdapter.clear();
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
       /* for (LetterButton b : buttons) {
            b.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
            b.buttonSelected = false;
        }*/
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
}



