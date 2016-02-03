package edu.neu.madcourse.ranchen;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import static android.provider.BaseColumns._ID;
import static edu.neu.madcourse.ranchen.database.Constants.WORD;
import static edu.neu.madcourse.ranchen.database.Constants.TABLE_NAME;

import edu.neu.madcourse.ranchen.database.WordData;

public class Dictionary extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int LOADER_ID = 1;

    private SimpleCursorAdapter mAdapter;
    private WordData words;
    private static String [] FROM = {  WORD, };
    private static int[] TO = {R.id.wordid};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        mAdapter = new SimpleCursorAdapter(this, R.layout.item, null, FROM, TO, 0);

        setListAdapter(mAdapter);

        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, this);

        /*words = new WordData(this);
        try {
            Cursor cursor = getWords();
            showWords(cursor);
        } finally {
            words.close();
        }*/
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, null, FROM, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter.swapCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    /*private Cursor getWords() {
        SQLiteDatabase db = words.getReadableDatabase();
        EditText wordlookup = (EditText) Dictionary.this.findViewById(R.id.textView);
        String search = wordlookup.getText().toString();
        Cursor cursor = db.rawQuery("SELECT * from WordTable WHERE word = ?", new String[]{search});
        return cursor;
    }

    private void showWords(Cursor cursor) {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.item,cursor,FROM,TO);
        setListAdapter(adapter);
    }*/
}
