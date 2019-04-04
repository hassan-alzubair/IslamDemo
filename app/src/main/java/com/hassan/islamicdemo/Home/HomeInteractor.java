package com.hassan.islamicdemo.Home;

import android.content.Context;

import java.util.List;

public interface HomeInteractor {

    interface Callback{
        void onSuccess(List<PrayerTime> times);
        void onGDate(String date);
        void onHDate(String date);
        void onError(String err);
    }

    void getPrayerTimes(Context context,Callback callback);
}
