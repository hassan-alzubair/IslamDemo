package com.hassan.islamicdemo.Home;

import java.util.List;

public interface HomeView {

    void showPrayerTimes(List<PrayerTime> times);

    void showError(String err);
}
