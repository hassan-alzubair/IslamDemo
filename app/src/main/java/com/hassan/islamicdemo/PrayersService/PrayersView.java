package com.hassan.islamicdemo.PrayersService;

import com.hassan.islamicdemo.Home.PrayerTime;

import java.util.List;

public interface PrayersView {
    void savePrayers(List<PrayerTime> times);

    void saveHDate(String date);

    void saveGDate(String date);

    void saveAddress(String address);

    void onError(String err);
}
