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

import com.example.maruf.tourMateApplication.OtherClass.DatabaseRef;
import com.example.maruf.tourMateApplication.R;
import com.example.maruf.tourMateApplication.ProjoPackage.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;


public class SignUpActivity extends AppCompatActivity {
    private TextView loginTv ;
    private AppCompatButton signUpBtn;
    private TextInputLayout emailTextInput;
    private TextInputLayout passwordTextInput;
    private TextInputLayout re_EnterPasswordTextInput;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loginTv = findViewById(R.id.link_login);
        signUpBtn = findViewById(R.id.btn_signup);
        emailTextInput = findViewById(R.id.input_email);
        passwordTextInput = findViewById(R.id.input_password);
        re_EnterPasswordTextInput = findViewById(R.id.input_reEnterPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();


            }
        });

         loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void confirmInput() {
        if(!validateEmail() | !validatePassword()){
            return;
        }
        String password = passwordTextInput.getEditText().getText().toString().trim();
        String email = emailTextInput.getEditText().getText().toString().trim();
        signUpWithEmailAndPassword(email,password);
    }

    private void signUpWithEmailAndPassword(final String email, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering....");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userId = firebaseAuth.getCurrentUser().getUid();
                    Users user = new Users(email,password);
                    DatabaseRef.userRef.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toasty.info(SignUpActivity.this,"Sign up Completed Successfully",Toast.LENGTH_SHORT,false).show();
                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                progressDialog.dismiss();
                                Toasty.warning(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT,false).show();
                            }

                        }
                    });

                }else{
                    progressDialog.dismiss();
                    Toasty.warning(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT,false).show();

                }
            }
        });

    }

    private boolean validateEmail(){
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

    private boolean validatePassword(){
        String passwordInput = passwordTextInput.getEditText().getText().toString().trim();
        String reEnterPasswordInput = re_EnterPasswordTextInput.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()){
            passwordTextInput.setError("Field can not be empty");
            return false;
        }else if(passwordInput.length()<6) {
            passwordTextInput.setError("Password length must be greater than 6");
            return false;
        }else if(!passwordInput.equals(reEnterPasswordInput)){
            passwordTextInput.setError("Passwords don't match");
            re_EnterPasswordTextInput.setError("Passwords don't match");
            return false;
        }
        else{
            passwordTextInput.setError(null);
            return true;
        }
    }





}
