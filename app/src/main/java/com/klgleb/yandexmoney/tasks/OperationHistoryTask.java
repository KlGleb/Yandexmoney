package com.klgleb.yandexmoney.tasks;

import com.klgleb.yandexmoney.account.YaMoneyApi;
import com.klgleb.yandexmoney.model.OperationHistory;

import org.androidannotations.annotations.EBean;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Getting a list of hotels from server
 * <p/>
 * Created by klgleb on 02.11.15.
 */
@EBean
public class OperationHistoryTask extends ApiBaseTask<OperationHistory> {

    @Override
    OperationHistory getData() {

        String accessToken = getAccessToken();
        if(accessToken == null) {
            return null;
        }

        YaMoneyApi apiService = getApiService();

        Call<OperationHistory> resp = apiService.getOperationHistory("Bearer " + accessToken);


        Response<OperationHistory> execute = null;
        try {
            execute = resp.execute();
            return execute.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void cacheData(OperationHistory data) {
        return;
    }
}
