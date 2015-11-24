package com.klgleb.yandexmoney.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by klgleb on 21.11.15.
 */
public class YaAuthenticatorService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        YaAuthenticator authenticator = new YaAuthenticator(this);
        return authenticator.getIBinder();
    }
}
