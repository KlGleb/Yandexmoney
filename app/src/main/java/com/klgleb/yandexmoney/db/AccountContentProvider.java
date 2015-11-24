package com.klgleb.yandexmoney.db;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.klgleb.yandexmoney.db.AccountsDBHelper;

public class AccountContentProvider extends ContentProvider {


    // // Uri
    // authority
    public static final String AUTHORITY = "com.klgleb.yandexmoney";

    // path
    static final String CARDS_PATH = "cards";
    static final String ACCOUNT_PATH = "account";

    // Общий Uri
    public static final Uri CARDS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + CARDS_PATH);

    public static final Uri ACCOUNT_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + ACCOUNT_PATH);

    // Типы данных
    // набор строк
    static final String CARDS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CARDS_PATH;

    static final String ACCOUNT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CARDS_PATH;


    //// UriMatcher
    // общий Uri
    static final int URI_CARDS = 1;

    // Uri с указанным ID
    static final int URI_ACCOUNT = 2;


    private static final UriMatcher uriMatcher;
    public static final String TAG = "AccountContentProvider";

    private static ProviderInfo getProviderInfo(Context context, Class<? extends ContentProvider> provider, int flags)
            throws PackageManager.NameNotFoundException {
        return context.getPackageManager()
                .getProviderInfo(new ComponentName(context.getPackageName(), provider.getName()), flags);
    }

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CARDS_PATH, URI_CARDS);
        uriMatcher.addURI(AUTHORITY, ACCOUNT_PATH, URI_ACCOUNT);
    }

    AccountsDBHelper dbHelper;
    private SQLiteDatabase db;

    public AccountContentProvider() {

    }

    @Override
    public boolean onCreate() {
        dbHelper = new AccountsDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String tableName = getTableName(uri);

        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(tableName, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);

        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        String tableName = getTableName(uri);

        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_ACCOUNT:
                return ACCOUNT_CONTENT_TYPE;

            case URI_CARDS:
                return CARDS_CONTENT_TYPE;

            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.d(TAG, "insert");
        db = dbHelper.getWritableDatabase();

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri res = null;

        switch (uriMatcher.match(uri)) {
            case URI_ACCOUNT:
                db.execSQL(String.format("DELETE FROM %s", AccountsDBHelper.TABLE_ACCOUNT));
                db.insert(AccountsDBHelper.TABLE_ACCOUNT, null, values);
                contentResolver.notifyChange(ACCOUNT_CONTENT_URI, null);

                res = ACCOUNT_CONTENT_URI;

                break;
            case URI_CARDS:

                db.insert(AccountsDBHelper.TABLE_ACCOUNT, null, values);
                contentResolver.notifyChange(CARDS_CONTENT_URI, null);

                res = CARDS_CONTENT_URI;
                break;
        }

        if (res == null) {
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        return  res;


    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String tableName = getTableName(uri);
        db = dbHelper.getWritableDatabase();

        ContentResolver contentResolver = getContext().getContentResolver();

        int count = db.update(tableName, values, selection, selectionArgs);
        contentResolver.notifyChange(uri, null);
        return count;

    }

    private String getTableName(Uri uri) {
        String tableName = "";

        switch (uriMatcher.match(uri)) {
            case URI_ACCOUNT:
                tableName = AccountsDBHelper.TABLE_ACCOUNT;
                break;
            case URI_CARDS:
                tableName = AccountsDBHelper.TABLE_CARDS;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        return tableName;
    }
}
