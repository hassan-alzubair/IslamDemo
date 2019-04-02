package com.hassan.islamicdemo.PrayersService;

import com.hassan.islamicdemo.Home.PrayerTime;

import java.util.List;

public interface PrayersInteractor {

    interface Callback{
        void onSuccess(List<PrayerTime> times);
        void onError(String err);
    }

    void getPrayers(double latitude,double longitude,int method, Callback callback);
}
