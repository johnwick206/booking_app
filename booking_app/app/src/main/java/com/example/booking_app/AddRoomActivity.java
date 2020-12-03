package com.example.booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddRoomActivity extends AppCompatActivity {

    private EditText roomET , blockET;
    private TextView categoryET;
    private Button addBtn;

    String blockS , roomS , categoryS;

    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        referenceUI();
        fireStore = FirebaseFirestore.getInstance();

        categoryET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open dialog box
                Toast.makeText(AddRoomActivity.this, "open Dialog Box", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void referenceUI() {
        blockET = findViewById(R.id.BlockName);
        roomET = findViewById(R.id.NewRoomName);
        addBtn = findViewById(R.id.Add);
        categoryET = findViewById(R.id.CategoryName);
    }
    public void addRoom(View view){
        blockS = blockET.getText().toString();
        roomS = roomET.getText().toString();

        HashMap<String , Object> map = new HashMap<>();
        map.put("9-10" , "0");
        DocumentReference reference =  fireStore.collection("classroom")
                .document("block")
                .collection("M")
                .document("101");

        reference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())  Toast.makeText(AddRoomActivity.this, "Added", Toast.LENGTH_SHORT).show();
                else Toast.makeText(AddRoomActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });



    }
}