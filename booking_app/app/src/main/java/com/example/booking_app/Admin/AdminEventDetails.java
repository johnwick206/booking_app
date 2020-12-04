package com.example.booking_app.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.booking_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminEventDetails extends AppCompatActivity {

    TextView eventName,organizer,category,roomNo,date,slot,contact ;
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
    }


    private void displayDetails(String name) {
        final DocumentReference docRef = db.collection("seminars").document(name);
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
                        slot.setText(document.getString("slot"));
                        contact.setText(document.getString("email"));
                    } else {
                        Log.d("nonexists", "No such document");
                    }
                } else {
                    Log.d("fail", "get failed with ", task.getException());
                }
            }
        });
    }
}