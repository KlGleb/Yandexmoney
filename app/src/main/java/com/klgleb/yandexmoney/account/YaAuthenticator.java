package com.klgleb.yandexmoney.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.klgleb.yandexmoney.LoginActivity;
import com.klgleb.yandexmoney.LoginActivity_;

import static android.accounts.AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE;

/**
 * Created by klgleb on 20.11.15.
 */
public class YaAuthenticator extends AbstractAccountAuthenticator {

    public static final String TAG = "YaAuthenticator";
    public static final String CLIENT_ID = "1EF9B6D1713878ED1FDC348956DA4CC44B059B67AF81126C0C15ADE0CC28BB2F";
    public static final String SERVER_URL = "https://money.yandex.ru";
    public static final String REDIRECT_URI = "http://hello200-a.akamaihd.net/redirect_uri";
    public static final String ACCOUNT_TYPE = "com.klgleb.ya_auth";
    public static final String TOKEN_TYPE = "com.klgleb.ya_auth";
    public static final String SCOPE = "account-info operation-history";


    private final Context context;

    public YaAuthenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response,
                             String accountType, String authTokenType, String[] requiredFeatures,
                             Bundle options) throws NetworkErrorException {


        final Bundle bundle = new Bundle();

        if (options != null) {
            bundle.putAll(options);
        }

        bundle.putParcelable(AccountManager.KEY_INTENT, createLoginIntent(response));

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        final Bundle bundle = new Bundle();

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(context);

        String authToken = am.peekAuthToken(account, authTokenType);

        // Lets give another try to authenticate the user
      /*  if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
//                authToken = sServerAuthenticate.userSignIn(account.name, password, authTokenType);
            }
        }
*/
        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.

        bundle.putParcelable(AccountManager.KEY_INTENT, createLoginIntent(response));
        return bundle;
    }


    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

    private Intent createLoginIntent(final AccountAuthenticatorResponse response) {

        final Intent intent = LoginActivity_.intent(context)
                .clientId(CLIENT_ID)
                .redirectUri(REDIRECT_URI)
                .scope(SCOPE)
                .serverUrl(SERVER_URL + "/oauth/authorize")
                .responseType("code")
                .get();

        intent.putExtra(KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);


        return intent;
    }
}
