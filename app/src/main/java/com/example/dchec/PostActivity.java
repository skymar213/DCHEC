package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    ImageView postImg , savingPost;
    Button btnBack,btnContacter;
    TextView txtPoster,txtTitle,txtDescription,txtPrice;
    FirebaseAuth mAuth;
    String currentUserId ;
    DatabaseReference savedPostRef;
    private String postKey,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);



        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        savedPostRef = FirebaseDatabase.getInstance().getReference().child("savedPosts").child(currentUserId);

        postKey = getIntent().getStringExtra("postKey");
        uid = getIntent().getStringExtra("uid");
        txtDescription = findViewById(R.id.txtDescription);
        postImg = findViewById(R.id.imgPostActivity);
        txtPoster = findViewById(R.id.txtPostPoster);
        txtTitle = findViewById(R.id.txtTitle);
        btnBack = findViewById(R.id.btnBackArrow);
        btnContacter = findViewById(R.id.btnContacterPost);
        savingPost = findViewById(R.id.saving_post);



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (HisProfile.b){
            FirebaseDatabase.getInstance().getReference().child("profilePosts").child(uid).child(postKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()){
                        String description = snapshot.child("description").getValue().toString();
                        String image = snapshot.child("postImage").getValue().toString();
                        String title = snapshot.child("title").getValue().toString();
                        String poster = snapshot.child("userName").getValue().toString();

                        txtDescription.setText(description);
                        Picasso.get().load(image).into(postImg);
                        txtTitle.setText(title);
                        txtPoster.setText(poster);
                        HisProfile.b = false;
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            FirebaseDatabase.getInstance().getReference().child("posts").child(postKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()){
                        String description = snapshot.child("description").getValue().toString();
                        String image = snapshot.child("postImage").getValue().toString();
                        String title = snapshot.child("title").getValue().toString();
                        String poster = snapshot.child("userName").getValue().toString();

                        txtDescription.setText(description);
                        Picasso.get().load(image).into(postImg);
                        txtTitle.setText(title);
                        txtPoster.setText(poster);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        btnContacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this,MessageActivity.class);
                intent.putExtra("nom_of_roommate",txtPoster.getText().toString());
                startActivity(intent);
            }
        });

        savingPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostActivity.this, "element enregistrer", Toast.LENGTH_SHORT).show();

                FirebaseDatabase.getInstance().getReference().child("posts").child(postKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String description = snapshot.child("description").getValue().toString();
                        String image = snapshot.child("postImage").getValue().toString();
                        String title = snapshot.child("title").getValue().toString();
                        String poster = snapshot.child("userName").getValue().toString();

                        HashMap hashMap = new HashMap();
                        hashMap.put("postImage" , image);
                        hashMap.put("title" , title);
                        hashMap.put("userName" , poster);
                        hashMap.put("description" , description);

                        savedPostRef.child(postKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(PostActivity.this, "your post has been saved on fire base", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }
}