package edu.neu.madcourse.ranchen;
//

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class Dictionary extends Activity {
    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    private AlertDialog mDialog;

    public static int binarySearch(ArrayList<String> integerList, String searchValue) {

        int low = 0;
        int high = integerList.size() - 1;
        int mid = (low + high) / 2;

        while (low <= high && !integerList.get(mid).equalsIgnoreCase(searchValue)) {

            if (integerList.get(mid).compareTo(searchValue) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }

            mid = (low + high) / 2;

            if (low > high) {
                mid = -1;
            }

        }
        return mid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        Data d = (Data) getApplication();
        final ArrayList<String> words = d.getData();
        //final ArrayList<String> words = data();

        final TextView textView = (TextView) findViewById(R.id.wordshow);
        final EditText wordLookUp = (EditText) Dictionary.this.findViewById(R.id.textView);

        View clearButton = this.findViewById(R.id.clear_button);
        View returnButton = this.findViewById(R.id.return_button);
        View ackButton = this.findViewById(R.id.ack_button);
        final Context context = this;

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, aboutMeActivity.class);
                startActivity(intent);
            }
        });


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordLookUp.setText("");
                textView.setText("");
            }
        });

        ackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Link of the App's picture: \n" +
                        "maniacpaint.tumblr.com\n" +
                        "Reading file is inspired by:\n " +
                        "thenewboston.com");
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();

            }
        });

        wordLookUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    String search = wordLookUp.getText().toString();
                    if (findWord(words, search)) {
                        textView.append("\n" + search);
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                    }
                }
            }
        });

    }

    public boolean findWord(ArrayList<String> strs, String s) {
        //Collections.sort(strs);
        int value = binarySearch(strs, s);
        if (value != -1) {
            return true;
        } else {
            return false;
        }
    }

 /*   public ArrayList<String> data(){
        ArrayList<String>words = new ArrayList<>();
        Data d = (Data) getApplication();
        if(words.isEmpty()){
            words = d.getData();
        }
        return words;
    }*/
}


    /*private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText || view instanceof TextView) {
                ((EditText)view).setText("");
                ((TextView) view).setText("");
            }


            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
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

 /*   protected void onResume(){
        super.onResume();
        String inputValue = getSharedPreferences(PREF_RESTORE,MODE_PRIVATE).getString(KEY_RESTORE,"");
        textView.setText(inputValue);
    }

    protected void onPause() {
        super.onPause();
        getSharedPreferences(PREF_RESTORE,MODE_PRIVATE).edit().putString(KEY_RESTORE,).commit();
    }*/

     /* public ArrayList<String> ReadFile(InputStream inputStream) throws IOException{
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
    }*/


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

   /* public char[] read(InputStream is) throws Exception {
//        InputStream is = getResources().openRawResource(R.raw.wordlist);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            outputStream.write(buffer,0, len);
        }
        byte[] data = outputStream.toByteArray();
        String words = data.toString();
        char[] word = words.toCharArray();
        return  word;
    }*/

    /*    public boolean findWord(String[] words,String args) {
            int result = java.util.Arrays.binarySearch(words, args);
            if (result >= 0){
                return true;
            }
            else {
                return false;
            }
        }*/

  /*  public static boolean isHave(String strs, String s) {
        boolean bo = false;
        int result = strs.indexOf(s);
            if(result != -1) {
                bo = true;
            }
            else {
                bo = false;
            }
        return bo;
    }*/

