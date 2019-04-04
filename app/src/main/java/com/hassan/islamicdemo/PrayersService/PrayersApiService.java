package com.hassan.islamicdemo.PrayersService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrayersApiService {

    @GET("timings/today")
    Call<ResponseBody> getPrayersTimes(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("method") int method);

    @GET("json")
    Call<ResponseBody> getLocation(
            @Query("latlng") String latLng,
            @Query("sensor") boolean sensor,
            @Query("key") String key);
}
