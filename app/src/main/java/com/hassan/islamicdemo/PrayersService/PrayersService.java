package com.hassan.islamicdemo.PrayersService;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hassan.islamicdemo.Base.App;
import com.hassan.islamicdemo.Home.PrayerTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.hassan.islamicdemo.Utils.AppConstants.TAG;


public class PrayersService extends Service implements PrayersView {

    private LocationManager locationManager;

    private static final long INTERVAL = 30 * 1000;//2 * 60 * 60 * 1000;
    private boolean serviceRunning = true;
    private PrayersPresenter presenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        presenter = new PrayersPresenterImpl(new PrayersInteractorImpl(), this);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        new Thread(() -> {
            while (serviceRunning) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation(location -> {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        presenter.getPrayers(lat, lng, 4);
                    });
                } else {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    if (preferences.getString("location", null) != null) {
                        String coords = preferences.getString("location", "0.0,0.0");
                        double lat = Double.parseDouble(coords.split(",")[0]);
                        double lng = Double.parseDouble(coords.split(",")[1]);
                        presenter.getPrayers(lat, lng, 4);
                    }
                }
                try {
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return START_STICKY;
    }

    private void saveLocation(double wayLatitude, double wayLongitude) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("location", wayLatitude + "," + wayLongitude).apply();
    }

    @SuppressLint("MissingPermission")
    private void getLocation(LocationFetchedListener listener) {
        Log.d(TAG, "getLocation: ");
        if (locationManager != null) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                saveLocation(location.getLatitude(), location.getLongitude());
                if (listener != null) {
                    listener.onLocationFetched(location);
                }
            } else {
                LocationListener mListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            saveLocation(location.getLatitude(), location.getLongitude());
                            if (listener != null) {
                                listener.onLocationFetched(location);
                            }
                        }
                        locationManager.removeUpdates(this);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                };
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mListener, Looper.getMainLooper());
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("NewApi")
    @Override
    public void savePrayers(List<PrayerTime> times) {
        ArrayList<PrayerTime> arrayList = new ArrayList<>();
        for (PrayerTime time : times) {
            PrayerTime t = ((App) getApplication()).getDaoSession().getPrayerTimeDao().queryRaw("where tag = ?", time.getTag()).get(0);
            t.setTime(time.getTime());
            ((App) getApplication()).getDaoSession().getPrayerTimeDao().update(t);
            arrayList.add(t);
            if (t.getAlarmSet()) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t.getTime().split(":")[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(t.getTime().split(":")[1]));
                Intent intent = new Intent("my_app_alarm_receiver");
                intent.putExtra("id", t.getId().toString());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(t.getId().toString()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                long startupTime = calendar.getTimeInMillis();
                if (System.currentTimeMillis() > startupTime) {
                    startupTime = startupTime + 24 * 60 * 60 * 1000;
                }
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, startupTime, pendingIntent);
            }
        }
        Intent intent = new Intent("change_times_receiver");
        Collections.sort(arrayList, (prayerTime, t1) -> (Integer.parseInt(t1.getId().toString())) < (Integer.parseInt(prayerTime.getId().toString())) ? 0 : -1);
        intent.putParcelableArrayListExtra("times", arrayList);
        sendBroadcast(intent);
        Log.d(TAG, "change_times_receiver");
    }

    @Override
    public void saveHDate(String date) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("h_date", date).apply();
    }

    @Override
    public void saveGDate(String date) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("g_date", date).apply();
    }

    @Override
    public void saveAddress(String address) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("address",address).apply();
    }

    @Override
    public void onError(String err) {

    }


    interface LocationFetchedListener {
        void onLocationFetched(Location location);
    }
}