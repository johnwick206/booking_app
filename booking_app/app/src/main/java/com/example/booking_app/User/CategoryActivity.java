package com.example.booking_app.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.booking_app.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private View clsRoomCard, labCard, seminarCard;
    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        referenceUI();
        setClickListeners();

        setImagesInSlider();
    }

    private void referenceUI() {
        clsRoomCard = findViewById(R.id.classroomCard);
        labCard = findViewById(R.id.LabCard);
        seminarCard = findViewById(R.id.SeminarCard);
        imageSlider = findViewById(R.id.imageSlider);
    }

    private void setClickListeners() {
        clsRoomCard.setOnClickListener(this);
        labCard.setOnClickListener(this);
        seminarCard.setOnClickListener(this);
    }

    private void setImagesInSlider() {
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel("https://getmyuni.azureedge.net/college-image/big/vidyalankar-institute-of-technology-vit-mumbai.jpg"
                , "Main Building"));
        imageList.add(new SlideModel("https://images.shiksha.com/mediadata/images/1516603414phpaqoEBV.jpeg"
                , "Auditorium"));
        imageList.add(new SlideModel("https://viie.edu.in/wp-content/uploads/2019/03/Vidyalankar-Institute-for-International-Education9.jpg"
                , "Seminar Hall"));
        imageList.add(new SlideModel("https://lh3.googleusercontent.com/proxy/eokKpxqMcTnvNtkbyKpF8h0GSxQkySmhn8nCGlCVmnos0b5T3QW39UJ4HhByzA7hVPSjY72R6HJxR2Q0ehLa4SPCoTa9fvimsRXaHbDFSP6k27F_lmnNksCcnw"
        ,"Labs"));
        imageList.add(new SlideModel("https://vidyalankar.edu.in/wp-content/uploads/2017/02/Classroom.jpg"
        ,"ClassRoom"));


        imageSlider.setImageList(imageList , true);
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
        Intent intent = new Intent(CategoryActivity.this , SortRoomActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(CategoryActivity.this
                , view
                , "cardTransition");

        intent.putExtra("Name" , name);
        startActivity(intent , optionsCompat.toBundle());
    }
}