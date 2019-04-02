package com.hassan.islamicdemo.PrayersService;

import com.hassan.islamicdemo.Home.PrayerTime;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PrayersTimesJsonParser {


    public static List<PrayerTime> parse(JSONObject jsonObject) throws Exception {
        PrayersTimesJsonParser response = new PrayersTimesJsonParser();
        JSONObject timings = jsonObject.getJSONObject("data").getJSONObject("timings");
        List<PrayerTime> times = new ArrayList<>();
        PrayerTime time = new PrayerTime();

        time.setTag(PrayerTime.TAG_IMSAK);
        time.setTime(timings.getString("Imsak"));
        times.add(time);

        time = new PrayerTime();
        time.setTag(PrayerTime.TAG_FAJR);
        time.setTime(timings.getString("Fajr"));
        times.add(time);

        time = new PrayerTime();
        time.setTag(PrayerTime.TAG_SUNRISE);
        time.setTime(timings.getString("Sunrise"));
        times.add(time);

        time = new PrayerTime();
        time.setTag(PrayerTime.TAG_THUHR);
        time.setTime(timings.getString("Dhuhr"));
        times.add(time);

        time = new PrayerTime();
        time.setTag(PrayerTime.TAG_ASR);
        time.setTime(timings.getString("Asr"));
        times.add(time);

        time = new PrayerTime();
        time.setTag(PrayerTime.TAG_MAGHRIB);
        time.setTime(timings.getString("Maghrib"));
        times.add(time);

        time = new PrayerTime();
        time.setTag(PrayerTime.TAG_SUNSET);
        time.setTime(timings.getString("Sunset"));
        times.add(time);

        time = new PrayerTime();
        time.setTag(PrayerTime.TAG_ISHA);
        time.setTime(timings.getString("Isha"));
        times.add(time);
        return times;
    }
}
