package com.klgleb.yandexmoney.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by klgleb on 21.11.15.
 */
public class AccountInfo {

    String account;
    float balance;
    String currency;

    @SerializedName("account_status")
    String accountStatus;


    @SerializedName("account_type")
    String accountType;

    AccountAvatar avatar;

    @SerializedName("balance_details")
    BalanceDetails balanceDetails;

    @SerializedName("cards_linked")
    List<CardLinked> cardLinkeds;

    public AccountInfo(String account,
                       float balance,
                       String currency,
                       String accountStatus,
                       String accountType,
                       AccountAvatar avatar,
                       BalanceDetails balanceDetails,
                       List<CardLinked> cardLinkeds) {
        this.account = account;
        this.balance = balance;
        this.currency = currency;
        this.accountStatus = accountStatus;
        this.accountType = accountType;
        this.avatar = avatar;
        this.balanceDetails = balanceDetails;
        this.cardLinkeds = cardLinkeds;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public AccountAvatar getAvatar() {
        return avatar;
    }

    public void setAvatar(AccountAvatar avatar) {
        this.avatar = avatar;
    }

    public BalanceDetails getBalanceDetails() {
        return balanceDetails;
    }

    public void setBalanceDetails(BalanceDetails balanceDetails) {
        this.balanceDetails = balanceDetails;
    }

    public List<CardLinked> getCardLinkeds() {
        return cardLinkeds;
    }

    public void setCardLinkeds(List<CardLinked> cardLinkeds) {
        this.cardLinkeds = cardLinkeds;
    }
}
