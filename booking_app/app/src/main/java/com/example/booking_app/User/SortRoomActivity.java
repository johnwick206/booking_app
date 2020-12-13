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
import com.example.booking_app.FormatFields;
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
    CollectionReference roomCollection , dateCollection;
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

        dateCollection = fireStore.collection("Date")
                .document("03-12-2020")
                .collection(RoomDetails.roomType)
                .document("block").collection("A");

        roomCollection = fireStore.collection("classroom")
                .document("block")
                .collection("A");

        //retrieved data from firebase
         query = roomCollection.orderBy("roomNo",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<SlotModel> options = new FirestoreRecyclerOptions.Builder<SlotModel>().setQuery(query , SlotModel.class).build();

        adapter = new UserSlotAdapter(options);

        recyclerView = findViewById(R.id.recylcerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();


        DialogBoxRB.setOnCLickBlockOptn(new DialogBoxRB.onClickBlocks() {
            @Override
            public void setBlockName(String blockName) {
                setBlockBtn.setText(blockName);
            }
        });


        //onclick room to open form
        UserSlotAdapter.setOnCLickRoom(new UserSlotAdapter.onClickRoom() {
            @Override
            public void openForm(String slots , String roomNo) {
                Intent intent = new Intent(SortRoomActivity.this , FormActivity.class);
                intent.putExtra("room" , roomNo);
                intent.putExtra("date" , RoomDetails.date);
                intent.putExtra("block" , RoomDetails.roomBlock);
                intent.putExtra("slots" , slots);

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

        month = month + 1;

        //date format yyyy-m-d
        String occasionDate = year + "-" + month + "-" + day;
        RoomDetails.date = occasionDate;

        Toast.makeText(this, occasionDate, Toast.LENGTH_SHORT).show();

        //date visible to user
        String visibleDate = day + "-" + month + "-" + year;
        setDateBtn.setText(visibleDate);
    }

    //on click specific room card
    private void openRoom( CardView card,String name) {
        Intent intent = new Intent(SortRoomActivity.this , FormActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SortRoomActivity.this , card , "cardTransition2");
        intent.putExtra("room" , name);
        intent.putExtra("date" , RoomDetails.date);
        intent.putExtra("block" , setBlockBtn.getText());


        RoomDetails.roomBlock = setBlockBtn.getText().toString();
        RoomDetails.roomNumber = name;
        startActivity(intent,optionsCompat.toBundle());
    }


    public void search() {

         final String currentDate, currentBlock;
        currentDate = setDateBtn.getText().toString();
        currentBlock = setBlockBtn.getText().toString();

        //select date and block first
        if(currentDate.equals("Date") && currentBlock.equals("BLOCK")){
            Toast.makeText(this, "Select Date and Block", Toast.LENGTH_SHORT).show();
            return;
        }

        //format block and date
        String[] blockSortArray = currentBlock.split("-");
        RoomDetails.roomBlock = blockSortArray[0];


        dateCollectionDocument = fireStore.collection("Date")
                .document(RoomDetails.date)
                .collection(RoomDetails.roomType)
                .document("block");


        //uncomment
        dateCollectionDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    //if collection on selected date existing
                    onDateCollectionPresent(dateCollectionDocument , currentDate , currentBlock);
                }
                else {
                    //display All category rooms

                     roomCollection = fireStore.collection(RoomDetails.roomType)
                            .document("block")
                            .collection(RoomDetails.roomBlock);

                    query = roomCollection.orderBy("roomNo", Query.Direction.ASCENDING);
                    options = new FirestoreRecyclerOptions.Builder<SlotModel>()
                            .setQuery(query, SlotModel.class)
                            .build();

                    adapter = new UserSlotAdapter(options);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                    adapter.notifyDataSetChanged();

                    Toast.makeText(SortRoomActivity.this, "display all", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void onDateCollectionPresent(DocumentReference dateCollectionDocument
            , String currentDate
            , String currentBlock) {

        dateCollection = fireStore.collection("Date")
                .document(RoomDetails.date)
                .collection(RoomDetails.roomType)
                .document("block").collection(RoomDetails.roomBlock);


       dateCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.getResult().isEmpty()){

                    query = dateCollection.orderBy("roomNo", Query.Direction.ASCENDING);
                    options = new FirestoreRecyclerOptions.Builder<SlotModel>()
                            .setQuery(query, SlotModel.class)
                            .build();

                    adapter = new UserSlotAdapter(options);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                    adapter.notifyDataSetChanged();


                    Toast.makeText(SortRoomActivity.this, "collection exists", Toast.LENGTH_SHORT).show();
                }
                else{
                    roomCollection = fireStore.collection(RoomDetails.roomType)
                            .document("block")
                            .collection(RoomDetails.roomBlock);

                    query = roomCollection.orderBy("roomNo", Query.Direction.ASCENDING);
                    options = new FirestoreRecyclerOptions.Builder<SlotModel>()
                            .setQuery(query, SlotModel.class)
                            .build();

                    adapter = new UserSlotAdapter(options);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                    adapter.notifyDataSetChanged();

                    Toast.makeText(SortRoomActivity.this, "display all : no room booked", Toast.LENGTH_SHORT).show();
                }
           }
       });

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