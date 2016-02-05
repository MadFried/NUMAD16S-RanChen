package edu.neu.madcourse.ranchen.database;
import static android.provider.BaseColumns._ID;
import static edu.neu.madcourse.ranchen.database.Constants.TABLE_NAME;
import static edu.neu.madcourse.ranchen.database.Constants.WORD;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by FredChen on 2/1/16.
 */
public class WordData extends SQLiteOpenHelper {
    private static final String DATABAE_NAME = "word.db";
    private static final int DATABASE_VERSION = 1;
    private Context ctx;

    public WordData(Context context) {
        super(context, DATABAE_NAME, null, DATABASE_VERSION);
        ctx = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE dictionary (word TEXT NOT NULL);");

        String mCSVfile = "wordlist.csv";
        AssetManager manager = ctx.getAssets();
        InputStream is = null;
        try {
            is = manager.open(mCSVfile);
        }catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
        String line = "";
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");
                ContentValues cv = new ContentValues();
                cv.put(_ID, colums[0].trim());
                cv.put(WORD, colums[1].trim());
                db.insert(TABLE_NAME, null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }
}
