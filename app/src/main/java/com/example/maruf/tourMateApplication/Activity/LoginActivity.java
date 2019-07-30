package com.example.maruf.tourMateApplication.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maruf.tourMateApplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity {

    private TextView signUpTv;
    private TextInputLayout emailTextInput;
    private TextInputLayout passwordTextInput;
    private AppCompatButton login;
    private FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signUpTv = findViewById(R.id.link_signup);
        emailTextInput = findViewById(R.id.input_email);
        passwordTextInput = findViewById(R.id.input_password);
        login = findViewById(R.id.btn_login);
        firebaseAuth = FirebaseAuth.getInstance();
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail() | !validatePassword()){
                    return;
                }
                String password = passwordTextInput.getEditText().getText().toString().trim();
                String email = emailTextInput.getEditText().getText().toString().trim();
                signInWithEmailAndPassword(email,password);
            }
        });
        if (firebaseAuth.getCurrentUser()!=null){
            gotToHome();
        }


    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    private void gotToHome() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private void signInWithEmailAndPassword(String email,String password) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating....");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toasty.info(LoginActivity.this,"Welcome Back",Toast.LENGTH_SHORT,false).show();
                    gotToHome();
                    }else{
                    Toasty.warning(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT,false).show();
                    progressDialog.dismiss();
                }

            }
        });
    }

    public boolean validateEmail(){
        String emailInput = emailTextInput.getEditText().getText().toString().trim();
        if(emailInput.isEmpty()){
            emailTextInput.setError("Field can not be empty");
            return  false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            emailTextInput.setError("Please enter a valid email address");
            return false;
        }
        else{
            emailTextInput.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){
        String passwordInput = passwordTextInput.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()){
         passwordTextInput.setError("Field can not be empty");
         return false;
     }else if(passwordInput.length() < 6) {
         passwordTextInput.setError("Password length must be greater than 6");
        return false;
     }else{
         passwordTextInput.setError(null);
         return true;
     }
    }

}
