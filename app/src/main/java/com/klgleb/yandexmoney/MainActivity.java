package com.klgleb.yandexmoney;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.klgleb.yandexmoney.account.YaAuthenticator;
import com.klgleb.yandexmoney.db.AccountContentProvider;
import com.klgleb.yandexmoney.db.AccountsDBHelper;
import com.klgleb.yandexmoney.model.Operation;
import com.klgleb.yandexmoney.model.OperationHistory;
import com.klgleb.yandexmoney.tasks.ITaskCallback;
import com.klgleb.yandexmoney.tasks.OperationHistoryTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        ITaskCallback<OperationHistory> {
    public static final int REQUEST_AUTH = 230;
    public static final String TAG = "MainActivity";
    private static final int LOADER_ID = 0x01;

    @ViewById
    Toolbar toolbar;

    @ViewById
    Button syncBtn;

    @ViewById
    Button requestBtn;

    @ViewById
    TextView balanceText;

    @ViewById
    TextView historyTxt;

    @ViewById
    ProgressBar progressBar;

    @Bean
    @NonConfigurationInstance
    OperationHistoryTask task;

    @NonConfigurationInstance
    OperationHistory operationHistory;

    @AfterViews
    void afterViews() {
        setSupportActionBar(toolbar);
        refrButtons();

        getLoaderManager().initLoader(LOADER_ID, null, this);
        setRefreshing(task.isLoading());

        displayData(operationHistory);
    }

    @UiThread
    void refrButtons() {
        Account account = getAccount();
        syncBtn.setEnabled(account != null);
        requestBtn.setEnabled(account != null);

    }

    @Click
    void authBtn() {
        showLogin();
    }

    @Click
    void syncBtn() {
        Account account = getAccount();

        if (account == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(account, AccountContentProvider.AUTHORITY, bundle);
    }

    @Click
    void requestBtn() {

        task.doRequest();
    }



    @UiThread
    void showLogin() {
        Intent intent = AccountManager.newChooseAccountIntent(null, null,
                new String[]{YaAuthenticator.ACCOUNT_TYPE}, true, null, null,
                null, null);


        startActivityForResult(intent, REQUEST_AUTH);

    }

    @OnActivityResult(REQUEST_AUTH)
    void onResult() {
        refrButtons();
        syncBtn();
    }


    @Override
    protected void onResume() {
        super.onResume();
        refrButtons();
        getLoaderManager().getLoader(LOADER_ID).forceLoad();
    }


    Account getAccount() {
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(YaAuthenticator.ACCOUNT_TYPE);

        if (accounts.length == 0) {
            return null;
        }

        return accounts[0];
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getApplicationContext(),
                AccountContentProvider.ACCOUNT_CONTENT_URI,
                null,
                null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");

        data.moveToFirst();
        if (data.getCount() > 0 && getAccount() != null) {
            float balance = data.getFloat(data
                    .getColumnIndex(AccountsDBHelper.COLUMN_BALANCE));

            balanceText.setText(String.format(getString(R.string.your_balance), balance));
        } else {
            balanceText.setText(R.string.data_not_found);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        balanceText.setText(R.string.data_not_found);
    }


    @Override
    @UiThread
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    @UiThread
    public void onError(String message) {
        historyTxt.setText(message);
    }

    @Override
    @UiThread
    public void displayData(OperationHistory data) {
        if (data != null) {
            this.operationHistory = data;
        }

        String s = "";

        if (operationHistory != null &&  operationHistory.getOperations() != null) {
            for (Operation operation : operationHistory.getOperations()) {
                s += String.format("%s: #%s -- %s   %.2f",
                        operation.getDatetime(),
                        operation.getOperaitonId(),
                        operation.getTitle(),
                        operation.getAmount());
                s += "\n";
            }
        }


        historyTxt.setText(s);
    }
}
