package com.example.booking_app.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

public class FormActivity extends AppCompatActivity {

    private TextView roomTitle , seminarNameET , descriptionET , organizingBodyET , audienceNoET ,splRequirementET;
    private String roomTitleS , seminarNameS , descriptionS , organizingBodyS , audienceNoS ,splRequirementS;
    private String date , block , name;
    private CheckBox c1,c2,c3;
    private String allSlots;
    private Button bookBtn;
    private int i=0;
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
        roomTitle.setText(name);

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

        roomTitle = findViewById(R.id.RoomName);
        seminarNameET = findViewById(R.id.title1);
        descriptionET = findViewById(R.id.descriptionBox);
        organizingBodyET = findViewById(R.id.organizingBody);
        audienceNoET = findViewById(R.id.audienceNo);
        splRequirementET = findViewById(R.id.splRequirement);

        bookBtn = findViewById(R.id.bookSeminar);

    }

    private void bookSeminar() {

            // TODO: 09-12-2020 list checkboxes 
           /* ArrayList<CheckBox> checkBoxList = new ArrayList<>();
            checkBoxList.add(c1);
            checkBoxList.add(c2);
            checkBoxList.add(c3);
            int[] value = new int[3];
            HashMap<String , Integer> map1 = new HashMap<>();

            while (i < checkBoxList.size()){
                if(checkBoxList.get(i).isChecked()) value[i] =1;
                i++;
            }

            map1.put(checkBoxList.get(0).getText().toString() , value[0] );
            map1.put(checkBoxList.get(1).getText().toString() , value[1] );
            map1.put(checkBoxList.get(2).getText().toString() , value[2] );

           if(!map1.containsValue(1))
               return;*/

            try {
                dialog = new ProgressDialog(FormActivity.this);
                dialog.setTitle("Add");
                dialog.setMessage("Adding");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                roomTitleS = roomTitle.getText().toString();
                seminarNameS = seminarNameET.getText().toString();
                descriptionS = descriptionET.getText().toString();
                organizingBodyS = organizingBodyET.getText().toString();
                audienceNoS = audienceNoET.getText().toString();
                splRequirementS = splRequirementET.getText().toString();

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
                map.put("slot" , "9-10");

                // TODO: 10-12-2020 select slots
           /* for(i = 0 ; i < checkBoxList.size() ;  i++)
                if(checkBoxList.get(i).isChecked())
                    map.put(checkBoxList.get(i).getText().toString() , 1);

                    DocumentReference dateReference = fireStore.collection("Date")
                    .document(date)
                    .collection("classroom")
                    .document("block")
                    .collection(block)
                    .document(roomTitleS);

            dateReference.set(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        System.out.println("added to date collection");
                    //added to date collection
                    else
                        System.out.println("Failed to add in date collection");
                }
            });
*/

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