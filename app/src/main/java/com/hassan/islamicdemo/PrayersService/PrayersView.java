package com.hassan.islamicdemo.PrayersService;

import com.hassan.islamicdemo.Home.PrayerTime;

import java.util.List;

public interface PrayersView {
    void savePrayers(List<PrayerTime> times);
    void onError(String err);
}
