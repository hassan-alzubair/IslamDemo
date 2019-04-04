package com.hassan.islamicdemo.Home;

import java.util.List;

public interface HomeView {

    void showPrayerTimes(List<PrayerTime> times);

    void showGDate(String date);

    void showHDate(String date);

    void showError(String err);
}
