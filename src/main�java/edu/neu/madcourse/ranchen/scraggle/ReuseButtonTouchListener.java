package edu.neu.madcourse.ranchen.scraggle;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by FredChen on 2/25/16.
 */
public class ReuseButtonTouchListener implements View.OnTouchListener {
    LetterButton previouslyPressed = null;
    WordBuilder wordBuilder;
    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            LetterButton source = (LetterButton) v;
            if(source.buttonSelected) {
                return false;
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
                wordBuilder.addLetter(source.getText().charAt(0));
            } else {
                return false;
            }
        }
        return true;
    }
}
