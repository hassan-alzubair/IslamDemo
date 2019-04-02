package com.hassan.islamicdemo.PrayersService;

import com.hassan.islamicdemo.Home.PrayerTime;

import java.util.ArrayList;
import java.util.List;

public class TestPrayersInteractor implements PrayersInteractor {

    @Override
    public void getPrayers(double latitude, double longitude, int method, Callback callback) {
        List<PrayerTime> times = new ArrayList<>();
        times.add(new PrayerTime(2L, PrayerTime.TAG_FAJR, PrayerTime.NAME_FAJR, "20:19", false));
        times.add(new PrayerTime(1L, PrayerTime.TAG_IMSAK, PrayerTime.NAME_IMSAK, "10:10", false));
        times.add(new PrayerTime(5L, PrayerTime.TAG_ASR, PrayerTime.NAME_ASR, "10:10", false));
        times.add(new PrayerTime(3L, PrayerTime.TAG_SUNRISE, PrayerTime.NAME_SUNRISE, "10:10", false));
        times.add(new PrayerTime(6L, PrayerTime.TAG_SUNSET, PrayerTime.NAME_SUNSET, "10:10", false));
        times.add(new PrayerTime(7L, PrayerTime.TAG_MAGHRIB, PrayerTime.NAME_MAGHRIB, "10:10", false));
        times.add(new PrayerTime(8L, PrayerTime.TAG_ISHA, PrayerTime.NAME_ISHA, "10:10", false));
        times.add(new PrayerTime(4L, PrayerTime.TAG_THUHR, PrayerTime.NAME_THUHR, "10:10", false));
        callback.onSuccess(times);
    }
}
