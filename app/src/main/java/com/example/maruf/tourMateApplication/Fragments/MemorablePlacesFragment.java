package com.example.maruf.tourMateApplication.Fragments;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maruf.tourMateApplication.Adapter.ExpenseListAdapter;
import com.example.maruf.tourMateApplication.Adapter.MemorableAdapter;
import com.example.maruf.tourMateApplication.OtherClass.DatabaseRef;
import com.example.maruf.tourMateApplication.ProjoPackage.MemorablePlaces;
import com.example.maruf.tourMateApplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


import es.dmoral.toasty.Toasty;



public class MemorablePlacesFragment extends Fragment {

    public MemorablePlacesFragment() {
    }

    private FirebaseAuth firebaseAuth;
    private String userId;
    private String memorablePlacesId;
    private com.github.clans.fab.FloatingActionButton clickCamera;
    private String downloadUrl;
    private String eventId;
    private RecyclerView recyclerView;
    private MemorableAdapter memorableAdapter;
    private List<MemorablePlaces> memorablePlacesList;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memorable_places, container, false);
        Bundle bundle = getArguments();
        eventId = bundle.getString("id");
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        recyclerView = view.findViewById(R.id.memorableRecycler);
        memorablePlacesList = new ArrayList<MemorablePlaces>();
        loadMemorablePlaceListFromDatabase();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        clickCamera = view.findViewById(R.id.clickCamera);
        clickCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(data,1);
            }
        });
        return view;
    }

    private void loadMemorablePlaceListFromDatabase() {
        DatabaseRef.userRef.child(userId).child("Events").child(eventId).child("MemorablePlaces").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    memorablePlacesList.clear();
                    for(DataSnapshot d : dataSnapshot.getChildren()){
                        MemorablePlaces memorablePlaces= d.getValue(MemorablePlaces.class);
                        memorablePlacesList.add(memorablePlaces);
                    }
                    memorableAdapter = new MemorableAdapter(getActivity(),memorablePlacesList);
                    recyclerView.setAdapter(memorableAdapter);
               }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

            progressDialog = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_DialogMemorable);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Saving image");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            if (requestCode == 1 && resultCode == Activity.RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] uri = byteArrayOutputStream.toByteArray();
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("MemorablePhotos").child("photos_"+System.currentTimeMillis());
                storageReference.putBytes(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                memorablePlacesId = DatabaseRef.userRef.child(userId).child("Events").child("MemorablePlaces").push().getKey();
                                MemorablePlaces memorablePlaces = new MemorablePlaces(memorablePlacesId,downloadUrl);
                                DatabaseRef.userRef.child(userId).child("Events").child(eventId).child("MemorablePlaces").child(memorablePlacesId).setValue(memorablePlaces).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Toasty.info(getActivity(),"Image uploaded Sucessfully",Toast.LENGTH_SHORT,false).show();
                                        }else {
                                            Toasty.warning(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT,false).show();
                                        }
                                    }
                                });

                            }

                        });
                    }
                });
                }

        }catch (Exception e){
            Toasty.error(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }



}

