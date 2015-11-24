package com.klgleb.yandexmoney.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.klgleb.yandexmoney.db.AccountContentProvider;
import com.klgleb.yandexmoney.account.YaAuthenticator;
import com.klgleb.yandexmoney.account.YaMoneyApi;
import com.klgleb.yandexmoney.db.AccountsDBHelper;
import com.klgleb.yandexmoney.model.AccountInfo;
import com.klgleb.yandexmoney.model.CardLinked;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Created by klgleb on 23.11.15.
 */
public class AccountInfoSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String TAG = "AccountInfoSyncAdapter";
    private final AccountManager accountManager;

    public AccountInfoSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        accountManager = AccountManager.get(context);

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {


            String authToken = accountManager.blockingGetAuthToken(account, YaAuthenticator.TOKEN_TYPE, true);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(YaAuthenticator.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            YaMoneyApi service = retrofit.create(YaMoneyApi.class);


            Call<AccountInfo> accountInfo = service.getAccountInfo("Bearer " + authToken);
            Response<AccountInfo> execute = accountInfo.execute();
            AccountInfo body = execute.body();

            ContentValues values = new ContentValues();
            values.put(AccountsDBHelper.COLUMN_ACCOUNT, body.getAccount());
            values.put(AccountsDBHelper.COLUMN_ACCOUNT_STATUS, body.getAccountStatus());
            values.put(AccountsDBHelper.COLUMN_ACCOUNT_TYPE, body.getAccountType());
            values.put(AccountsDBHelper.COLUMN_BALANCE, body.getBalance());

            /*float minX = 50.0f;
            float maxX = 100.0f;

            Random rand = new Random();

            float finalX = rand.nextFloat() * (maxX - minX) + minX;

            values.put(AccountsDBHelper.COLUMN_BALANCE, finalX);*/

            values.put(AccountsDBHelper.COLUMN_CURRENCY, body.getCurrency());

            if (body.getAvatar() != null) {
                values.put(AccountsDBHelper.COLUMN_AVATAR_URL, body.getAvatar().getUrl());
                values.put(AccountsDBHelper.COLUMN_AVATAR_TS, body.getAvatar().getTs());
            }

            if (body.getBalanceDetails() != null) {
                values.put(AccountsDBHelper.COLUMN_BAL_AVAILABLE, body.getBalanceDetails().getAvailable());
                values.put(AccountsDBHelper.COLUMN_BAL_BLOCKED, body.getBalanceDetails().getBlocked());
                values.put(AccountsDBHelper.COLUMN_BAL_DEBDT, body.getBalanceDetails().getDebt());
                values.put(AccountsDBHelper.COLUMN_BAL_DEPOSITION_PENDING, body.getBalanceDetails().getDepositionPending());
                values.put(AccountsDBHelper.COLUMN_BAL_HOLD, body.getBalanceDetails().getHold());
                values.put(AccountsDBHelper.COLUMN_BAL_TOTAL, body.getBalanceDetails().getTotal());

            }



            provider.delete(AccountContentProvider.ACCOUNT_CONTENT_URI, null, null);
            provider.delete(AccountContentProvider.CARDS_CONTENT_URI, null, null);

            Uri uri = provider.insert(AccountContentProvider.ACCOUNT_CONTENT_URI, values);
            if (uri != null) Log.d(TAG, "inserted to " + uri.toString());

            if (body.getCardLinkeds() != null) {
                for (CardLinked card : body.getCardLinkeds()) {
                    ContentValues cardValues = new ContentValues();
                    cardValues.put(AccountsDBHelper.COLUMN_PAN_FRAGMENT, card.getPanFragment());
                    cardValues.put(AccountsDBHelper.COLUMN_TYPE, card.getType());

                    provider.insert(AccountContentProvider.CARDS_CONTENT_URI, cardValues);
                }
            }


        } catch (Throwable throwable) {
            Log.w(TAG, throwable);
        }
    }


}
