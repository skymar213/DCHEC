package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private DatabaseReference userRef;
    private String currentUserId;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logoutBtn = findViewById(R.id.log_out_btn);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null){
            sendToLogInActivity();
        } else {
            CheckUserExistence();
            currentUserId = mAuth.getCurrentUser().getUid();
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                sendToLogInActivity();
            }
        });


    }


    private void sendToLogInActivity() {
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void CheckUserExistence() {


        final String current_user_id = mAuth.getCurrentUser().getUid();


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.hasChild(current_user_id))){
                    sendUserToSetUpActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendUserToSetUpActivity() {
        Intent setUpIntent = new Intent(HomeActivity.this , setUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setUpIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null){
            sendToLogInActivity();
        } else {
            CheckUserExistence();
        }
    }
}