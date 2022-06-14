package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HisAssoProfile extends AppCompatActivity {

    TextView txtHisUsername;
    Button btnContacter;
    RecyclerView recyclerHisPost;
    String clickedUid ="" ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_asso_profile);

        recyclerHisPost = findViewById(R.id.recyclerHisAssoPosts);
        clickedUid =getIntent().getStringExtra("uid");
        recyclerHisPost.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));

        txtHisUsername = findViewById(R.id.txtYourUsernamePost);
        btnContacter = findViewById(R.id.btnContacter);

        btnContacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),MessageActivity.class);
                intent.putExtra("nom_of_roommate",txtHisUsername.getText().toString());
                intent.putExtra("email_of_roommate2",clickedUid);
                startActivity(intent);
            }
        });




        txtHisUsername.setText( getIntent().getStringExtra("nom_of_roommate"));


DisplayAllAssociationPosts();


    }

    private void DisplayAllAssociationPosts() {

        if (clickedUid != null) {


            FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(FirebaseDatabase.getInstance().getReference().child("associationProfilePosts").child(clickedUid), Posts.class).build();

            FirebaseRecyclerAdapter<Posts, HisProfile.UserPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, HisProfile.UserPostViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull HisProfile.UserPostViewHolder holder, int position, @NonNull Posts model) {

                    String postKey = getRef(position).getKey();

                    holder.setUserName(model.getUserName());
                    holder.setPost_Image(model.getPostImage());
                    holder.setTitle(model.getTitle());
                    holder.setPrice(model.getPrice());

                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent clickPost = new Intent(HisAssoProfile.this, PostActivity.class);
                            clickPost.putExtra("postKey", postKey);
                            startActivity(clickPost);
                        }
                    });


                }

                @NonNull
                @Override
                public HisProfile.UserPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.posts_view_holder, parent, false);
                    return new HisProfile.UserPostViewHolder(view);
                }
            };

            firebaseRecyclerAdapter.startListening();
            recyclerHisPost.setAdapter(firebaseRecyclerAdapter);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        DisplayAllAssociationPosts();
    }

    @Override
    public void onStart() {
        super.onStart();
DisplayAllAssociationPosts();    }

}