package edu.neu.madcourse.ranchen.asn5;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.InputStream;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.asn3.Data;
import edu.neu.madcourse.ranchen.asn3.Dictionary;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewGameFragment extends Fragment {
    static private int mLargeIds[] = {R.id.word_large1,R.id.word_large2, R.id.word_large3, R.id.word_large4,
            R.id.word_large5, R.id.word_large6, R.id.word_large7, R.id.word_large8, R.id.word_large9};
    static private int mSmallIds[] = {R.id.word_small1, R.id.word_small2,R.id.word_small3,R.id.word_small4,
            R.id.word_small5, R.id.word_small6, R.id.word_small7, R.id.word_small8, R.id.word_small9};

    private NTile mEntireBoard = new NTile(this);
    private NTile mLargeTiles[] = new NTile[9];
    private NTile mSmallTiles[][] = new NTile[9][9];

    public Data d;
    public Dictionary dictionary;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PostTask().execute(getResources().openRawResource(R.raw.wordlist));
        setRetainInstance(true);
        initGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_large_board,container,false);
        initViews(rootView);
        updateAllTiles();
        return  rootView;
    }

    public void initGame() {
        Log.d("NewGame","init game");
        mEntireBoard = new NTile(this);
        for(int large = 0; large < 9; large++) {
            mLargeTiles[large] = new NTile(this);
            for(int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new NTile(this);
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);
    }

    public void putWords() {
        d.getData();



    }

    //还需要一个把字典中的char放进每个tile
    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);
        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);

            for (int small = 0; small < 9; small++) {
                Button inner = (Button) outer.findViewById(mSmallIds[small]);
                final int fLarge = large;
                final int fSmall = small;
                final NTile smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //点了过后的方法, 把每个button上的char 取出来放进一个stringBuilder, 确定后toString, 在字典里二分查找.
                    }
                });
            }
        }
    }

    private void updateAllTiles() {
        mEntireBoard.updateDrawableState();
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large].updateDrawableState();
            for (int small = 0; small < 9 ; small++) {
                mSmallTiles[large][small].updateDrawableState();
            }
        }
    }

    private class PostTask extends AsyncTask<InputStream, Integer, Long> {
        @Override
        protected Long doInBackground(InputStream... params) {
            InputStream is  = params[0];
            if (d.getData().size() == 0) {
                d.setData(dictionary.read(is));
            }
            return null;
        }
    }









}
