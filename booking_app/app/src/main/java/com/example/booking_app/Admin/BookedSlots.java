package com.example.booking_app.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.booking_app.R;
import com.example.booking_app.RoomDetails;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class BookedSlots extends AppCompatActivity {

    FirebaseFirestore fireStore;
    AdminEventAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> list = new ArrayList<>();
    CollectionReference datedelref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_slots);

        recyclerView = findViewById(R.id.recylcerView);
        fireStore = FirebaseFirestore.getInstance();

        //dummy reference
          datedelref=fireStore.collection("Date").document("30-30-3030").collection(RoomDetails.roomType)
                .document("block").collection("A");

        String collectionName = RoomDetails.roomType;


        //retrieved data from firebase
         adapterFn(collectionName);

        //on click events from recyclerview -> move to details activity
        AdminEventAdapter.setOnClickEvent(new AdminEventAdapter.onClickEvent() {
            @Override
            public void showDetails(String name) {
                Intent intent = new Intent(BookedSlots.this , AdminEventDetails.class);
                intent.putExtra("event Name" , name);
                startActivity(intent);
            }

            @Override
            public void deleteEvents(final String slot, final String date, String name, final String roomNo) {

                CollectionReference delref=fireStore.collection( "Booking: "+RoomDetails.roomType);

                delref.document(name).delete().addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        freeSlotOnDelete(date,slot,roomNo);
                    }
                });
            }
        });
    }

    //updating slots on deleting event
    private void freeSlotOnDelete(String date, final String slot, final String roomNo) {
         datedelref=fireStore.collection("Date").document(date).collection(RoomDetails.roomType)
                .document("block").collection(RoomDetails.roomBlock);

        datedelref.document(roomNo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()) {

                    try {
                        String dateSlotOld = documentSnapshot.getString("slots");
                        int updatedSlot = Integer.parseInt(dateSlotOld) - Integer.parseInt(slot);
                        String newSlots = (updatedSlot == 0) ? "000000000" : Integer.toString(updatedSlot);

                        datedelref.document(roomNo).update("slots", newSlots).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(BookedSlots.this, "Update Completed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (Exception e){
                        Toast.makeText(BookedSlots.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(BookedSlots.this, "Doc doesnt exist", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void adapterFn(String collectionName){
        Query query = fireStore.collection( "Booking: " + collectionName).orderBy("date",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<AdminEventModel> options = new FirestoreRecyclerOptions.Builder<AdminEventModel>().setQuery(query , AdminEventModel.class).build();
        adapter = new AdminEventAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}