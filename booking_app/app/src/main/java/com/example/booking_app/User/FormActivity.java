package com.example.booking_app.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.booking_app.R;

public class FormActivity extends AppCompatActivity {

    private TextView roomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        roomTitle = findViewById(R.id.RoomName);
        String name = getIntent().getStringExtra("room");
        roomTitle.setText(name);

    }
}