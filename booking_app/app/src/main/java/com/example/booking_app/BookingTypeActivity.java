package com.example.booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class BookingTypeActivity extends AppCompatActivity {

    private TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_type);

        titleTV = findViewById(R.id.CategoryTitle);

        String title;
        title = getIntent().getStringExtra("Name");
        titleTV.setText(title);
    }
}