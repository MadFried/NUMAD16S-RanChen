package edu.neu.madcourse.ranchen.scraggle;

import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;

import edu.neu.madcourse.ranchen.R;

public class PhaseTwo extends Activity {
    LetterButton[] Buttons = new LetterButton[9];
    NewGameActivity newGameActivity;
    ReuseButtonTouchListener reuseButtonTouchListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase_two);


        String data = getIntent().getExtras().getString("arg");
        Log.d("DATA?", ""+data);

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
            Buttons[i].setOnTouchListener(reuseButtonTouchListener);
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

}
