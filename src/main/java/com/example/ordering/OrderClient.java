package com.example.ordering;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderClient {
    private static final String BASE_URL = "https://api.upbit.com/";

    public static UpbitAPI getApi() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UpbitAPI.class);
    }
}
