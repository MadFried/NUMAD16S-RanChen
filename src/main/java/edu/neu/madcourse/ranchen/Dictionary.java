package edu.neu.madcourse.ranchen;
//
//import android.app.ListActivity;
//import android.app.LoaderManager;
//import android.content.Loader;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
//
//import static android.provider.BaseColumns._ID;
//import static edu.neu.madcourse.ranchen.database.Constants.WORD;
//import static edu.neu.madcourse.ranchen.database.Constants.TABLE_NAME;
//
//import edu.neu.madcourse.ranchen.database.WordData;
//
//public class Dictionary extends ListActivity  {
//
//    private WordData words;
//    private static String [] FROM = {  WORD, };
//    private static int[] TO = {R.id.word};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dictionary);
//
//        words = new WordData(this);
//        try {
//            Cursor cursor = getWords();
//            showWords(cursor);
//        } finally {
//            words.close();
//        }
//    }
//
//    private Cursor getWords() {
//        SQLiteDatabase db = words.getReadableDatabase();
//        EditText wordlookup = (EditText) Dictionary.this.findViewById(R.id.textView);
//        String search = wordlookup.getText().toString();
//        Cursor cursor = db.rawQuery("SELECT * from WordTable WHERE word = ?", new String[]{search});
//        return cursor;
//    }
//
//    private void showWords(Cursor cursor) {
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.item,cursor,FROM,TO);
//        setListAdapter(adapter);
//    }
//}

import android.app.Activity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Dictionary extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        View returnButton = this.findViewById(R.id.return_button);
        final Context context = this;
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,aboutMeActivity.class);
                    startActivity(intent);
                }
            });

        View clearButton = this.findViewById(R.id.clear_button);

        EditText wordlookup = (EditText) Dictionary.this.findViewById(R.id.textView);
        String search = wordlookup.getText().toString();

        TextView textView = (TextView) findViewById(R.id.wordshow);

                try {
                    String words = read(getResources().openRawResource(R.raw.wordlist));
                    if (words.equals(search)) {
                        textView.setText(search);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


       /* try {
            String[] lines = this.read();
            boolean bo = this.isHave(lines, search);
            if (bo == true) {
                TextView textView = (TextView) findViewById(R.id.wordshow);
                textView.setText(search);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /*public  String[] ReadFile() throws IOException{

            InputStream inputStream = getResources().openRawResource(R.raw.wordlist);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = null;
            ArrayList<String> words = new ArrayList<>();

            while ((receiveString = bufferedReader.readLine()) != null) {
                words.add(receiveString);
            }
            inputStream.close();
            return words.toArray(new String[words.size()]);
    }*/
    public boolean findWord(String[] words,String args) {
        int result = java.util.Arrays.binarySearch(words, args);
        if (result >= 0){
            return true;
        }
        else {
            return false;
        }
    }

    public String read(InputStream is) throws Exception {
//        InputStream is = getResources().openRawResource(R.raw.wordlist);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            outputStream.write(buffer,0, len);
        }
        byte[] data = outputStream.toByteArray();
        return  new String(data);
    }

  /*  public static boolean isHave(String[] strs, String s) {
        for(int i = 0; i < strs.length; i++) {
            if(strs[i].indexOf(s) != -1) {
                return true;
            }
        }
        return false;
    }*/

 /*   public void showWord() {
        EditText wordlookup = (EditText) Dictionary.this.findViewById(R.id.textView);
        String search = wordlookup.getText().toString();
        try {
            String[] lines = this.ReadFile();
            boolean bo = this.isHave(lines, search);
            if (bo == true) {
                TextView textView = (TextView) findViewById(R.id.wordshow);
                textView.setText(search);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
