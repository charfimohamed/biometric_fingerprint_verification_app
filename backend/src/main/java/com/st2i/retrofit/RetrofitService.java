package com.st2i.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit ;

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        Gson customGson = CustomGson.getGson();
        retrofit= new Retrofit.Builder().
                baseUrl("http://192.168.1.19:8080").
                addConverterFactory(GsonConverterFactory.create(customGson)).
                build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
