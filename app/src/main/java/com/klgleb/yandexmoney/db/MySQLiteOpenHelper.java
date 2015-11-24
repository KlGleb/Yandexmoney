package com.klgleb.yandexmoney.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by klgleb on 11.08.15.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String COLUMN_ID = "_id"; // primary key for all
    public static final String TAG = "MyTagMySQLiteOpenHelper";


    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public String getCreateTableQuest(String tableName, String[] texts, String[] ingeters, String[] reals) {
        String res = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY,  ", tableName, COLUMN_ID);

        ArrayList<String> arr = new ArrayList<>();

        for (String ingeter : ingeters) {
            arr.add(String.format(" %s INTEGER ", ingeter));
        }

        for (String text : texts) {
            arr.add(String.format(" %s TEXT ", text));
        }

        for (String text : reals) {
            arr.add(String.format(" %s REAL ", text));
        }

        res += join(arr, ",");


        res += ")";

        return res;
    }

    public String getCreateTableQuest(String tableName, String[] texts, String[] ingeters) {
        return getCreateTableQuest(tableName, texts, ingeters, new String[]{});
    }

    public String getCreateTableQuest(String tableName, String[] texts) {
        return getCreateTableQuest(tableName, texts, new String[]{}, new String[]{});
    }



    public ContentValues createContentValues(JSONObject jsonObject, String[] textKeys, String[] ingeterKeys) throws JSONException {

        ContentValues values = new ContentValues();

        for (String key : textKeys) {
            values.put(key, jsonObject.getString(key));
        }

        for (String key : ingeterKeys) {
            try {

                values.put(key, jsonObject.getInt(key));
            } catch (Throwable throwable) {
                Log.w(TAG, key);
                Log.w(TAG, jsonObject.toString());
            }
        }

        values.put("_id", jsonObject.getInt("id"));

        return values;

    }

    public ContentValues getFromCursor(Cursor cursor, String[] textKeys, String[] ingeterKeys) {
        ContentValues values = new ContentValues();

        for (String key : textKeys) {
            String s = cursor.getString(cursor.getColumnIndexOrThrow(key));
            values.put(key, s);
        }

        for (String key : ingeterKeys) {
            int value = cursor.getInt(cursor.getColumnIndexOrThrow(key));
            values.put(key, value);
        }

        values.put("id", cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.COLUMN_ID)));
        values.put("_id", cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.COLUMN_ID)));

        return values;
    }

    public ArrayList<ContentValues> queryAll(String tableName, String[] textKeys, String[] ingeterKeys) {

        return queryWhere(tableName, null, textKeys, ingeterKeys);
    }


    public ArrayList<ContentValues> queryWhere(String tableName, String where, String[] textKeys, String[] ingeterKeys) {

        SQLiteDatabase db = getReadableDatabase();

        String wh = "";

        if (where != null) {
            wh = " WHERE " + where;
        }

        Cursor c = db.rawQuery(
                MessageFormat.format("SELECT * FROM {0} {1}",
                        tableName, wh
                ), null
        );

        ArrayList<ContentValues> res = new ArrayList<>();

        if (c.moveToFirst()) {

            do {
                res.add(getFromCursor(c, textKeys, ingeterKeys));
            } while (c.moveToNext());

        }
        db.close();

        return res;
    }

    private String join(ArrayList<String> arr, String separator) {


        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arr.size(); i++) {
            String str = arr.get(i);

            sb.append(str);

            if (i < arr.size() - 1) {
                sb.append(separator);

            }

        }

        return sb.toString();
    }

}
