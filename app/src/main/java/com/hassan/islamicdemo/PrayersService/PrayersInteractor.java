package com.hassan.islamicdemo.PrayersService;

import com.hassan.islamicdemo.Home.PrayerTime;

import java.util.List;

public interface PrayersInteractor {

    interface Callback{
        void onSuccess(List<PrayerTime> times);
        void onSuccessHDate(String hDate);
        void onSuccessGDate(String gDate);
        void onError(String err);
        void onLocation(String location);
    }

    void getPrayers(double latitude,double longitude,int method, Callback callback);
}
