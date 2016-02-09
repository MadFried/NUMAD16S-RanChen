package edu.neu.madcourse.ranchen.asn3;

import android.app.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.neu.madcourse.ranchen.R;

/**
 * Created by FredChen on 2/5/16.
 */
public class Data extends Application{

    ArrayList<String> data;

    public Data(){
    }

    public ArrayList<String> getData() {
        ArrayList<String> words = null;
        try {
            words = ReadFile(getResources().openRawResource(R.raw.wordlist));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public void setData(ArrayList<String> d) {
        this.data = d;
    }

    public ArrayList<String> ReadFile(InputStream inputStream) throws IOException {
        //InputStream inputStream = getResources().openRawResource(R.raw.wordlist);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String receiveString = null;
        ArrayList<String> words = new ArrayList<>();
        while ((receiveString = bufferedReader.readLine()) != null) {
            words.add(receiveString);
        }
        inputStream.close();
        return words;
    }

}
