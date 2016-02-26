package edu.neu.madcourse.ranchen.scraggle;

/**
 * Created by FredChen on 2/19/16.
 */
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LetterButtonTouchListener implements View.OnTouchListener {

    LetterButton previouslyPressed = null;

    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

    WordBuilder wordBuilder;

    NewGameActivity parent;

    public final static int LETTER_BUTTON_BACKGROUND = Color.rgb(144, 239, 226);

    public LetterButtonTouchListener(WordBuilder builder) {
        wordBuilder = builder;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
//        if (!wordBuilder.isLoaded())
//            return false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            LetterButton source = (LetterButton) view;
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
            if (source.buttonSelected)
//                source.setBackgroundColor(LETTER_BUTTON_BACKGROUND);
//                source.buttonSelected = false;
//                source = previouslyPressed;
                return false;
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