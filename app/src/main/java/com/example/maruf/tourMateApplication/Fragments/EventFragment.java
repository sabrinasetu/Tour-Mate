package com.example.maruf.tourMateApplication.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maruf.tourMateApplication.Adapter.EventAdapter;
import com.example.maruf.tourMateApplication.OtherClass.DatabaseRef;
import com.example.maruf.tourMateApplication.OtherClass.DatePicker;
import com.example.maruf.tourMateApplication.ProjoPackage.EventCreates;
import com.example.maruf.tourMateApplication.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class EventFragment extends Fragment {

    public EventFragment() {

    }
    private FloatingActionButton openBottomsheetBtn;
    private EditText eventNameEt,fromDateEt,toDateEt,esatimateBudgetEt;
    private Button addEventEt;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String eventId;
    private BottomSheetDialog bottomSheetDialog;
    private EventAdapter eventAdapter;
    private  List<EventCreates>eventList;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        eventList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        recyclerView = view.findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        loadEventListFromDatabase();


        openBottomsheetBtn = view.findViewById(R.id.openBottomSheet);
        openBottomsheetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(getActivity());
                View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout,null);
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
                eventNameEt = sheetView.findViewById(R.id.eventET);
                fromDateEt = sheetView.findViewById(R.id.fromDateEt);
                toDateEt = sheetView.findViewById(R.id.toDateEt);
                esatimateBudgetEt = sheetView.findViewById(R.id.estimatebudgetEt);
                addEventEt = sheetView.findViewById(R.id.addEvent);
                DatePicker date = new DatePicker();
                date.datePicker(fromDateEt,getActivity());
                date.datePicker(toDateEt,getActivity());
                addEventEt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validationAndSend();
                    }
                });
            }
        });









        return view;
    }

    private void loadEventListFromDatabase() {
        DatabaseRef.userRef.child(userId).child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                if(!dataSnapshot.exists()){
                    progressDialog.dismiss();
                }else {
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        EventCreates events = d.getValue(EventCreates.class);
                        eventList.add(events);
                        eventAdapter = new EventAdapter(getActivity(),eventList);
                        recyclerView.setAdapter(eventAdapter);
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }






    private void validationAndSend() {
        String eventName = eventNameEt.getText().toString();
        String fromDate = fromDateEt.getText().toString();
        String toDate = toDateEt.getText().toString();
        String budgets = esatimateBudgetEt.getText().toString();
        Double budget = Double.valueOf(budgets);
        if(eventName.isEmpty() || fromDate.isEmpty() || toDate.isEmpty() || budget.equals(0.0)){
            Toasty.warning(getActivity(),"Please fill all the field",Toast.LENGTH_SHORT,false).show();
        }else{
            saveEventToDatabase(eventName,fromDate,toDate,budget);


        }
    }

    private void saveEventToDatabase(String eventName,String fromDate,String toDate,Double budget) {
        eventId = DatabaseRef.userRef.child(userId).child("Events").push().getKey();
        EventCreates eventDetails = new EventCreates(eventId,eventName,fromDate,toDate,budget);
        DatabaseRef.userRef.child(userId).child("Events").child(eventId).setValue(eventDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    bottomSheetDialog.dismiss();
                    Toasty.info(getActivity(),"Event Created Successfully",Toast.LENGTH_SHORT,false).show();
                }else {
                    Toasty.warning(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT,false).show();
                    bottomSheetDialog.dismiss();
                }
            }
        });

    }


}
