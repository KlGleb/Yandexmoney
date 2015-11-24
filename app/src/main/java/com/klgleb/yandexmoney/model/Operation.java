package com.klgleb.yandexmoney.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by klgleb on 24.11.15.
 */
public class Operation {
    @SerializedName("operation_id")
    String operaitonId;

    String status;
    String datetime;
    String title;

    @SerializedName("pattern_id")
    String patternId;

    float amount;
    String direction;
    String label;
    String type;

    public Operation(String operaitonId, String status, String datetime, String title,
                     String patternId, float amount, String direction, String label, String type) {
        this.operaitonId = operaitonId;
        this.status = status;
        this.datetime = datetime;
        this.title = title;
        this.patternId = patternId;
        this.amount = amount;
        this.direction = direction;
        this.label = label;
        this.type = type;
    }

    public String getOperaitonId() {
        return operaitonId;
    }

    public void setOperaitonId(String operaitonId) {
        this.operaitonId = operaitonId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

