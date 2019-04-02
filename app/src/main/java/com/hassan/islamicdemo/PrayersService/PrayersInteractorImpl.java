package com.hassan.islamicdemo.PrayersService;

import com.hassan.islamicdemo.Base.ApiClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrayersInteractorImpl implements PrayersInteractor, Callback<ResponseBody> {

    private Callback callback;

    @Override
    public void getPrayers(double latitude, double longitude, int method, Callback callback) {
        this.callback = callback;
        ApiClient.getAladhanApiClient().create(PrayersApiService.class).getPrayersTimes(latitude, longitude, method)
                .enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            callback.onSuccess(PrayersTimesJsonParser.parse(new JSONObject(response.body().string())));
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        callback.onError(t.getMessage());
    }
}
