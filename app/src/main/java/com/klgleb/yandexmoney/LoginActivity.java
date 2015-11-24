package com.klgleb.yandexmoney;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.klgleb.yandexmoney.account.YaAuthenticator;
import com.klgleb.yandexmoney.model.AccessTokenModel;
import com.klgleb.yandexmoney.tasks.GetAccessTokenTask;
import com.klgleb.yandexmoney.tasks.ITaskCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AccountAuthenticatorActivity implements ITaskCallback<AccessTokenModel> {

    private static final String TAG = "LoginActivity";
    public static final String PARAM_CODE = "code";
    public static final String PARAM_ERROR = "error";
    public static final String PARAM_ERROR_DESCRIPTION = "error_description";
    private static final String ARG_IS_ADDING_NEW_ACCOUNT = "arg_is_adding_new_account";


    @ViewById
    WebView webView;

    @Extra
    String clientId;

    @Extra
    String responseType;

    @Extra
    String redirectUri;

    @Extra
    String scope;

    @Extra
    String instanceName;

    @Extra
    String serverUrl;

    @NonConfigurationInstance
    @Bean
    GetAccessTokenTask task;


    private ProgressDialog progressBar;


    AccountManager accountManager;


    @AfterViews
    void afterViews() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

//        progressBar = ProgressDialog.show(LoginActivity.this, "WebView Example", "Loading...");

        if (!task.isLoading()) {

        }

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                setRefreshing(false);

                if (url.contains(redirectUri)) {
                    sendSuccess(url);
                }


            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                sendError("Error: " + description);
            }
        });


        if (serverUrl == null || clientId == null || redirectUri == null) {
            sendError("Error occurred");
            return;
        }

        Uri.Builder builder = Uri.parse(serverUrl).buildUpon()
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUri);

        if (responseType != null) {
            builder.appendQueryParameter("response_type", responseType);
        }

        if (instanceName != null) {
            builder.appendQueryParameter("response_type", responseType);
        }

        if (scope != null && !scope.trim().equals("")) {
            builder.appendQueryParameter("scope", scope);
        }

        String url = builder.build().toString();
        Log.d(TAG, url);

        webView.loadUrl(url);

        accountManager = AccountManager.get(this);

    }

    void sendError(String message) {
        Intent intent = new Intent();

        intent.putExtra(PARAM_ERROR, message);

//        setResult(RESULT_ERROR, intent);
    }

    void sendSuccess(String url) {
        Uri parse = Uri.parse(url);

        Log.d(TAG, parse.toString());


        String code = parse.getQueryParameter(PARAM_CODE);
        String error = parse.getQueryParameter(PARAM_ERROR);
        String errorDescription = parse.getQueryParameter(PARAM_ERROR_DESCRIPTION);

        if (error != null) {
            sendError(error + " " + "\n" + ((errorDescription != null) ? errorDescription : ""));
            return;
        }

        Intent intent = new Intent();

        intent.putExtra(PARAM_CODE, code);


        task.prepare(code, YaAuthenticator.CLIENT_ID, "authorization_code", YaAuthenticator.REDIRECT_URI);
        task.doRequest();


    }


    @Override
    @UiThread
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
           /* if (!progressBar.isShowing()) {
                progressBar = ProgressDialog.show(LoginActivity.this, "WebView Example", "Loading...");
            } else {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }*/

        }


    }

    @Override
    @UiThread
    public void onError(String message) {
//        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

        final Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ERROR_MESSAGE, message);
        setAccountAuthenticatorResult(result);
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    @UiThread
    public void displayData(AccessTokenModel data) {



        final String userName = getString(R.string.yor_account);
        final String userPass = "";

        final Intent intent = new Intent();


        final Account account = new Account(userName, YaAuthenticator.ACCOUNT_TYPE);


        if (accountManager.addAccountExplicitly(account, userPass, new Bundle())) {
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, YaAuthenticator.ACCOUNT_TYPE);
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, data.getAccessToken());
//        intent.putExtra(PARAM_USER_PASS, userPass);
        } else {
            //account already exist
            intent.putExtra(AccountManager.KEY_ERROR_MESSAGE, "account already exist");
        }

        accountManager.setAuthToken(account, YaAuthenticator.TOKEN_TYPE, data.getAccessToken());

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        setRefreshing(false);
        super.onDestroy();
    }
}
