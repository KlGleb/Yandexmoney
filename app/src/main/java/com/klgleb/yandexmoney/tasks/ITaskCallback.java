package com.klgleb.yandexmoney.tasks;

/**
 * Interface for callback from BaseTask
 *
 * Created by klgleb on 02.11.15.
 */
public interface ITaskCallback<DataType> {

    void setRefreshing(boolean refreshing);

    void onError(String message);

    void displayData(DataType data);
}
