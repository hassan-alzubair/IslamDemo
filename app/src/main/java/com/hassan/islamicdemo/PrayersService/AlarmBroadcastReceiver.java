package com.hassan.islamicdemo.PrayersService;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hassan.islamicdemo.Base.App;
import com.hassan.islamicdemo.Home.PrayerTime;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        Long id = Long.parseLong(intent.getStringExtra("id"));
        PrayerTime time = ((App) context.getApplicationContext()).getDaoSession().getPrayerTimeDao().load(id);

        Intent i = new Intent("my_app_alarm_receiver");
        Calendar calendar = Calendar.getInstance();
        intent.putExtra("id", time.getId().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(time.getId().toString()), i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long startupTime = calendar.getTimeInMillis();
        startupTime = startupTime + 24 * 60 * 60 * 1000;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startupTime, pendingIntent);
        Intent intent1 = new Intent(context,AlarmActivity.class);
        intent1.putExtra("time",time);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
