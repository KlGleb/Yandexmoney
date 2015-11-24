package com.klgleb.yandexmoney.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by klgleb on 24.11.15.
 */
public class OperationHistory {
    String error;

    @SerializedName("next_record")
    String nextRecord;

    List<Operation> operations;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getNextRecord() {
        return nextRecord;
    }

    public void setNextRecord(String nextRecord) {
        this.nextRecord = nextRecord;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}


