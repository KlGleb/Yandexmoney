package com.klgleb.yandexmoney.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by klgleb on 20.11.15.
 */
public class AccessTokenModel {

    @SerializedName("access_token")
    private String accessToken;

    private String error;

    public AccessTokenModel(String accessToken, String error) {
        this.accessToken = accessToken;
        this.error = error;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
