package com.klgleb.yandexmoney.account;

import com.klgleb.yandexmoney.model.AccessTokenModel;
import com.klgleb.yandexmoney.model.AccountInfo;
import com.klgleb.yandexmoney.model.OperationHistory;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

/**
 * Created by klgleb on 20.11.15.
 */
public interface YaMoneyApi {

    @GET("/oauth/token")
    Call<AccessTokenModel> getAccessToken(
            @Query("code") String code,
            @Query("client_id") String clientId,
            @Query("grant_type") String grantType,
            @Query("redirect_uri") String redirectUri

    );

    @GET("/api/account-info")
    Call<AccountInfo> getAccountInfo(@Header("Authorization") String token);

    @GET("/api/operation-history")
    Call<OperationHistory> getOperationHistory(@Header("Authorization") String token);




}

