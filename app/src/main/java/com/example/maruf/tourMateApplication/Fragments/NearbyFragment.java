package com.example.maruf.tourMateApplication.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.maruf.tourMateApplication.Adapter.NearbyAdapter;
import com.example.maruf.tourMateApplication.ApiClient;
import com.example.maruf.tourMateApplication.ApiService;
import com.example.maruf.tourMateApplication.NearbyResponse.NearbyResponse;
import com.example.maruf.tourMateApplication.NearbyResponse.Result;
import com.example.maruf.tourMateApplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyFragment extends Fragment {
    public NearbyFragment() {
    }

    private ApiService service;
    double lat,lan;
    private int radius = 500;
    private RecyclerView recyclerView;
    private NearbyAdapter nearbyAdapter;
    List<Result> results;
    private Spinner spinner;
    private ProgressDialog progressDialog;
    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback callback;
    private static int REQUEST_CODE_FOR_LOCATION = 1;
    private TextView textView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        recyclerView = view.findViewById(R.id.nearbyId);
        service = ApiClient.getRetrofit().create(ApiService.class);
        spinner = view.findViewById(R.id.spinnerId);
        List<String> placelist = new ArrayList<>();
        placelist.add("restaurant");
        placelist.add("hospital");
        placelist.add("bank");
        placelist.add("bus_station");
        placelist.add("atm");
        placelist.add("mosque");
        placelist.add("hindu_temple");
        placelist.add("doctor");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,placelist);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String placeType = parent.getItemAtPosition(position).toString();
                getNearBy(placeType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view ;
    }

    private void getNearBy(String place_type) {
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000).setFastestInterval(1000);
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    lat = location.getLatitude();
                    lan = location.getLatitude();
                }

            }
        };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE_FOR_LOCATION);
            return;
        }

        client.requestLocationUpdates(request, callback, null);
        lat = 23.75;
        lan= 90.39;
        String url = String.format("place/nearbysearch/json?location=%f,%f&radius=%d&type=%s&key=%s",lat,lan,radius,place_type,getResources().getString(R.string.apiKey));
        Call<NearbyResponse> nearbyResponseCall = service.getNearby(url);
        nearbyResponseCall.enqueue(new Callback<NearbyResponse>() {
            @Override
            public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                if(response.code() == 200){
                    NearbyResponse nearbyResponse =  response.body();
                    results = nearbyResponse.getResults();
                    nearbyAdapter = new NearbyAdapter(results,getActivity());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(nearbyAdapter);
                }
            }

            @Override
            public void onFailure(Call<NearbyResponse> call, Throwable t) {

            }
        });

    }


}
