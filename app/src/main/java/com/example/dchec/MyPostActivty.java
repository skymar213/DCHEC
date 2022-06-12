package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyPostActivty extends AppCompatActivity {
    ImageView postImg;
    Button btnBack;
    TextView txtPoster,txtTitle,txtDescription;
    private String postKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_activty);

        postKey = getIntent().getStringExtra("postKey");
        txtDescription = findViewById(R.id.txtDescription);
        postImg = findViewById(R.id.imgPostActivity);
        txtPoster = findViewById(R.id.txtPostPoster);
        txtTitle = findViewById(R.id.txtTitle);
        btnBack = findViewById(R.id.btnBackArrow);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("profilePosts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String description = snapshot.child("description").getValue().toString();
                String image = snapshot.child("postImage").getValue().toString();
                String title = snapshot.child("title").getValue().toString();
                String poster = snapshot.child("userName").getValue().toString();

                txtDescription.setText(description);
                Picasso.get().load(image).into(postImg);
                txtTitle.setText(title);
                txtPoster.setText(poster);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}