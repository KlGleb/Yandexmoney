package com.klgleb.yandexmoney.tasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.util.Log;

import com.klgleb.yandexmoney.account.YaAuthenticator;
import com.klgleb.yandexmoney.account.YaMoneyApi;

import org.androidannotations.annotations.EBean;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Таск для запросов к api, дополненный методами для получения токена
 * и Retrofit-сервиса
 *
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

            return accountManager.blockingGetAuthToken(account, YaAuthenticator.ACCOUNT_TYPE, true);
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
        return retrofit.create(YaMoneyApi.class);
    }
}
