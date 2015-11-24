package com.klgleb.yandexmoney.tasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.klgleb.yandexmoney.account.YaAuthenticator;
import com.klgleb.yandexmoney.account.YaMoneyApi;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by klgleb on 24.11.15.
 */
@EBean
abstract class ApiBaseTask<T> extends BaseTask<T> {



    String getAccessToken() {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(YaAuthenticator.ACCOUNT_TYPE);

        if (accounts.length == 0) {
            return null;
        }

        Account account = accounts[0];

        try {
            String authToken = accountManager.blockingGetAuthToken(account, YaAuthenticator.ACCOUNT_TYPE, true);

            return authToken;
        } catch (Throwable throwable) {
            Log.w(TAG, throwable);
            return null;
        }
    }

    YaMoneyApi getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YaAuthenticator.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        YaMoneyApi service = retrofit.create(YaMoneyApi.class);
        return service;
    }
}
