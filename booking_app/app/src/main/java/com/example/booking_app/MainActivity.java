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

	private Button googleLoginBtn , loginBtn;
	private TextView moveToSignUp , forgotPassword;
	private EditText emailT , passwordT;

	private FirebaseAuth mAuth;
	private ProgressDialog progressDialog;

	GoogleSignInClient mGoogleSignInClient;
	private String TAG = "MainActivity";
	private int RC_SIGN_IN = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mAuth = FirebaseAuth.getInstance();

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

		referenceUi();
		setClickListener();
	}

	private void referenceUi() {

		emailT = findViewById(R.id.emailED);
		passwordT = findViewById(R.id.PassWordED);

		loginBtn = findViewById(R.id.LoginBtn);
		googleLoginBtn = findViewById(R.id.GoogleLoginBtn);
		moveToSignUp = findViewById(R.id.MoveToSIgnUp);
		forgotPassword = findViewById(R.id.forgotPassword);
	}

	private void setClickListener() {

		loginBtn.setOnClickListener(this);
		googleLoginBtn.setOnClickListener(this);
		moveToSignUp.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.LoginBtn : login(); break;
			case R.id.GoogleLoginBtn : googleLogin();  break;
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

	private void googleLogin() {
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent,RC_SIGN_IN);
	}

	private void moveTosignUpPage() {
		startActivity(new Intent(MainActivity.this , SignUp.class));
	}


	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == RC_SIGN_IN){
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			handlesSignInResult(task);
		}
	}
	private void  handlesSignInResult (Task<GoogleSignInAccount> completedTask){
		try {
			GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
			Toast.makeText(MainActivity.this, "Signed in Successfully", Toast.LENGTH_SHORT).show();
			FirebaseGoogleAuth(acc);
		} catch (ApiException e) {
			Toast.makeText(MainActivity.this, "Signed in Failed",Toast.LENGTH_SHORT).show();
			FirebaseGoogleAuth(null);

		}
	}

	private void FirebaseGoogleAuth(GoogleSignInAccount acct){
		AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if(task.isSuccessful()){
					Toast.makeText(MainActivity.this, "Success",Toast.LENGTH_SHORT).show();
					FirebaseUser user = mAuth.getCurrentUser();
					updateUI(user);

				}
				else{
					Toast.makeText(MainActivity.this, "Failed",Toast.LENGTH_SHORT).show();
					updateUI(null);
				}

			}
		});



	}
	private void updateUI(FirebaseUser fUser){
		//next page code
		GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
		if (account != null){
			String personName = account.getDisplayName();
			String personGivenName = account.getGivenName();
			String personFamilyName = account.getFamilyName();
			String personEmail = account.getEmail();
			String personId = account.getId();

			Toast.makeText(MainActivity.this, personName + personEmail, Toast.LENGTH_SHORT).show();
		}

		referenceUi();
		setClickListener();
		startActivity(new Intent(MainActivity.this , CategoryActivity.class));
		finish();
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
