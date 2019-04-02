package com.hassan.islamicdemo.Home;

import android.content.Context;

import java.util.List;

public class HomePresenterImpl implements HomePresenter, HomeInteractor.Callback {

    private Context context;
    private HomeView view;
    private HomeInteractor interactor;

    public HomePresenterImpl(Context context, HomeView view, HomeInteractor interactor) {
        this.context = context;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void getPrayerTimes() {
        interactor.getPrayerTimes(context, this);
    }

    @Override
    public void onSuccess(List<PrayerTime> times) {
        view.showPrayerTimes(times);
    }

    @Override
    public void onError(String err) {
        view.showError(err);
    }
}