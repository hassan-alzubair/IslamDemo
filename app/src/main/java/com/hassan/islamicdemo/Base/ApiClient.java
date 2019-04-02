package com.hassan.islamicdemo.Base;

import com.hassan.islamicdemo.Utils.AppConstants;

import retrofit2.Retrofit;

public class ApiClient {

    private static Retrofit aladthanInstance;

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
}
