package com.example.booking_app.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking_app.R;
import com.example.booking_app.RoomDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.GONE;

public class FormActivity extends AppCompatActivity {

    private TextView roomTitle , seminarNameET , descriptionET , organizingBodyET , audienceNoET ,splRequirementET;
    private String roomTitleS , seminarNameS , descriptionS , organizingBodyS , audienceNoS ,splRequirementS;
    private String date , block , name;
    private CheckBox c1,c2,c3,c4,c5,c6,c7,c8,c9;
    private String allSlots ,slotselected;
    private Button bookBtn;

    private int i=0;
    ArrayList<CheckBox> checkBoxList;
    ProgressDialog dialog;

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

        name = RoomDetails.roomNumber;
        block = RoomDetails.roomBlock;

        date = getIntent().getStringExtra("date");
        allSlots=getIntent().getStringExtra("slots");

        roomTitle.setText(name);
        bookedDisable();
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookSeminar();
            }
        });

    }

    private void referenceUI() {

        c1 = findViewById(R.id.time1);
        c2 = findViewById(R.id.time2);
        c3 = findViewById(R.id.time3);
        c4 = findViewById(R.id.time4);
        c5 = findViewById(R.id.time5);
        c6 = findViewById(R.id.time6);
        c7 = findViewById(R.id.time7);
        c8 = findViewById(R.id.time8);
        c9 = findViewById(R.id.time9);
        roomTitle = findViewById(R.id.RoomName);
        seminarNameET = findViewById(R.id.title1);
        descriptionET = findViewById(R.id.descriptionBox);
        organizingBodyET = findViewById(R.id.organizingBody);
        audienceNoET = findViewById(R.id.audienceNo);
        splRequirementET = findViewById(R.id.splRequirement);
        checkBoxList = new ArrayList<>();
        checkBoxList.add(c1);
        checkBoxList.add(c2);
        checkBoxList.add(c3);
        checkBoxList.add(c4);
        checkBoxList.add(c5);
        checkBoxList.add(c6);
        checkBoxList.add(c7);
        checkBoxList.add(c8);
        checkBoxList.add(c9);

        bookBtn = findViewById(R.id.bookSeminar);

    }

    private void bookedDisable(){
        int i;
        Toast.makeText(FormActivity.this,allSlots, Toast.LENGTH_SHORT).show();
        for(i=0;i<allSlots.length();i++){
            if(allSlots.charAt(i)==49){
                checkBoxList.get(i).setEnabled(false);
                checkBoxList.get(i).setVisibility(GONE);
            }

        }

    }

    private void bookSeminar() {

        // TODO: 09-12-2020 list checkboxes


        int[] value = {0,0,0,0,0,0,0,0,0};
        int atleastOne=0;


        while (i < checkBoxList.size()){
            if(checkBoxList.get(i).isChecked()){ value[i]=1;atleastOne=1;}
            else if(!checkBoxList.get(i).isEnabled())value[i]=1;
            i++;
        }

         slotselected = new String();

        for(i=0;i<value.length;i++){
            slotselected=slotselected.concat(Integer.toString(value[i]));
        }


       /* if(!(atleastOne > 0)) {
            Toast.makeText(FormActivity.this, "Please select atleast one slot", Toast.LENGTH_SHORT).show();
            return;
        }*/

        try {
            dialog = new ProgressDialog(FormActivity.this);
            dialog.setTitle("Add");
            dialog.setMessage("Adding");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


            // TODO: 10-12-2020 select slots

            roomTitleS = roomTitle.getText().toString();
            seminarNameS = seminarNameET.getText().toString();
            descriptionS = descriptionET.getText().toString();
            organizingBodyS = organizingBodyET.getText().toString();
            audienceNoS = audienceNoET.getText().toString();
            splRequirementS = splRequirementET.getText().toString();


            final HashMap<String , Object> map1 = new HashMap<>();
            map1.put("roomNo", RoomDetails.roomNumber);
            map1.put("slots" , slotselected );

            final HashMap<String , Object> mapDummy = new HashMap<>();
            mapDummy.put("dummy" , "randomValue");

             final DocumentReference dateReference = fireStore.collection("Date")
                    .document(date);

             dateReference.set(mapDummy).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {

                    final DocumentReference a = dateReference.collection(RoomDetails.roomType).document("block");

                    a.set(mapDummy).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DocumentReference b = a.collection(RoomDetails.roomBlock).document(RoomDetails.roomNumber);
                            b.set(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    Toast.makeText(FormActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                 }
             });



            HashMap<String, Object> map = new HashMap<>();
            map.put("roomNo", roomTitleS);
            map.put("seminarName", seminarNameS);
            map.put("description", descriptionS);
            map.put("organizer", organizingBodyS);
            map.put("category", RoomDetails.roomType);
            map.put("audience", audienceNoS);
            map.put("splRequirement", splRequirementS);
            map.put("email", mAuth.getCurrentUser().getEmail().toString());
            map.put("date", date);
            map.put("block", block);
            map.put("slot" , slotselected);

            DocumentReference seminarsReference = fireStore.collection("Booking: " + RoomDetails.roomType)
                    .document(seminarNameS);

            seminarsReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        Toast.makeText(FormActivity.this, "Form accepted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(FormActivity.this, "Denied", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.dismiss();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

    }
}