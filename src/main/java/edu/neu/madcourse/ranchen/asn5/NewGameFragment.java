package edu.neu.madcourse.ranchen.asn5;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.asn3.Data;
import edu.neu.madcourse.ranchen.asn3.Dictionary;
import edu.neu.madcourse.ranchen.asn3.FileService;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewGameFragment extends Fragment {
    FileService fileService;

    Data d;

    String findingWord;
    StringBuilder sb = new StringBuilder();

    static private int mLargeIds[] = {R.id.word_large1,R.id.word_large2, R.id.word_large3, R.id.word_large4,
            R.id.word_large5, R.id.word_large6, R.id.word_large7, R.id.word_large8, R.id.word_large9};
    static private int mSmallIds[] = {R.id.word_small1, R.id.word_small2,R.id.word_small3,R.id.word_small4,
            R.id.word_small5, R.id.word_small6, R.id.word_small7, R.id.word_small8, R.id.word_small9};

    private NTile mEntireBoard = new NTile(this);
    private NTile mLargeTiles[] = new NTile[9];
    private NTile mSmallTiles[][] = new NTile[9][9];

    ArrayList<String>dics = new ArrayList<String>();
    ArrayList<String> NWordList = new ArrayList<>();
    ArrayList<String> tempList = new ArrayList<String>();

    private String fileDirPath = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private String fileName = "wordlist.txt";

    private ArrayList<String> words = new ArrayList<String>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        createFile();

        new PostTask().execute("");
        initGame();

        Log.d("size", dics.size() + "");

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
        Log.d("NewGame", "init game");
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

//    public void putWord() {
//        Random random = new Random();
//        View rootView = null;
//
//        for(String s :dics) {
//            if (s.length() == 9) {
//                NWordList.add(s);
//            }
//        }
//            for(int i=0; i < 9; i++) {
//                View outer = rootView.findViewById(mLargeIds[i]);
//                int x = random.nextInt(NWordList.size());
//                ArrayList<String> tempList = new ArrayList<String>();
//                tempList.add(NWordList.get(x));
//
//                for(int j=0; j<9; j++) {
//                    String s = tempList.get(j);
//                    char[] a = scramble(s);
//
//
//
//
//                }
//            }
//
//    }

    public char[] scramble(String string) {
        Random random = null;
        char a[] = string.toCharArray();
        for (int i = 0; i < a.length; i++ ) {
            int j = random.nextInt(a.length);
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        return a;
    }



    //找出字典里9个字母的单词
    //shuaffel 进3*3的tile里
    //onclick 选出char builstring
    //submit string
    //search in the dictionary
    //成功 score
    private void initViews(View rootView) {
        Random random = new Random();
        mEntireBoard.setView(rootView);
        for(int i = 0; i < dics.size(); i++) {
            if (dics.get(i).length() == 9) {
                NWordList.add(dics.get(i));
            }
        }

        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);
            int x = random.nextInt(9);
            tempList.add(NWordList.get(x));

            for (int small = 0; small < 9; small++) {
                String s = tempList.get(small);
                char[] a = scramble(s);
                char c = a[small];
//                int randomNum = random.nextInt((90 - 65) + 1) + 65;
//                char randomLetter = (char)randomNum;
                final Button inner = (Button) outer.findViewById(mSmallIds[small]);
                inner.setText(String.valueOf(c));
                final int fLarge = large;
                final int fSmall = small;
                final NTile smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inner.setBackgroundColor(Color.BLUE);
                        String temp = inner.getText().toString().toLowerCase();
                        sb.append(temp);
                        findingWord = sb.toString();
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

    public void createFile() {
        String filePath = fileDirPath + "/" + fileName;// path
        try {
            File dir = new File(fileDirPath);// dir path
            if (!dir.exists()) {
                System.out.println("dir not exist");
                if (dir.mkdirs()) {
                    System.out.println("build successfully");
                } else {
                    System.out.println("fail");
                }
            }

            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("not exists");
                InputStream ins = getResources().openRawResource(
                        R.raw.wordlist);
                System.out.println("read in");
                FileOutputStream fos = new FileOutputStream(file);
                System.out.println("write out ");
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = ins.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                System.out.println("already exists");
                fos.close();
                ins.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //read from sdcard
    public void readFromSdCard() {
        File Sdcard = Environment.getExternalStorageDirectory();
        File file = new File(Sdcard, "wordlist.txt");
        ArrayList<String>wordList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp;

            while ((temp = br.readLine())!= null) {
               dics.add(temp);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private class PostTask extends AsyncTask<String, Integer, Long> {
        @Override
        protected Long doInBackground(String... params) {
            String is  = params[0];
            d = (Data)getActivity().getApplication();
            if (dics.size() == 0){
                readFromSdCard();
            }
            return null;
        }

    }











}
