package com.example.booking_app.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.booking_app.R;
import com.example.booking_app.RoomDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminMain extends AppCompatActivity implements View.OnClickListener {


    FloatingActionButton addRoomBtn;
    View bookedClass , bookedLab , bookedSeminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        addRoomBtn = findViewById(R.id.AddRoomBtn);
        bookedClass = findViewById(R.id.classroomCard);
        bookedLab = findViewById(R.id.LabCard);
        bookedSeminar = findViewById(R.id.SeminarCard);

        addRoomBtn.setOnClickListener(this);
        bookedClass.setOnClickListener(this);
        bookedLab.setOnClickListener(this);
        bookedSeminar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.AddRoomBtn : openFormToAddRoom(); break;
            case R.id.classroomCard : displaySlots("ClassRoom"); break;
            case R.id.LabCard : displaySlots("Lab"); break;
            case R.id.SeminarCard :displaySlots("Seminar Hall"); break;
            default: break;
        }
    }

   private void displaySlots(String roomType){
        Intent intent = new Intent(AdminMain.this , BookedSlots.class);
       RoomDetails.roomType = roomType;
        startActivity(intent);
   }

    private void openFormToAddRoom() {
        startActivity(new Intent(AdminMain.this , AddRoomActivity.class));
    }

}