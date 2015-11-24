package com.klgleb.yandexmoney.tasks;

import com.klgleb.yandexmoney.account.YaAuthenticator;
import com.klgleb.yandexmoney.account.YaMoneyApi;
import com.klgleb.yandexmoney.model.AccessTokenModel;

import org.androidannotations.annotations.EBean;

import java.io.IOException;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Task для получения accessToken
 * <p/>
 * Created by klgleb on 02.11.15.
 */
@EBean
public class GetAccessTokenTask extends BaseTask<AccessTokenModel> {


    private String code;
    private String clientId;
    private String grantType;
    private String redirectUri;


    public void prepare(String code, String clientId, String grantType, String redirectUri) {
        this.code = code;
        this.clientId = clientId;
        this.grantType = grantType;
        this.redirectUri = redirectUri;
    }


    @Override
    AccessTokenModel getData() {
        Retrofit restAdapter =  new Retrofit.Builder()
                .baseUrl(YaAuthenticator.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YaMoneyApi api =  restAdapter.create(YaMoneyApi.class);

        Call<AccessTokenModel> accessToken = api.getAccessToken(code, clientId, grantType, redirectUri);
        Response<AccessTokenModel> execute;
        try {
            execute = accessToken.execute();
            return execute.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public void cacheData(AccessTokenModel data) {
    }
}
