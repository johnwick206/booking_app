package com.example.booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class AdminMain extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout r1, r2 ,r3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        r1 = findViewById(R.id.event1);
        r2 = findViewById(R.id.event2);
        r3 = findViewById(R.id.event3);

        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.event1 : openDetails("Seminar 1"); break;
            case R.id.event2 : openDetails("Seminar 2"); break;
            case R.id.event3 : openDetails("Seminar 3"); break;
        }
    }

    private void openDetails(String name) {
        Intent intent = new Intent(AdminMain.this ,AdminEventDetails.class);
        intent.putExtra("Name",name);
        startActivity(intent);
    }
}