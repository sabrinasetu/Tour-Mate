package com.example.maruf.tourMateApplication.Fragments;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maruf.tourMateApplication.ApiClient;
import com.example.maruf.tourMateApplication.ApiService;
import com.example.maruf.tourMateApplication.R;
import com.example.maruf.tourMateApplication.WeatherResponse.WeatherResponse;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class WeatherFragment extends Fragment {
    public WeatherFragment() {
    }

    private ApiService service;
    private TextView date,location,temp,minTemp,maxtemp,wind,pressure,time,des;
    private ImageView imageView;
    private String unit = "metric";
    private double lat = 23.750877;
    private double lon =90.393552;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_weather, container, false);
        date = view.findViewById(R.id.dateId);
        location = view.findViewById(R.id.weatherlocationTv);
        temp = view.findViewById(R.id.temperatureTV);
        maxtemp = view.findViewById(R.id.maxTemperatureTV);
        minTemp = view.findViewById(R.id.minTemperatureTV);
        wind = view.findViewById(R.id.windTV);
        pressure = view.findViewById(R.id.pressuereTV);
        imageView = view.findViewById(R.id.weatherImage);
        time = view.findViewById(R.id.weatherTimeTv);
        des = view.findViewById(R.id.weatherImageDescription);

        String url = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s",lat,lon,unit,getResources().getString(R.string.weatherApiKey));
        service = ApiClient.getCurrentWeather().create(ApiService.class);
        Call<WeatherResponse> userResponseCall = service.getWeatherDetail(url);
        userResponseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    long dates = System.currentTimeMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                    SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd yyyy");
                    String tymString = sdfs.format(dates);
                    String datString = sdf.format(dates);
                    date.setText(tymString);
                    time.setText(datString);
                    WeatherResponse weatherResponse = response.body();
                    location.setText(weatherResponse.getName());
                    temp.setText(weatherResponse.getMain().getTemp().toString());
                    minTemp.setText(weatherResponse.getMain().getTempMin().toString());
                    maxtemp.setText(weatherResponse.getMain().getTempMax().toString());
                    wind.setText(weatherResponse.getWind().getSpeed().toString());
                    pressure.setText(weatherResponse.getMain().getPressure().toString());
                    String image = weatherResponse.getWeather().get(0).getIcon();
                    des.setText(weatherResponse.getWeather().get(0).getDescription());
                    Picasso.get().load("http://openweathermap.org/img/w/"+image+".png").into(imageView);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        return view;


    }
}
