package com.hassan.islamicdemo.PrayersService;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hassan.islamicdemo.Base.BaseActivity;
import com.hassan.islamicdemo.Home.PrayerTime;
import com.hassan.islamicdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmActivity extends BaseActivity {

    @BindView(R.id.txt_alarm_name)
    TextView txtName;

    @BindView(R.id.txt_alarm_time)
    TextView txtTime;
    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm_layout);
        ButterKnife.bind(this);

        PrayerTime time = getIntent().getExtras().getParcelable("time");
        txtName.setText(time.getPrayer_name());
        txtTime.setText(time.getTime());

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, sound);
        ringtone.play();
    }

    public void dismiss(View view) {
        if (ringtone != null && ringtone.isPlaying())
            ringtone.stop();
        finish();
    }
}
