package com.klgleb.yandexmoney.tasks;

import android.content.Context;
import android.util.Log;


import com.klgleb.yandexmoney.R;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;

/**
 * Base task for geeting data from server
 *
 * Created by klgleb on 02.11.15.
 */
@EBean
public abstract class BaseTask<DataType> {
    public static final String TAG = "BaseTask";

    @RootContext
    Context context;

    @StringRes(R.string.connection_error)
    String connectionErrorMessage;

    private boolean loading = false;

    abstract DataType getData();

    public abstract void cacheData(DataType data);

    @Background
    public void doRequest(){

        getCallback().setRefreshing(true);
        loading = true;

        DataType data;

        try {
            data = getData();
        } catch (Throwable throwable) {

            Log.w(TAG, throwable);
            Log.d(TAG, "loading = false");

            loading = false;
            onError(connectionErrorMessage);
            return;
        }

        if (data != null) {

            updateUI(data);
            cacheData(data);
        } else {
            loading = false;
            onError(connectionErrorMessage);
        }

    }

    @UiThread
    void updateUI(DataType data  ) {
        Log.d(TAG, "Updating UI...");


        getCallback().displayData(data);
        getCallback().setRefreshing(false);

        Log.d(TAG, "loading = false");
        loading = false;
    }




    @UiThread
    void onError(String errorMsg) {
        Log.d(TAG, "trace error " + errorMsg);

        getCallback().onError(errorMsg);
        getCallback().setRefreshing(false);
    }

    @SuppressWarnings("unchecked")
    private ITaskCallback<DataType> getCallback(){
        return (ITaskCallback<DataType>) context;
    }


    public boolean isLoading() {
        return loading;
    }

}
