package com.example.androidlab;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.androidlab.models.Message;
import com.example.androidlab.models.Message.Type;

import java.util.ArrayList;
import java.util.List;

public class MessageCenter {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public MessageCenter(Context c) {
        context = c;
    }

    public MessageCenter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(Message message) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.COL_TEXT, message.getText());
        contentValue.put(DatabaseHelper.COL_TYPE, message.getType().ordinal());
        long id = database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        dbHelper.close();
        return id;
    }

    public List<Message> fetch() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {
                DatabaseHelper.COL_ID,
                DatabaseHelper.COL_TYPE,
                DatabaseHelper.COL_TEXT
        };
        Cursor results = db.query(
                false,
                DatabaseHelper.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null,
                null
        );
        List<Message> messages = new ArrayList<>(results.getCount());
        if (results.getCount() > 0) {
            while (results.moveToNext()) {
                Message message = new Message(
                        results.getLong(results.getColumnIndex(DatabaseHelper.COL_ID)),
                        results.getString(results.getColumnIndex(DatabaseHelper.COL_TEXT)),
                        results.getInt(results.getColumnIndex(DatabaseHelper.COL_TYPE)) > 0
                                ? Type.RECEIVED : Type.SENT
                );
                messages.add(message);
            }
            results.moveToFirst();
            printCursor(results, db.getVersion());
        }
        results.close();
        db.close();
        return messages;
    }


    public void delete(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, "_id=?", new String[]{message.getId().toString()});
        db.close();
    }

    private void printCursor(Cursor cursor, int ver){
        String activity = ((Activity) context).getComponentName().flattenToString();
        Log.i( activity, "Database version: " + ver);
        Log.i(activity, "Columns: " + TextUtils.join(",", cursor.getColumnNames()));
        Log.i(activity, "Number of Results: " + cursor.getCount());
        Log.i(activity, "Results:\n");
    }
}
