package com.example.booking_app.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FormActivity extends AppCompatActivity {

    private TextView roomTitle , seminarNameET , descriptionET , organizingBodyET , audienceNoET ,splRequirementET;
    private String roomTitleS , seminarNameS , descriptionS , organizingBodyS , audienceNoS ,splRequirementS;
    String date , block;
    private CheckBox c1,c2,c3;
    private String allSlots;
    private Button bookBtn;

    FirebaseFirestore fireStore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        referenceUI();
        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String name = getIntent().getStringExtra("room");
        String date = getIntent().getStringExtra("date");
        String block = getIntent().getStringExtra("block");
        roomTitle.setText(name);

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookSeminar();
            }
        });

    }

    private void referenceUI() {
        roomTitle = findViewById(R.id.RoomName);
        seminarNameET = findViewById(R.id.title1);
        descriptionET = findViewById(R.id.descriptionBox);
        organizingBodyET = findViewById(R.id.organizingBody);
        audienceNoET = findViewById(R.id.audienceNo);
        splRequirementET = findViewById(R.id.splRequirement);

        bookBtn = findViewById(R.id.bookSeminar);

    }

    private void bookSeminar() {

        try {
            roomTitleS = roomTitle.getText().toString();
            seminarNameS = seminarNameET.getText().toString();
            descriptionS = descriptionET.getText().toString();
            organizingBodyS = organizingBodyET.getText().toString();
            audienceNoS = audienceNoET.getText().toString();
            splRequirementS = splRequirementET.getText().toString();

            HashMap<String , Object> map = new HashMap<>();
            map.put("roomNo" , roomTitleS);
            map.put("name" , seminarNameS);
            map.put("description" , descriptionS);
            map.put("organizer" , organizingBodyS);
            map.put("audience" , audienceNoS);
            map.put("splRequirement" , splRequirementS);
            map.put("email" , mAuth.getCurrentUser().getEmail().toString());
            map.put("date" ,date);
            map.put("block" , block);

            DocumentReference dateReference = fireStore.collection("date")
                    .document(date)
                    .collection("classroom")
                    .document("block")
                    .collection(block)
                    .document(roomTitleS);

            DocumentReference seminarsReference = fireStore.collection("seminars")
                    .document(seminarNameS);



        }
        catch (NullPointerException exception){
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
        }

    }
}