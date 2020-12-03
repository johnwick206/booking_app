package com.example.booking_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

   private EditText nameT , emailT , passwordT;
   private Button signUpBtn ,googleBtn , twitterBtn , fbBtn;
   private FirebaseAuth mAuth;
   private ProgressDialog progressDialog;

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
    }

    private void refUI() {

        nameT = findViewById(R.id.UserNameED);
        emailT = findViewById(R.id.EmailED);
        passwordT = findViewById(R.id.PasswordED);
        signUpBtn = findViewById(R.id.SignUpBtn);
        /*googleBtn = findViewById(R.id.GoogleLoginBtn);
        twitterBtn = findViewById(R.id.TwitterLoginBtn);
        fbBtn = findViewById(R.id.FBLoginBtn);*/

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.SignUpBtn) {
            signUpWithEmail();
        }
    }

    private void signUpWithEmail() {

        String nameS , emailS , passwordS;
        nameS = nameT.getText().toString();
        emailS = emailT.getText().toString().trim();
        passwordS = passwordT.getText().toString().trim();

        if(TextUtils.isEmpty(nameS)){
            nameT.setError("Enter name");
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

    @Override
    public void onBackPressed() {
        checkVerifyStatus();
    }
}