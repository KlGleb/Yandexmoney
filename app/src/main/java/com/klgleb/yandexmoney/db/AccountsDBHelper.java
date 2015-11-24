package com.klgleb.yandexmoney.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.transition.ChangeTransform;
import android.util.Log;

/**
 * Created by klgleb on 27.08.15.
 */
public class AccountsDBHelper extends MySQLiteOpenHelper {


    public static final String DATABASE_NAME = "yaDb";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_CARDS = "cards";

    //TABLE_ACCOUNT
    public static final String COLUMN_ACCOUNT = "account";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_ACCOUNT_STATUS = "accountStatus";
    public static final String COLUMN_ACCOUNT_TYPE = "accountType";
    public static final String COLUMN_AVATAR_URL = "avUrl";
    public static final String COLUMN_AVATAR_TS = "avTs";
    public static final String COLUMN_BAL_TOTAL = "bdTotal";
    public static final String COLUMN_BAL_AVAILABLE = "bdAvailable";
    public static final String COLUMN_BAL_DEPOSITION_PENDING = "bdDepositionPending";
    public static final String COLUMN_BAL_BLOCKED = "bdBlocked";
    public static final String COLUMN_BAL_DEBDT = "bdDebt";
    public static final String COLUMN_BAL_HOLD = "bdHold";

    //TABLE_CARDS
    public static final String COLUMN_PAN_FRAGMENT = "panFragment";
    public static final String COLUMN_TYPE = "type";



    public static final String[] ACCOUNT_TEXT_KEYS = new String[]{
            COLUMN_ACCOUNT,
            COLUMN_CURRENCY,
            COLUMN_ACCOUNT_STATUS,
            COLUMN_AVATAR_URL,
            COLUMN_ACCOUNT_TYPE
    };

    public static final String[] ACCOUNT_INT_KEYS = new String[]{
            COLUMN_AVATAR_TS
    };

    public static final String[] ACCOUNT_REAL_KEYS = new String[]{
            COLUMN_BALANCE,
            COLUMN_BAL_AVAILABLE,
            COLUMN_BAL_TOTAL,
            COLUMN_BAL_BLOCKED,
            COLUMN_BAL_DEBDT,
            COLUMN_BAL_DEPOSITION_PENDING,
            COLUMN_BAL_HOLD
    };

    public static final String[] CARDS_TEXT_KEYS = new String[]{COLUMN_PAN_FRAGMENT, COLUMN_TYPE};


    public AccountsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(getCreateTableQuest(TABLE_ACCOUNT, ACCOUNT_TEXT_KEYS, ACCOUNT_INT_KEYS,
                ACCOUNT_REAL_KEYS));
        database.execSQL(getCreateTableQuest(TABLE_CARDS, CARDS_TEXT_KEYS));

    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(AccountsDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        if (oldVersion <= 1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);

        }

        onCreate(db);
    }

}
