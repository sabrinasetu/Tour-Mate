package com.example.maruf.tourMateApplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.maruf.tourMateApplication.ProjoPackage.MemorablePlaces;
import com.example.maruf.tourMateApplication.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class MemorableAdapter extends RecyclerView.Adapter<MemorableAdapter.ViewHolder> {

    private Context context;
    private List<MemorablePlaces> memorablePlacesList;
    public MemorableAdapter(Context context,List<MemorablePlaces> memorablePlacesList) {
        this.context = context;
        this.memorablePlacesList = memorablePlacesList;

    }

    @NonNull
    @Override
    public MemorableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.memorable_places_recycler_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemorableAdapter.ViewHolder viewHolder, int i) {
        MemorablePlaces currentMemory = memorablePlacesList.get(i);
        Picasso.get().load(currentMemory.getDownloadUrl()).into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return memorablePlacesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.memorableImageView);

        }
    }
}
