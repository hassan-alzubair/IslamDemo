package com.hassan.islamicdemo.PrayersService;

import com.hassan.islamicdemo.Home.PrayerTime;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PrayersTimesJsonParser {

    private List<PrayerTime> times;
    private String hDate;
    private String gDate;

    public List<PrayerTime> getTimes() {
        return times;
    }

    public String gethDate() {
        return hDate;
    }

    public String getgDate() {
        return gDate;
    }

    private PrayersTimesJsonParser(){
    }

    public void setTimes(List<PrayerTime> times) {
        this.times = times;
    }

    public void sethDate(String hDate) {
        this.hDate = hDate;
    }

    public void setgDate(String gDate) {
        this.gDate = gDate;
    }

    public static PrayersTimesJsonParser parse(JSONObject jsonObject) throws Exception {
        PrayersTimesJsonParser parser = new PrayersTimesJsonParser();

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

        // (h) means hijry
        String hDay = jsonObject.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getString("day");
        String hMonth = jsonObject.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getJSONObject("month").getString("ar");
        String hYear = jsonObject.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getString("year");

        // (g) means gregorian
        String gDate = jsonObject.getJSONObject("data").getJSONObject("date").getJSONObject("gregorian").getString("date").replace("-", "/");
        String gDayName = jsonObject.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getJSONObject("weekday").getString("ar");

        parser.setTimes(times);
        parser.sethDate(String.format("%s/%s/%s", hYear, hMonth, hDay));
        parser.setgDate(gDayName + " " + gDate);
        return parser;
    }
}
