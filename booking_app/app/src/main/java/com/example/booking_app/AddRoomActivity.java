package com.example.booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddRoomActivity extends AppCompatActivity {

    private EditText roomET , blockET;
    private Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        referenceUI();

    }

    private void referenceUI() {
        blockET = findViewById(R.id.BlockName);
        roomET = findViewById(R.id.NewRoomName);
        addBtn = findViewById(R.id.Add);
    }
    public void addRoom(View view){
        String blockS , roomS;
        blockS = blockET.getText().toString();
        roomS = roomET.getText().toString();
    }
}