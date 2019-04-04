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
    public void getDates() {

    }

    @Override
    public void onSuccess(List<PrayerTime> times) {
        view.showPrayerTimes(times);
    }

    @Override
    public void onGDate(String date) {
        view.showGDate(date);
    }

    @Override
    public void onHDate(String date) {
        view.showHDate(date);
    }

    @Override
    public void onError(String err) {
        view.showError(err);
    }
}