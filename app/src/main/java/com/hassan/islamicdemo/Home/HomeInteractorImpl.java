package com.hassan.islamicdemo.Home;

import android.content.Context;
import android.preference.PreferenceManager;

import com.hassan.islamicdemo.Base.App;

import java.util.List;


public class HomeInteractorImpl implements HomeInteractor {

    @Override
    public void getPrayerTimes(Context context, Callback callback) {
        List<PrayerTime> times = ((App) context.getApplicationContext()).getDaoSession().getPrayerTimeDao().loadAll();
        callback.onSuccess(times);
        callback.onGDate(PreferenceManager.getDefaultSharedPreferences(context).getString("g_date",""));
        callback.onHDate(PreferenceManager.getDefaultSharedPreferences(context).getString("h_date",""));
    }

}