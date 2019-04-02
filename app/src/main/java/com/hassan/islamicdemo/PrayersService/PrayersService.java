package com.hassan.islamicdemo.PrayersService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.hassan.islamicdemo.Base.App;
import com.hassan.islamicdemo.Home.MainActivity;
import com.hassan.islamicdemo.Home.PrayerTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;


public class PrayersService extends Service implements PrayersView {

    private boolean isContinue = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LocationManager locationManager;


    private static final long INTERVAL = 2 * 60 * 60 * 1000;
    private boolean serviceRunning = true;
    private PrayersPresenter presenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        if (!isContinue) {
                            saveLocation(wayLatitude, wayLongitude);
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };


        presenter = new PrayersPresenterImpl(new PrayersInteractorImpl(), this);
        new Thread(() -> {
            while (serviceRunning) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                if (preferences.getString("location", null) != null) {
                    String coords = preferences.getString("location", "0.0,0.0");
                    double lat = Double.parseDouble(coords.split(",")[0]);
                    double lng = Double.parseDouble(coords.split(",")[1]);
                    presenter.getPrayers(lat, lng, 4);
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

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        saveLocation(wayLatitude, wayLongitude);
                    } else {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
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
        Collections.sort(arrayList, (prayerTime, t1) -> (Integer.parseInt(t1.getId().toString())) < (Integer.parseInt(prayerTime.getId().toString())) ? 0:-1);
        intent.putParcelableArrayListExtra("times", arrayList);
        sendBroadcast(intent);
    }

    @Override
    public void onError(String err) {

    }
}