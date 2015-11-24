package com.klgleb.yandexmoney.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service for AccountInfoSyncAdapter
 *
 * Created by klgleb on 23.11.15.
 */

public class SyncService extends Service {

    private static AccountInfoSyncAdapter sSyncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sSyncAdapter == null) {
            sSyncAdapter = new AccountInfoSyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }

}