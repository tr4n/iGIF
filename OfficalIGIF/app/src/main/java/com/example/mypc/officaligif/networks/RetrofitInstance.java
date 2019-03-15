package com.example.mypc.officaligif.networks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    public static Retrofit retrofitGif;

    public static Retrofit getRetrofitGifInstance() {
        if (retrofitGif == null) {
            retrofitGif = new Retrofit.Builder()
                    .baseUrl("http://api.giphy.com/v1/gifs/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitGif;
    }
}
