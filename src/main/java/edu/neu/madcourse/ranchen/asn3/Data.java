package edu.neu.madcourse.ranchen.asn3;

import android.app.Application;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FredChen on 2/5/16.
 */
public class Data extends Application {

    Dictionary dictionary;

    private ArrayList<String> data = new ArrayList<>();

    public Data(){
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }


}
