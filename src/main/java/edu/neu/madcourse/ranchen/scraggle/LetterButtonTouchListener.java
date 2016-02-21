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

    WordBuilder wordBuilder;

    public LetterButtonTouchListener(WordBuilder builder) {
        wordBuilder = builder;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!wordBuilder.isLoaded())
            return false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            LetterButton source = (LetterButton) view;
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
            if (source.buttonSelected)
                return false;
            if (previouslyPressed == null ||
                    (Math.abs(previouslyPressed.x - source.x) == 0 && Math.abs(previouslyPressed.y - source.y) == 1) ||
                    (Math.abs(previouslyPressed.x - source.x) == 1 && Math.abs(previouslyPressed.y - source.y) == 0)) {
                if (previouslyPressed != null) previouslyPressed.setBackgroundColor(Color.rgb(52, 209, 178));
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