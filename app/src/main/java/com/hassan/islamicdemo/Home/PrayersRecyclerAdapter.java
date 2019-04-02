package com.hassan.islamicdemo.Home;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hassan.islamicdemo.Base.App;
import com.hassan.islamicdemo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrayersRecyclerAdapter extends RecyclerView.Adapter<PrayersRecyclerAdapter.PrayerViewHolder> {

    Context context;
    List<PrayerTime> prayerTimes = new ArrayList<>();

    public PrayersRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setPrayerTimes(List<PrayerTime> times) {
        this.prayerTimes = times;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrayerViewHolder(LayoutInflater.from(context).inflate(R.layout.prayer_row, parent, false));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull PrayerViewHolder holder, int position) {
        PrayerTime time = prayerTimes.get(position);
        if (time != null) {
            holder.txtName.setText(time.getPrayer_name());
            holder.txtTime.setText(time.getTime());
            if (time.getAlarmSet())
                holder.imgAlarmSet.setImageResource(R.drawable.ic_alarm_off_black_24dp);
            else
                holder.imgAlarmSet.setImageResource(R.drawable.ic_alarm_on_black_24dp);
            holder.imgAlarmSet.setOnClickListener(view -> {
                time.setAlarmSet(!time.getAlarmSet());
                if (time.getAlarmSet()) {
                    holder.imgAlarmSet.setImageResource(R.drawable.ic_alarm_off_black_24dp);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MINUTE,Integer.parseInt(time.getTime().split(":")[1]));
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.getTime().split(":")[0]));
                    Intent intent = new Intent("my_app_alarm_receiver");
                    intent.putExtra("id",time.getId().toString());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(time.getId().toString()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    long startupTime = calendar.getTimeInMillis();
                    if (System.currentTimeMillis() > startupTime){
                        startupTime = startupTime + 24*60*60*1000;
                    }
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,startupTime, pendingIntent);
                    Toast.makeText(context, "تم ضبط المنبه على " + time.getTime(), Toast.LENGTH_SHORT).show();
                } else {
                    holder.imgAlarmSet.setImageResource(R.drawable.ic_alarm_on_black_24dp);
                    Intent intent = new Intent();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(time.getId().toString()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(context, "تم الغاء المنبه", Toast.LENGTH_SHORT).show();
                }
                ((App) context.getApplicationContext()).getDaoSession().getPrayerTimeDao().update(time);
            });
        }
    }

    @Override
    public int getItemCount() {
        return prayerTimes.size();
    }

    public static class PrayerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_prayer_name)
        TextView txtName;

        @BindView(R.id.txt_prayer_time)
        TextView txtTime;

        @BindView(R.id.img_prayer_alarm_set)
        ImageView imgAlarmSet;

        public PrayerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
