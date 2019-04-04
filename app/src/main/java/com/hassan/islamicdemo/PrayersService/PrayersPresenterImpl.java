package com.hassan.islamicdemo.PrayersService;

import com.hassan.islamicdemo.Home.PrayerTime;

import java.util.List;

public class PrayersPresenterImpl implements PrayersPresenter, PrayersInteractor.Callback {

    private PrayersInteractor interactor;
    private PrayersView view;


    public PrayersPresenterImpl(PrayersInteractor interactor, PrayersView view) {
        this.interactor = interactor;
        this.view = view;
    }

    @Override
    public void getPrayers(double latitude, double longitude, int method) {
        interactor.getPrayers(latitude, longitude, method, this);
    }

    @Override
    public void onSuccess(List<PrayerTime> times) {
        view.savePrayers(times);
    }

    @Override
    public void onSuccessHDate(String hDate) {
        view.saveHDate(hDate);
    }

    @Override
    public void onSuccessGDate(String gDate) {
        view.saveGDate(gDate);
    }

    @Override
    public void onError(String err) {
        view.onError(err);
    }

    @Override
    public void onLocation(String location) {
        view.saveAddress(location);
    }
}