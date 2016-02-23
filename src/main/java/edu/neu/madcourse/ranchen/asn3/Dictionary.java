package edu.neu.madcourse.ranchen.asn3;
//

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.neu.madcourse.ranchen.R;


public class Dictionary extends Activity {
    public Data d = (Data) getApplication();
    //    public Data d;
    FileService fileService = new FileService();
    private AlertDialog mDialog;
    private ArrayList<String> words = new ArrayList<String>();
    private String fileDirPath = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private String fileName = "wordlist.txt";

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

       /* createFile();
        new PostTask().execute("");
*/

        final TextView textView = (TextView) findViewById(R.id.wordshow);
        final EditText wordLookUp = (EditText) Dictionary.this.findViewById(R.id.textView);

        View clearButton = this.findViewById(R.id.clear_button);
        View returnButton = this.findViewById(R.id.return_button);
        View ackButton = this.findViewById(R.id.ack_button);
        final Context context = this;


        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    //Log.d("RAN", "search >>>>>>>>>" + search.substring(0, 2).toUpperCase());
                    read(search.substring(0, 2).toUpperCase());
                    if (words.contains(search)) {
                        textView.append("\n" + search);
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                    }
                }
            }
        });

    }
/*
    private class PostTask extends AsyncTask<String, Integer, Long> {
        @Override
        protected Long doInBackground(String... params) {
            String is  = params[0];
            words = new ArrayList<>();
            d = (Data)getApplication();
            if (words.size() == 0) {
                readFromSdCard();
               // d.setData(words);
            }
            return null;
        }
    }*/
    public void read(String s) {
        BufferedReader reader = null;
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open(s);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader, 1024);
            String receiveString = null;
            while ((receiveString = reader.readLine()) != null) {
                words.add(receiveString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void onResume() {
        super.onResume();

    }

    protected void onPause() {
        super.onPause();

    }

}

//byte read

//ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[8192];
//            int len = 0;
//            while ((len = is.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, len);
//            }
//            data = outputStream.toByteArray();
//            word = new String(data);
//            values = word.split("\\s*\\r?\\n\\s*");


//create file in sdcard
/*public void createFile() {
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
    }*/

//read from sdcard
/* public ArrayList<String> readFromSdCard() {
        File Sdcard = Environment.getExternalStorageDirectory();
        File file = new File(Sdcard, "wordlist.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp;

            while ((temp = br.readLine())!= null) {
                words.add(temp);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return words;
    }*/

//bianary search for string[]
/* public int searchString(String[] names, String key) {
            int first = 0;
            int last  = names.length;

            while (first < last) {
                int mid = first + ((last - first) / 2);
                if (key.compareTo(names[mid]) < 0) {
                    last = mid;
                } else if (key.compareTo(names[mid]) > 0) {
                    first = mid + 1;
                } else {
                    return mid;
                }
            }
            return -(first + 1);
        }*/

     /* public boolean FindWord(String[] words,String args) {
        int result = searchString(words, args);
        if (result != -1){
            return true;
        }
        else {
            return false;
        }
    }*/


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

//bufferedReader readline
  /*public ArrayList<String> ReadFile() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.wordlist);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader,8192);
            String receiveString = null;
            //StringBuilder total= new StringBuilder(inputStream.available());
            while ((receiveString = bufferedReader.readLine()) != null) {
                words.add(receiveString);
            }
            buffereadReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;

    }*/

//nio read function
/* public ArrayList<String> Read(){
        RandomAccessFile randomAccessFile= null;
        File Sdcard = Environment.getExternalStorageDirectory();
        File file = new File(Sdcard, "wordlist.txt");
        try {
            randomAccessFile = new RandomAccessFile(file,"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel fileChannel = randomAccessFile.getChannel();
        final StringBuilder stringBuilder = new StringBuilder();
        final CharsetDecoder charsetDecoder = Charset.forName("UTF-8").newDecoder();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                stringBuilder.append(charsetDecoder.decode(buffer));
                buffer.clear();
                String[] lines = stringBuilder.toString().split("\r\n");

                for(String s:lines) {
                    words.add(s);
                }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }*/




