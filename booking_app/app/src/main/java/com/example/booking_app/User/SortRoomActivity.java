package com.example.booking_app.User;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking_app.CalenderFragment;
import com.example.booking_app.DialogBoxRB;
import com.example.booking_app.R;
import com.example.booking_app.RoomDetails;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class SortRoomActivity extends AppCompatActivity implements View.OnClickListener , DatePickerDialog.OnDateSetListener {

    private TextView titleTV;
    private Button setDateBtn , setBlockBtn;
    private CardView testCard1 , testCard2;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    //Room Type :::: Header
    private void setTitle() {
        String title;
        title = getIntent().getStringExtra("Name");
        RoomDetails.roomType = title;
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

        setDateBtn.setText(occasionDate);
    }

    //on click specific room card
    private void openRoom( CardView card,String name) {
        Intent intent = new Intent(SortRoomActivity.this , FormActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SortRoomActivity.this , card , "cardTransition2");
        intent.putExtra("room" , name);
        intent.putExtra("date" , formatDate());
        intent.putExtra("block" , setBlockBtn.getText());

        RoomDetails.roomBlock = setBlockBtn.getText().toString();
        RoomDetails.roomNumber = name;
        startActivity(intent,optionsCompat.toBundle());
    }

    private String formatDate() {

        String dateChangeFormat = "Date";
        try {
            String[] splitDate = setDateBtn.getText().toString().split("/");
             dateChangeFormat = splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0];
        }
        catch (IndexOutOfBoundsException e){
            Toast.makeText(this, "Choose date", Toast.LENGTH_SHORT).show();
        }
        return dateChangeFormat;
    }

}