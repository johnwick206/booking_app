package com.example.booking_app.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.booking_app.R;
import com.example.booking_app.RoomDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminEventDetails extends AppCompatActivity {

    TextView eventName,organizer,category,roomNo,date,slot,contact,splRequirement ;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_details);


        referenceUI();

        String name = getIntent().getStringExtra("event Name");
        eventName.setText(name);

        displayDetails(name);


    }

    private void referenceUI() {
        db= FirebaseFirestore.getInstance();
        eventName = findViewById(R.id.name);
        organizer= findViewById(R.id.organizerName);
        category=findViewById(R.id.actualcategory);
        roomNo=findViewById(R.id.actualroomno);
        date=findViewById(R.id.actualdate);
        slot=findViewById(R.id.actualslot);
        contact=findViewById(R.id.actualcontact);
        splRequirement=findViewById(R.id.req);
    }


    private void displayDetails(String name) {
        final DocumentReference docRef = db.collection("Booking: "+ RoomDetails.roomType).document(name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("done", "DocumentSnapshot data: " + document.getData());
                        organizer.setText(document.getString("organizer"));
                        category.setText(document.getString("category"));
                        roomNo.setText(document.getString("roomNo"));
                        date.setText(document.getString("date"));
                        slot.setText(makeslot(""+document.getString("slot")));
                        contact.setText(document.getString("email"));
                        splRequirement.setText(document.getString("splRequirement"));
                    } else {
                        Log.d("nonexists", "No such document");
                    }
                } else {
                    Log.d("fail", "get failed with ", task.getException());
                }
            }
        });
    }

    private String makeslot(String binslot){

        String stringSlot="";

        for(int i=0;i<binslot.length();i++){
            if(i==0){
                if(binslot.charAt(i)==49)stringSlot = stringSlot.concat("9am-10am\n");
            }
            if(i==1){
                if(binslot.charAt(i)==49)stringSlot=stringSlot.concat("10am-11am\n");
            }
            if(i==2){
                if(binslot.charAt(i)==49)stringSlot=stringSlot+"11am-12pm\n";
            }
            if(i==3){
                if(binslot.charAt(i)==49)stringSlot=stringSlot+"12pm-01pm\n";
            }
            if(i==4){
                if(binslot.charAt(i)==49)stringSlot=stringSlot+"01pm-02pm\n";
            }
            if(i==5){
                if(binslot.charAt(i)==49)stringSlot=stringSlot+"02pm-03pm\n";
            }
            if(i==6){
                if(binslot.charAt(i)==49)stringSlot=stringSlot+"03pm-04pm\n";
            }
            if(i==7){
                if(binslot.charAt(i)==49)stringSlot=stringSlot+"04pm-05pm\n";
            }
            if(i==8){
                if(binslot.charAt(i)==49)stringSlot=stringSlot+"05pm-06pm\n";
            }

        }

        return stringSlot;
    }
}
