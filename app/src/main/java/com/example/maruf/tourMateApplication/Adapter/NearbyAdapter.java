package com.example.maruf.tourMateApplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maruf.tourMateApplication.NearbyResponse.Result;
import com.example.maruf.tourMateApplication.R;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class NearbyAdapter extends RecyclerView.Adapter< NearbyAdapter.ViewHolder > {
    private List<Result> results;
    private Context context;
    public NearbyAdapter(List<Result> results,Context context) {
        this.results = results;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nearbyrecyclerview,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.placeName.setText(results.get(i).getName());
        try{
            boolean value = results.get(i).getOpeningHours().getOpenNow();
            if(!value){
                viewHolder.placeOpen.setText("Close");
            }else {
                viewHolder.placeOpen.setText("Open");
            }
            viewHolder.placeRating.setText(results.get(i).getRating().toString());
            viewHolder.placeAddress.setText(results.get(i).getVicinity());

        }catch (Exception e){

        }



    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView placeName,placeOpen,placeRating,placeAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            placeOpen = itemView.findViewById(R.id.placeOpen);
            placeRating = itemView.findViewById(R.id.placeRating);
            placeAddress = itemView.findViewById(R.id.placeAddress);

        }
    }

}
