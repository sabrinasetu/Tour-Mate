package com.example.maruf.tourMateApplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maruf.tourMateApplication.Activity.EventDetailsActivity;
import com.example.maruf.tourMateApplication.OtherClass.DatabaseRef;
import com.example.maruf.tourMateApplication.ProjoPackage.EventCreates;
import com.example.maruf.tourMateApplication.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.CustomViewHolder> {
    private Context context;
    private  List<EventCreates> eventCreatesList;

    public EventAdapter(Context context, List<EventCreates> eventCreatesList){
        this.context = context;
        this.eventCreatesList = eventCreatesList;

    }



    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.event_list_recycler_view, viewGroup, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {
        final EventCreates currentEvent = eventCreatesList.get(i);
        holder.eventName.setText(currentEvent.getEventName());
        holder.fromDate.setText(currentEvent.getFromDate());
        holder.toDate.setText(currentEvent.getToDate());
        holder.estimateBudget.setText(currentEvent.getEstimatedBudget() +" BDT");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EventDetailsActivity.class);
                intent.putExtra("eventName",currentEvent.getEventName());
                intent.putExtra("eventId",currentEvent.getId());
                intent.putExtra("eventBudget",currentEvent.getEstimatedBudget());
                context.startActivity(intent);


            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseRef.userRef.child("Events").child(currentEvent.getId()).removeValue();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return eventCreatesList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView eventName,fromDate,toDate,estimateBudget;
        private CardView cardView;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventDestinationTv);
            fromDate = itemView.findViewById(R.id.eventStartTv);
            toDate = itemView.findViewById(R.id.eventEndTv);
            estimateBudget = itemView.findViewById(R.id.estimateBudgetTv);
            cardView = itemView.findViewById(R.id.eventDetailCV);
        }
    }
}
