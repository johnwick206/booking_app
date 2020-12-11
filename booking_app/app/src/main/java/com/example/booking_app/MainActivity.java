package com.example.booking_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking_app.Admin.AdminMain;
import com.example.booking_app.User.CategoryActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private Button loginBtn;
	private TextView moveToSignUp , forgotPassword;
	private EditText emailT , passwordT;

	private FirebaseAuth mAuth;
	private ProgressDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mAuth = FirebaseAuth.getInstance();


		referenceUi();
		setClickListener();
	}

	private void referenceUi() {

		emailT = findViewById(R.id.emailED);
		passwordT = findViewById(R.id.PassWordED);

		loginBtn = findViewById(R.id.LoginBtn);
		moveToSignUp = findViewById(R.id.MoveToSIgnUp);
		forgotPassword = findViewById(R.id.forgotPassword);
	}

	private void setClickListener() {

		loginBtn.setOnClickListener(this);
		moveToSignUp.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.LoginBtn : login(); break;
			case R.id.MoveToSIgnUp : moveTosignUpPage(); break;
			case R.id.forgotPassword : forgetPass(); break;
		}
	}

	private void login() {

		final String emailS , passwordS;
		emailS = emailT.getText().toString().trim();
		passwordS = passwordT.getText().toString().trim();

		if(TextUtils.isEmpty(emailS)){
			emailT.setError("Enter valid Email-id");
			return;
		}
		if(TextUtils.isEmpty(passwordS)){
			passwordT.setError("Enter password");
			return;
		}


		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Login");
		progressDialog.setMessage("Getting in....");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		mAuth.signInWithEmailAndPassword(emailS , passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {

				if(task.isSuccessful()){
					progressDialog.dismiss();

					// TODO: 03-12-2020 Check admin or not
					if(isAdmin(emailS , passwordS)) {
						startActivity(new Intent(MainActivity.this , AdminMain.class));
						finish();
						return;
					}

					startActivity(new Intent(MainActivity.this , CategoryActivity.class));
					Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
					finish();
				}else {
					progressDialog.dismiss();
					Toast.makeText(MainActivity.this, "Check Credentials" + task.getException(), Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private boolean isAdmin(String email , String password){
		return email.equals("adminvit2125@gmail.com") && password.equals("123456");
	}


	private void moveTosignUpPage() {
		startActivity(new Intent(MainActivity.this , SignUp.class));
	}


	//Password Reset
	private void forgetPass(){
		final EditText resetMail=new EditText(this);
		AlertDialog.Builder passwordResetDialogue = new AlertDialog.Builder(this);
		passwordResetDialogue.setTitle("Reset Password!!");
		passwordResetDialogue.setMessage("Enter Email to recieve link for Reset :");
		passwordResetDialogue.setView(resetMail);
		passwordResetDialogue.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

				String mail=resetMail.getText().toString();
				mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Toast.makeText(MainActivity.this,"Reset Link sent",Toast.LENGTH_SHORT).show();
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(MainActivity.this,"Error,no reset link Sent",Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
		passwordResetDialogue.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

			}
		});
		passwordResetDialogue.create().show();
	}

}
