package com.hassan.islamicdemo.Base;

import com.hassan.islamicdemo.Utils.AppConstants;

import retrofit2.Retrofit;

public class ApiClient {

    private static Retrofit aladthanInstance;
    private static Retrofit googleApiInstance;

    private ApiClient() {
    }

    public static Retrofit getAladhanApiClient() {
        if (aladthanInstance == null) {
            aladthanInstance = new Retrofit.Builder()
                    .baseUrl(AppConstants.ALADTHAN_API_HOST)
                    .build();
        }
        return aladthanInstance;
    }

    public static Retrofit getGoogleApiClient() {
        if (googleApiInstance == null) {
            googleApiInstance = new Retrofit.Builder()
                    .baseUrl(AppConstants.GOOGLE_API_CLIENT)
                    .build();
        }
        return googleApiInstance;
    }
}
