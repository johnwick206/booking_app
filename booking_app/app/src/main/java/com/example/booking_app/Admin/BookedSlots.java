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
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookedSlots extends AppCompatActivity {

    FirebaseFirestore fireStore;
    AdminEventAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_slots);

        recyclerView = findViewById(R.id.recylcerView);
        fireStore = FirebaseFirestore.getInstance();


        String collectionName = getIntent().getStringExtra("SlotCategory");

      /*  assert collectionName != null;
        fireStore.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                        list.add(document.getId());
                    Toast.makeText(BookedSlots.this, "Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookedSlots.this, "Failed to load", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

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
        });
    }

    private void adapterFn(String collectionName){
        Query query = fireStore.collection(collectionName).orderBy("roomNo",Query.Direction.DESCENDING);
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