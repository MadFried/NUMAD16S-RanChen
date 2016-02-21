package edu.neu.madcourse.ranchen.scraggle;

/**
 * Created by FredChen on 2/19/16.
 */
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class LetterButton extends Button {

    private View mView;
    int x, y;
    boolean buttonSelected;

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public LetterButton(Context context, int x, int y, boolean buttonSelected) {
        super(context);
        this.x = x;
        this.y = y;
        this.buttonSelected = buttonSelected;
    }
}