package com.example.booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AdminEventDetails extends AppCompatActivity {

    TextView eventName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_details);

        eventName = findViewById(R.id.name);

        String name = getIntent().getStringExtra("Name");
        eventName.setText(name);
    }
}