package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private ImageButton arrowBack ;
    private EditText userEmail ;
    private Button btnPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        arrowBack = findViewById(R.id.btnBack);
        userEmail = findViewById(R.id.user_email_forget_password);
        btnPassword = findViewById(R.id.forget_password_btn);
        firebaseAuth = FirebaseAuth.getInstance();


        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBackToLoginActivity();
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowWritingNewPassword();
            }
        });
    }

    private void AllowWritingNewPassword() {
        String email = userEmail.getText().toString();

        if (email.isEmpty()){
            userEmail.setError("Obligatoire");
        }else{
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgetPassword.this, "verifier votre email", Toast.LENGTH_SHORT).show();
                        SendUserToLogInActivity();
                    }else{
                        Toast.makeText(ForgetPassword.this, "eurror : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void SendUserToLogInActivity() {
        Intent intent = new Intent(ForgetPassword.this , LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void sendBackToLoginActivity() {
        onBackPressed();
    }
}