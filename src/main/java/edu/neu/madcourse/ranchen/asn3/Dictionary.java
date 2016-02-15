package edu.neu.madcourse.ranchen.asn3;
//

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.neu.madcourse.ranchen.R;
import edu.neu.madcourse.ranchen.asn1.aboutMeActivity;


public class Dictionary extends Activity {
    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    private AlertDialog mDialog;
    private ArrayList<String> words = new ArrayList<String>();
    private Data d;
    private byte [] data ;

    private String fileDirPath = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath();

    private String fileName = "wordlist.txt";



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

        createFile();


        d = (Data)getApplication();
        if (d.getData().size() == 0) {
            readFromSdCard();
            d.setData(words);
        }

       /* d=(Data)getApplication();
        read();
        d.setData(words);
*/
        //System.out.println("size" + read().indexOf("a",0));
        //    //TEST
//
//        try {
//            System.out.println("size" + ReadFile().size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        final ArrayList<String> words = d.getData();
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
                    if (findWord(d.getData(), search)) {
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

   /* protected void onResume(){
        super.onResume();

    }

    protected void onPause() {
        super.onPause();

    }
*/
    /*public void Read() throws IOException{
        RandomAccessFile randomAccessFile= new RandomAccessFile("wordlist.txt","r");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (fileChannel.read(buffer) > 0) {
            buffer.flip();
            for(int i = 0; i < buffer.limit(); i++) {
                buffer.get();
            }

        }
    }*/

     /* public ArrayList<String> read() {
          try{
              InputStream is = getResources().openRawResource(R.raw.wordlist);
              ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
              byte[] buffer = new byte[1024];
              int len = 0;
              while ((len = is.read(buffer)) != -1) {
                  outputStream.write(buffer,0, len);
              }
              data = outputStream.toByteArray();
              String temp = new String(data);
              String[] values = temp.split("\r\n");
              for(String temps : values) {
                  words.add(temps);
              }
              //word = temp.split("\r");
              //words = Arrays.asList(word);
          }catch (IOException e) {
              e.printStackTrace();;
          }
          return  words;
      }*/


        private void createFile() {
            String filePath = fileDirPath + "/" + fileName;// 文件路径
            try {
                File dir = new File(fileDirPath);// 目录路径
                if (!dir.exists()) {// 如果不存在，则创建路径名
                    System.out.println("要存储的目录不存在");
                    if (dir.mkdirs()) {// 创建该路径名，返回true则表示创建成功
                        System.out.println("已经创建文件存储目录");
                    } else {
                        System.out.println("创建目录失败");
                    }
                }
                // 目录存在，则将apk中raw中的需要的文档复制到该目录下
                File file = new File(filePath);
                if (!file.exists()) {// 文件不存在
                    System.out.println("要打开的文件不存在");
                    InputStream ins = getResources().openRawResource(
                            R.raw.wordlist);// 通过raw得到数据资源
                    System.out.println("开始读入");
                    FileOutputStream fos = new FileOutputStream(file);
                    System.out.println("开始写出");
                    byte[] buffer = new byte[8192];
                    int count = 0;// 循环写出
                    while ((count = ins.read(buffer)) > 0) {
                        fos.write(buffer, 0, count);
                    }
                    System.out.println("已经创建该文件");
                    fos.close();// 关闭流
                    ins.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


       /* try{
            File file = new File(Environment.getExternalStorageDirectory(),"wordlist.txt");
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileOutputStream fos = new FileOutputStream(file);

                InputStream is = getResources().openRawResource(R.raw.wordlist);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outputStream.write(buffer,0, len);
                }
                data = outputStream.toByteArray();
                fos.write(data);

                fos.close();
                is.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       /* String state = Environment.getExternalStorageState();

        FileOutputStream outputstream = null;

        File root = Environment.getExternalStorageDirectory();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File (root.getAbsolutePath() + "/txt");
            if (!file.exists()) {
                file.mkdirs();
            }
            byte[] buffer = new byte[1024];
            int read = 0;
            try {
                outputstream = new FileOutputStream(new File(file,fileName));
                while((read = inputstream.read())>0) {
                    outputstream.write(buffer,0,read);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                try {
                    inputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

            /*InputStream inputStream = getResources().openRawResource(R.raw.wordlist);
            File file = new File(Environment.getExternalStorageDirectory(), "wordlist.txt");
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = 0;
            try {
                while ((read = inputStream.read()) > 0) {
                    out.write(buffer, 0, read);
                }
            } finally {
                inputStream.close();
                out.close();
            }*/



    public ArrayList<String> readFromSdCard() {
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
    }


    /*   public static boolean isHave(String[] strs, String target) {
           Arrays.sort(strs);
        if(Arrays.binarySearch(strs,target) != 0){
            return true;
        }
           return false;
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



