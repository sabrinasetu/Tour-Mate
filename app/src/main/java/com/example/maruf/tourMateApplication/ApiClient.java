package com.example.maruf.tourMateApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private static  String base_url = "https://maps.googleapis.com/maps/api/";
    private static  String base_url2 = "https://openweathermap.org/data/2.5/";

    public static  Retrofit getRetrofit(){
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        return  retrofit;
    }
    public static  Retrofit getCurrentWeather(){
        retrofit = new Retrofit.Builder().baseUrl(base_url2).addConverterFactory(GsonConverterFactory.create()).build();
        return  retrofit;
    }

}
