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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDate;
import java.util.ArrayList;

public class BookedSlots extends AppCompatActivity {

    FirebaseFirestore fireStore;
    AdminEventAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> list = new ArrayList<>();
    CollectionReference datedelref;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            public void deleteEvents(final String slot, final String date, String name, final String roomNo,final String emailId) {

                CollectionReference delref=fireStore.collection( "Booking: "+RoomDetails.roomType);

                delref.document(name).delete().addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        freeSlotOnDelete(date,slot,roomNo,emailId);
                    }
                });
            }
        });
    }

    //updating slots on deleting event
    private void freeSlotOnDelete(final String date, final String slot, final String roomNo, final String emailId) {
        datedelref=fireStore.collection("Date").document(date).collection(RoomDetails.roomType)
                .document("block").collection(RoomDetails.roomBlock);

        datedelref.document(roomNo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()) {
                    String newSlots="";

                    try {
                        String dateSlotOld = documentSnapshot.getString("slots");
                        for(int i=0;i<dateSlotOld.length();i++){
                            if(slot.charAt(i)==49){
                                newSlots=newSlots.concat("0");
                            }
                            else{
                                newSlots=newSlots.concat(String.valueOf(dateSlotOld.charAt(i)));
                            }
                        }

                        datedelref.document(roomNo).update("slots", newSlots).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(BookedSlots.this, "Update Completed", Toast.LENGTH_SHORT).show();
                                String to=emailId;
                                String subject="Cancellation of Your Seminar";
                                String message="Respected Organizer,\n\n" +
                                        "Sorry to inform you that your seminar : "+AdminEventAdapter.eventName+" on date "+date+"(yyyy-mm-dd) has been cancelled.\n\n" +
                                        "For more information contact : swapnil.sonawane@vit.edu.in\n\n" +
                                        "Yours Sincerely,\n" +
                                        "VIT BOOKING staff";


                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                                email.putExtra(Intent.EXTRA_TEXT, message);

                                //need this to prompts email client only
                                email.setType("message/rfc822");

                                startActivity(Intent.createChooser(email, "Choose an Email client :"));
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void adapterFn(String collectionName){
        LocalDate myObj = LocalDate.now();
        Query query = fireStore.collection( "Booking: " + collectionName).whereGreaterThan("date",myObj.toString()).orderBy("date",Query.Direction.ASCENDING);
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