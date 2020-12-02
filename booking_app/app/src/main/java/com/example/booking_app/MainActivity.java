package com.example.booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private Button googleLoginBtn , twitterLoginBtn , fbLoginBtn , loginBtn;
	private TextView moveToSignUp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		referenceUi();
		setClickListener();
	}

	private void referenceUi() {
		loginBtn = findViewById(R.id.LoginBtn);
		googleLoginBtn = findViewById(R.id.GoogleLoginBtn);
		twitterLoginBtn = findViewById(R.id.TwitterLoginBtn);
		fbLoginBtn = findViewById(R.id.FBLoginBtn);
		moveToSignUp = findViewById(R.id.MoveToSIgnUp);
	}

	private void setClickListener() {

		loginBtn.setOnClickListener(this);
		googleLoginBtn.setOnClickListener(this);
		twitterLoginBtn.setOnClickListener(this);
		fbLoginBtn.setOnClickListener(this);
		moveToSignUp.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.LoginBtn : login(); break;
			case R.id.GoogleLoginBtn : googleLogin();  break;
			case R.id.TwitterLoginBtn : twitterLogin(); break;
			case R.id.FBLoginBtn : fbLogin(); break;
			case R.id.MoveToSIgnUp : moveTosignUpPage(); break;
		}
	}

	private void login() {
		startActivity(new Intent(MainActivity.this , CategoryActivity.class));
	}

	private void googleLogin() {
		startActivity(new Intent(MainActivity.this , AdminMain.class));
	}

	private void twitterLogin() {
	}

	private void fbLogin() {
	}

	private void moveTosignUpPage() {
		startActivity(new Intent(MainActivity.this , SignUp.class));
	}
}