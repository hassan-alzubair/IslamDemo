package com.hassan.islamicdemo.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.hassan.islamicdemo.Base.BaseActivity;
import com.hassan.islamicdemo.PrayersService.PrayersService;
import com.hassan.islamicdemo.R;
import com.hassan.islamicdemo.Utils.AppConstants;
import com.hassan.islamicdemo.Utils.GpsUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements HomeView {

    private boolean isContinue = false;
    private boolean isGPS = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @BindView(R.id.g_date)
    TextView txtGDate;

    @BindView(R.id.h_date)
    TextView txtHDate;

    @BindView(R.id.prayer_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.txt_location)
    TextView txtLocation;

    private PrayersRecyclerAdapter adapter;
    private HomePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("أوقات الصلوات");

        ButterKnife.bind(this);

        presenter = new HomePresenterImpl(this, this, new HomeInteractorImpl());
        adapter = new PrayersRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        new GpsUtils(this).turnGPSOn(isGPSEnable -> {
            isGPS = isGPSEnable;
        });

        txtLocation.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("address", ""));

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
        presenter.getPrayerTimes();
        getLocation();
        if (!isServiceRunning(PrayersService.class)) {
            startService(new Intent(this, PrayersService.class));
        }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
                            if (location != null) {
                                wayLatitude = location.getLatitude();
                                wayLongitude = location.getLongitude();
                                saveLocation(wayLatitude, wayLongitude);
                            } else {
                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void saveLocation(double wayLatitude, double wayLongitude) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("location", wayLatitude + "," + wayLongitude).apply();
    }

    @Override
    public void showPrayerTimes(List<PrayerTime> times) {
        adapter.setPrayerTimes(times);
    }

    @Override
    public void showGDate(String date) {
        txtGDate.setText(date);
    }

    @Override
    public void showHDate(String date) {
        txtHDate.setText(date);
    }

    @Override
    public void showError(String err) {
        new AlertDialog.Builder(this)
                .setMessage(err)
                .setNeutralButton("موافق", null)
                .show();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<PrayerTime> times = intent.getExtras().getParcelableArrayList("times");
            showPrayerTimes(times);
            txtGDate.setText(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("g_date", ""));
            txtHDate.setText(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("h_date", ""));
            txtLocation.setText(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("address", ""));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("change_times_receiver"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private boolean isServiceRunning(Class<?> clazz) {
        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (clazz.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}