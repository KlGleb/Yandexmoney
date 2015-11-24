package com.klgleb.yandexmoney.model;

/**
 * panFragment	string	Маскированный номер карты.
 type	string	Тип карты. Может отсутствовать, если неизвестен. Возможные значения:
 VISA;
 MasterCard;
 AmericanExpress;
 JCB.
 * Created by klgleb on 21.11.15.
 */
public class CardLinked {
    String panFragment;
    String type;

    public CardLinked(String panFragment, String type) {
        this.panFragment = panFragment;
        this.type = type;
    }

    public String getPanFragment() {
        return panFragment;
    }

    public void setPanFragment(String panFragment) {
        this.panFragment = panFragment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
