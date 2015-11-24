package com.klgleb.yandexmoney.model;

/**
 * Created by klgleb on 21.11.15.
 */
public class AccountAvatar {
    String url;
    int ts;

    public AccountAvatar(String url, int ts) {
        this.url = url;
        this.ts = ts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }
}
