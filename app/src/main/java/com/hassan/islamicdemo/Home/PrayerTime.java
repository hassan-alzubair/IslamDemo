package com.hassan.islamicdemo.Home;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity(nameInDb = "prayers_times")
public class PrayerTime implements Parcelable {

    // tags
    public static final String TAG_IMSAK = "imsak";
    public static final String TAG_FAJR = "fajr";
    public static final String TAG_SUNRISE = "sunrise";
    public static final String TAG_THUHR = "thuhr";
    public static final String TAG_ASR = "asr";
    public static final String TAG_SUNSET = "sunsit";
    public static final String TAG_MAGHRIB = "maghrib";
    public static final String TAG_ISHA = "isha";

    // names
    public static final String NAME_IMSAK = "الإمساك";
    public static final String NAME_FAJR = "الفجر";
    public static final String NAME_SUNRISE = "شروق الشمس";
    public static final String NAME_THUHR = "الظهر";
    public static final String NAME_ASR = "العصر";
    public static final String NAME_SUNSET = "غروب الشمس";
    public static final String NAME_MAGHRIB = "المغرب";
    public static final String NAME_ISHA = "العشاء";


    @Id(autoincrement = true)
    @Unique
    private Long id;

    @Property(nameInDb = "tag")
    private String tag;

    @Property(nameInDb = "prayer_name")
    private String prayer_name;

    @Property(nameInDb = "prayer_time")
    private String time;

    @Property(nameInDb = "alarm_set")
    private boolean alarmSet;

    public PrayerTime(String tag, String s, boolean b) {
        this.tag = tag;
        this.time = s;
        this.alarmSet = b;
    }

    public PrayerTime() {
    }

    @Generated(hash = 1336709785)
    public PrayerTime(Long id, String tag, String prayer_name, String time,
                      boolean alarmSet) {
        this.id = id;
        this.tag = tag;
        this.prayer_name = prayer_name;
        this.time = time;
        this.alarmSet = alarmSet;
    }

    protected PrayerTime(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        tag = in.readString();
        prayer_name = in.readString();
        time = in.readString();
        alarmSet = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(tag);
        dest.writeString(prayer_name);
        dest.writeString(time);
        dest.writeByte((byte) (alarmSet ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PrayerTime> CREATOR = new Creator<PrayerTime>() {
        @Override
        public PrayerTime createFromParcel(Parcel in) {
            return new PrayerTime(in);
        }

        @Override
        public PrayerTime[] newArray(int size) {
            return new PrayerTime[size];
        }
    };

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getAlarmSet() {
        return this.alarmSet;
    }

    public void setAlarmSet(boolean alarmSet) {
        this.alarmSet = alarmSet;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrayer_name() {
        return this.prayer_name;
    }

    public void setPrayer_name(String prayer_name) {
        this.prayer_name = prayer_name;
    }

}
