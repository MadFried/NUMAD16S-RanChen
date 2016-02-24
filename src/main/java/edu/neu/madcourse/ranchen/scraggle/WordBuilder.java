package edu.neu.madcourse.ranchen.scraggle;

/**
 * Created by FredChen on 2/19/16.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import edu.neu.madcourse.ranchen.R;


public class WordBuilder {



    ArrayAdapter<String> output;

    public String getWordInProgress() {
        return wordInProgress;
    }

    public void setWordInProgress(String wordInProgress) {
        this.wordInProgress = wordInProgress;
    }

    private String wordInProgress = "";

    NewGameActivity parent;


    public ArrayList<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(ArrayList<String> dictionary) {
        this.dictionary = dictionary;
    }

    private ArrayList<String> dictionary = new ArrayList<>();

    private Lock loadingLock = new ReentrantLock();
    private boolean loaded;

    public WordBuilder(ArrayAdapter<String> adapter, NewGameActivity parent) {
        this.output = adapter;
        this.parent = parent;
        this.loaded = false;
        //Create a thread to load the dictionary of words
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (dictionary.size() == 0) {
//                    loadWordDictionary();
//                    }
//                } catch (IOException ex) {
//                    return;
//                }
//                setLoaded(true);
//            }}).start();
    }

    /**
     * Reset to an empty word
     */
    public void clearWord() {
        wordInProgress = "";
    }

    /**
     * Adds a letter to the word being built; will send the word to the list if this letter completes
     * a valid word.
     * @param c A letter to add to the currently building word.
     */
    public boolean addLetter(char c) {
        wordInProgress += Character.toLowerCase(c);
        if(wordInProgress.length() >= 2) {
            read(wordInProgress.substring(0, 2).toUpperCase());
            if (dictionary.contains(wordInProgress) && !(output.getPosition(wordInProgress) >= 0)) {
                sendCurrentWord();
                //parent.clearSelections();
            }
        }
        return true;
    }

//    /**
//     * Thread-safe getter for the dictionary loading
//     * User can't start playing until the dictionary is loaded.
//     */
//    public boolean isLoaded() {
//        loadingLock.lock();
//        boolean retVal = loaded;
//        loadingLock.unlock();
//        return retVal;
//    }
//
//    /**
//     * Thread-safe setter for the dictionary loading
//     * @param newVal Whether the WordBuilder is still loading the dictionary
//     */
//    public void setLoaded(boolean newVal) {
//        loadingLock.lock();
//        loaded = newVal;
//        loadingLock.unlock();
//    }

    /**
     * Sends the current word to the list of words
     */
    private void sendCurrentWord() {
        addWordToList(wordInProgress);
    }

    /**
     * Adds the given word to the list of words
     * @param word
     */
    private void addWordToList(String word) {
        output.insert(word, 0);
        //clearWord();
    }

   /* *
     * Loads the dictionary of words, updating the progress bar as it goes.
     * Will tell the MainActivity to remove the progress bar when complete.*/

    public void read(String s) {
        BufferedReader reader = null;
        AssetManager assetManager = parent.getResources().getAssets() ;
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open(s);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader, 1024);
            String receiveString = null;
            while ((receiveString = reader.readLine()) != null) {
                dictionary.add(receiveString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private void loadWordDictionary() throws IOException {
//        final ProgressBar progress = (ProgressBar) parent.findViewById(R.id.progress_Bar);
//
//        Resources resources = parent.getResources();
//        InputStream input = resources.openRawResource(R.raw.NineWords);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//        dictionary = new ArrayList<>();
//        String nextWord;
//        char lastChar = 'a';
//        int percent_complete = 0;
//        while ((nextWord = reader.readLine()) != null) {
//            if (nextWord.charAt(0) > lastChar) {
//                lastChar = nextWord.charAt(0);
//                percent_complete += 4; //100 / 26 = roughly 4 percent
//                final int newPercentage = percent_complete;
//                parent.handler.post(new Runnable() { //Post will add this progress bar update to the events queue of the GUI thread
//                    @Override
//                    public void run() {
//                        progress.setProgress(newPercentage);
//                    }
//                });
//            }
//            dictionary.add(nextWord.trim());
//        }
//
//        parent.handler.post(new Runnable() {
//            @Override
//            public void run() {
//                progress.setVisibility(View.GONE);
//            }
//        });
//    }
}