package com.example.booking_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booking_app.User.CategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

   private EditText firstNameT , lastNameT , passwordT;
   private TextView emailT;
   private Button signUpBtn ;
   private FirebaseAuth mAuth;
   private ProgressDialog progressDialog;
   private String firstNameS , lastNameS, emailS , passwordS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        refUI();
        signUpBtn.setOnClickListener(this);

        //on close verifiction dialog

        VerifyMailDialogBox.setOnCLickOk(new VerifyMailDialogBox.onCLickOk() {
            @Override
            public void checkStatus() {
                checkVerifyStatus();
            }
        });

        FixedUpdate();
    }

    private void refUI() {

        firstNameT = findViewById(R.id.UserFirstNameED);
        lastNameT = findViewById(R.id.UserLastNameED);
        emailT = findViewById(R.id.EmailED);
        passwordT = findViewById(R.id.PasswordED);
        signUpBtn = findViewById(R.id.SignUpBtn);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.SignUpBtn) {
            signUpWithEmail();
        }
    }

    private void FixedUpdate() {
        final Handler handler =new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                handler.postDelayed(this, 1000);
                firstNameS = firstNameT.getText().toString().trim();
                lastNameS = lastNameT.getText().toString().trim();

                emailS = firstNameS.toLowerCase() +"." + lastNameS.toLowerCase() +"@vit.edu.in";
                emailT.setText(emailS);
            }
        };
        handler.postDelayed(r , 1000);
    }

    private void signUpWithEmail() {

        passwordS = passwordT.getText().toString().trim();

        if(TextUtils.isEmpty(firstNameS)){
            firstNameT.setError("Enter name");
            return;
        }

        if(TextUtils.isEmpty(lastNameS)){
            firstNameT.setError("Enter name");
            return;
        }

        if(TextUtils.isEmpty(emailS)){
            emailT.setError("Enter valid email-id");
            return;
        }

        if(TextUtils.isEmpty(passwordS)){
            passwordT.setError("Enter password");
            return;
        }

       /* progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Initializing....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/

        //create new Account
        mAuth.createUserWithEmailAndPassword(emailS , passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    sendVerification();
                }else {
                   // progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Sign Up failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void sendVerification(){
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) checkVerifyStatus();
                else
                    Toast.makeText(SignUp.this, "Failed to share Verification mail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkVerifyStatus(){
       final FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!user.isEmailVerified()){

                    VerifyMailDialogBox verifyMailDialogBox = new VerifyMailDialogBox();
                    verifyMailDialogBox.show(getSupportFragmentManager() , "not verified");
                }else{

                    startActivity(new Intent(SignUp.this, CategoryActivity.class));
                    Toast.makeText(SignUp.this, "Sign Up complete", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

}