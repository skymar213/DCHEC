package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private ImageView arrowBack ;

    private EditText userName , userNickName , userEmail , userPassword , userPhoneNumber , userLocalisation ;
    private Button saveBtn ;

    private FirebaseAuth mAuth;
    private DatabaseReference accountUserRef;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        accountUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

        String email = mAuth.getCurrentUser().getEmail();

        userName = findViewById(R.id.account_user_name);
        userNickName = findViewById(R.id.account_user_nickname);
        userEmail = findViewById(R.id.account_user_email);
        userPassword = findViewById(R.id.account_user_password);
        userPhoneNumber = findViewById(R.id.account_user_phone_number);
        userLocalisation = findViewById(R.id.account_user_localisation);

        userEmail.setText(email);

        saveBtn = findViewById(R.id.account_save_button);

        arrowBack = findViewById(R.id.account_back_arrow);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        accountUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String myName = snapshot.child("userName").getValue().toString();
                String myNickName = snapshot.child("nickName").getValue().toString();
                String myPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                String myLocalisation = snapshot.child("localisation").getValue().toString();
                String myPassword = snapshot.child("password").getValue().toString();


                userName.setText(myName);
                userNickName.setText(myNickName);
                userPhoneNumber.setText(myPhoneNumber);
                userLocalisation.setText(myLocalisation);
                userPassword.setText(myPassword);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}