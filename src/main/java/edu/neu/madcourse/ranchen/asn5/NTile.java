package edu.neu.madcourse.ranchen.asn5;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.View;
import android.widget.Button;

/**
 * Created by FredChen on 2/16/16.
 */
public class NTile{

    public enum Owner{
        PICKED, BLANK, AVAILABLE
    }
    private static final int LEVEL_PICKED = 0;
    private static final int LEVEL_BLANK = 2;
    private static final int LEVEL_AVAILABLE = 3;

    private final NewGameFragment mGame;
    private View mView;
    private NTile mSubTiles[];
    private Owner mOwner = Owner.BLANK;

    public NTile(NewGameFragment game) {
        this.mGame = game;
    }

    public Owner getOwner() {
        return mOwner;
    }

    public void setOwner(Owner Owner) {
        this.mOwner = Owner;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public NTile[] getSubTiles() {
        return mSubTiles;
    }

    public void setSubTiles(NTile[] SubTiles) {
        this.mSubTiles = SubTiles;
    }

    public void updateDrawableState() {
        if(mView == null) return;
        int level = getLevel();
        if(mView.getBackground() != null) {
            mView.getBackground().setLevel(level);
        }
        if (mView instanceof Button) {
            Drawable drawable = ((Button) mView).getBackground();
            drawable.setLevel(level);
        }
    }

    private int getLevel() {
        int level = LEVEL_AVAILABLE;
        switch (mOwner) {
            case PICKED:
                level = LEVEL_PICKED;
                break;
            case BLANK:
                level = LEVEL_BLANK;
                break;
            case AVAILABLE:
                level = LEVEL_AVAILABLE;
                break;
        }
        return level;
    }


}
