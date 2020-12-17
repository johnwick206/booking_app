package com.example.booking_app.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.booking_app.R;
import com.example.booking_app.RoomDetails;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
          datedelref=fireStore.collection("Date").document("30-30-3030").collection(RoomDetails.roomType)
                .document("block").collection(RoomDetails.roomBlock);

        String collectionName = RoomDetails.roomType;


        //retrieved data from firebase
         adapterFn(collectionName);

        //on click event from recyclerview -> move to details activity
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
                        dateDelete(date,slot,roomNo);
                    }
                });
            }
        });
    }

    private void dateDelete(String date, final String slot, final String roomNo) {
         datedelref=fireStore.collection("Date").document(date).collection(RoomDetails.roomType)
                .document("block").collection(RoomDetails.roomBlock);

        datedelref.document(roomNo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String dateSlotOld=task.getResult().get("slots").toString();
                int updatedSlot=Integer.parseInt(dateSlotOld)-Integer.parseInt(slot);
                HashMap<String,Object> map=new HashMap<>();
                map.put("slots",Integer.toString(updatedSlot));
                datedelref.document(roomNo).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(BookedSlots.this, "Seminar Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
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