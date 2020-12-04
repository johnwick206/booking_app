package com.example.booking_app.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.booking_app.R;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private View clsRoomCard, labCard, seminarCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        referenceUI();
        setClickListeners();
    }

    private void referenceUI() {
        clsRoomCard = findViewById(R.id.classroomCard);
        labCard = findViewById(R.id.LabCard);
        seminarCard = findViewById(R.id.SeminarCard);
    }

    private void setClickListeners() {
        clsRoomCard.setOnClickListener(this);
        labCard.setOnClickListener(this);
        seminarCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.classroomCard:makeTransition(clsRoomCard , "ClassRoom"); break;
            case R.id.LabCard: makeTransition(labCard , "Lab"); break;
            case R.id.SeminarCard:makeTransition(seminarCard , "Seminar Hall"); break;

        }
    }

    private void makeTransition(View view ,String name) {
        Intent intent = new Intent(CategoryActivity.this , BookingTypeActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(CategoryActivity.this
                , view
                , "cardTransition");

        intent.putExtra("Name" , name);
        startActivity(intent , optionsCompat.toBundle());
    }
}