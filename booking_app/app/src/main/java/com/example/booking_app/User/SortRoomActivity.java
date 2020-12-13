package com.example.booking_app.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Calendar;

public class SortRoomActivity extends AppCompatActivity implements View.OnClickListener , DatePickerDialog.OnDateSetListener {

    private TextView titleTV;
    private Button setDateBtn , setBlockBtn , searchBtn;
    private RecyclerView recyclerView;
    FirebaseFirestore fireStore;
    DocumentReference dateCollectionDocument;
    CollectionReference roomCollection;
    public UserSlotAdapter adapter;
    Query query;
    FirestoreRecyclerOptions<SlotModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_room);

        titleTV = findViewById(R.id.CategoryTitle);
        setDateBtn = findViewById(R.id.DateSetBtn);
        setBlockBtn = findViewById(R.id.Name);
        searchBtn = findViewById(R.id.searchBtn);
       // recyclerView = findViewById(R.id.recylcerView2);

        fireStore = FirebaseFirestore.getInstance();

        setDateBtn.setOnClickListener(this);
        setBlockBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);

        setTitle();

        roomCollection = fireStore.collection("classroom")
                .document("block")
                .collection("A");

        //retrieved data from firebase
         query = roomCollection.orderBy("roomNo",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<SlotModel> options = new FirestoreRecyclerOptions.Builder<SlotModel>().setQuery(query , SlotModel.class).build();

        adapter = new UserSlotAdapter(options);

        recyclerView = findViewById(R.id.recylcerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        DialogBoxRB.setOnCLickBlockOptn(new DialogBoxRB.onClickBlocks() {
            @Override
            public void setBlockName(String blockName) {
                setBlockBtn.setText(blockName);
            }
        });


        UserSlotAdapter.setOnCLickRoom(new UserSlotAdapter.onClickRoom() {
            @Override
            public void openForm(String slots , String roomNo) {
                Intent intent = new Intent(SortRoomActivity.this , FormActivity.class);
                intent.putExtra("room" , roomNo);
                intent.putExtra("date" , formatDate());
                intent.putExtra("block" , setBlockBtn.getText());

                RoomDetails.roomBlock = setBlockBtn.getText().toString();
                RoomDetails.roomNumber = roomNo;
                startActivity(intent);
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
            case R.id.Name: chooseBlock(); break;
            case R.id.searchBtn: search(); break;
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


    public void search() {

         final String currentDate, currentBlock;
        currentDate = setDateBtn.getText().toString();
        currentBlock = setBlockBtn.getText().toString();
        dateCollectionDocument = fireStore.collection("Date")
                .document(currentDate)
                .collection(RoomDetails.roomType)
                .document("block");

        //select date and block first
        if(currentDate.equals("Date") && currentBlock.equals("Block")){
            Toast.makeText(this, "Select Date and Block", Toast.LENGTH_SHORT).show();
            return;
        }

        //uncomment
        dateCollectionDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    //if collection on selected date existing
                    Toast.makeText(SortRoomActivity.this, "collection exists", Toast.LENGTH_SHORT).show();
                    onDateCollectionPresent(dateCollectionDocument , currentDate , currentBlock);
                }
                else {
                    //display All category rooms
                    String[] blockCharArray = setBlockBtn.getText().toString().split("-");
                    String block = blockCharArray[0];

                     roomCollection = fireStore.collection(RoomDetails.roomType)
                            .document("block")
                            .collection(block);

                    query = roomCollection.orderBy("roomNo", Query.Direction.ASCENDING);
                    options = new FirestoreRecyclerOptions.Builder<SlotModel>()
                            .setQuery(query, SlotModel.class)
                            .build();

                    adapter = new UserSlotAdapter(options);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();

                    Toast.makeText(SortRoomActivity.this, "display all", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void onDateCollectionPresent(DocumentReference dateCollectionDocument
            , String currentDate
            , String currentBlock) {

         roomCollection = dateCollectionDocument.collection(currentBlock);

        if(roomCollection.get().getResult().isEmpty()){
            Toast.makeText(this, "Error : 404", Toast.LENGTH_SHORT).show();
            return;
        }

        query = roomCollection.orderBy("roomNo", Query.Direction.ASCENDING);
        options = new FirestoreRecyclerOptions.Builder<SlotModel>()
                .setQuery(query, SlotModel.class)
                .build();

        adapter = new UserSlotAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}