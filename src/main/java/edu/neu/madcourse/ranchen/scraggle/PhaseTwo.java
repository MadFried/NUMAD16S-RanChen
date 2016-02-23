package edu.neu.madcourse.ranchen.scraggle;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;

import edu.neu.madcourse.ranchen.R;

public class PhaseTwo extends Activity {
    LetterButton[] Buttons = new LetterButton[9];
    NewGameActivity newGameActivity;
    LetterButtonTouchListener letterButtonTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase_two);

        GridLayout grid = (GridLayout) findViewById(R.id.phasetwo_grid_layout);
        for(int i = 0; i < 9; i++) {
            LetterButton newButton = new LetterButton(this, i / 3, i % 3, false);
            Buttons[i] = newButton;
            grid.addView(newButton);
            newButton.setBackgroundColor(newGameActivity.LETTER_BUTTON_BACKGROUND);
            newButton.setOnTouchListener(letterButtonTouchListener);
        }

        for (int i = 0; i < 9; i++) {
            String data = newGameActivity.getPhaseTwoString();
            char a[] = data.toCharArray();
            char c = a[i];
            Button b = Buttons[i];
            b.setText(String.valueOf(c));
        }

    }

}
