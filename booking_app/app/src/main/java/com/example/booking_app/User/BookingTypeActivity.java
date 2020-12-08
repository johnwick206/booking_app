package com.example.booking_app.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.booking_app.CalenderFragment;
import com.example.booking_app.DialogBoxRB;
import com.example.booking_app.R;

import java.text.DateFormat;
import java.util.Calendar;

public class BookingTypeActivity extends AppCompatActivity implements View.OnClickListener , DatePickerDialog.OnDateSetListener {

    private TextView titleTV;
    private Button setDateBtn , setBlockBtn;
    private CardView testCard1 , testCard2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_type);

        titleTV = findViewById(R.id.CategoryTitle);
        setDateBtn = findViewById(R.id.DateSetBtn);
        setBlockBtn = findViewById(R.id.Name);
        testCard1 = findViewById(R.id.card1);
        testCard2 = findViewById(R.id.card2);

        setDateBtn.setOnClickListener(this);
        setBlockBtn.setOnClickListener(this);
        testCard1.setOnClickListener(this);
        testCard2.setOnClickListener(this);
        setTitle();

        DialogBoxRB.setOnCLickBlockOptn(new DialogBoxRB.onClickBlocks() {
            @Override
            public void setBlockName(String blockName) {
                setBlockBtn.setText(blockName);
            }
        });
    }

    private void setTitle() {
        String title;
        title = getIntent().getStringExtra("Name");
        titleTV.setText(title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.DateSetBtn: callDateFragment();break;
            case R.id.Name: chooseBlock(); ;break;
            case R.id.card1:openRoom(testCard1 ,"M - 101"); break;
            case R.id.card2:openRoom(testCard2 , "M - 102"); break;
            default: break;
        }
    }

    private void chooseBlock() {
        String[] list = getResources().getStringArray(R.array.Blocks);
        DialogBoxRB dialogBoxRB = new DialogBoxRB(list);
        dialogBoxRB.show(getSupportFragmentManager() , "open");
    }

    private void callDateFragment(){
        CalenderFragment calenderFragment = new CalenderFragment();
        calenderFragment.show(getSupportFragmentManager() , "Pick Up Date");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR , year);
        c.set(Calendar.MONTH , month);
        c.set(Calendar.DAY_OF_MONTH , day);

        String occasionDate = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
        String[] splitDate = occasionDate.split("/");
        String dateChangeFormat = splitDate[0] + "-"+ splitDate[1] + "-" + splitDate[2];
        setDateBtn.setText(dateChangeFormat);
    }

    private void openRoom( CardView card,String name) {
        Intent intent = new Intent(BookingTypeActivity.this , FormActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(BookingTypeActivity.this , card , "cardTransition2");
        intent.putExtra("room" , name);
        intent.putExtra("date" , setBlockBtn.getText());
        intent.putExtra("block" , setBlockBtn.getText());
        startActivity(intent,optionsCompat.toBundle());
    }

}