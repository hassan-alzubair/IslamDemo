package com.hassan.islamicdemo.PrayersService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartPrayerServiceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            context.startService(new Intent(context,PrayersService.class));
    }
}
