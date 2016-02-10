package edu.neu.madcourse.ranchen.asn3;

import android.app.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by FredChen on 2/5/16.
 */
public class Data extends Application {

    private ArrayList<String> data = new ArrayList<>();

    public Data(){
    }

//    public ArrayList<String> getData() {
//        ArrayList<String> words = null;
//        try {
//            words = ReadFile(getResources().getAssets().open("wordlist.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return words;
//    }
//
//    public void setData(ArrayList<String> d) {
//        this.data = d;
//    }


    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }




    /*public List<String> readFile() {
        List<String> li = new ArrayList<String>();
        String path = "";
        try {
            path =  文件路径
        } catch (URISyntaxException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        File f = new File(path);
        InputStream in = null;
        try {
            in = new FileInputStream(f);
            int ab = in.available();
            byte b[] = new byte[ab];
            while (in.read(b, 0, ab) > -1) {
                System.out.println(new String(b));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return li;
    }
     */


       /* public String read(InputStream is) throws Exception {
//        InputStream is = getResources().openRawResource(R.raw.wordlist);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            outputStream.write(buffer,0, len);
        }
        byte[] data = outputStream.toByteArray();
        return  new String(data);
    }*/

}
