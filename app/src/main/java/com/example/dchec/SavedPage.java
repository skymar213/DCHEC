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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SavedPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    String currentUserId;
    DatabaseReference savedPostRef;
    ImageButton arrowBack ;
    RecyclerView savedPostsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_page);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        savedPostRef = FirebaseDatabase.getInstance().getReference().child("savedPosts").child(currentUserId);

        arrowBack = findViewById(R.id.save_page_back_arrow);
        savedPostsRecycler = findViewById(R.id.saved_post_recycler_view);


        savedPostsRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SavedPage.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        savedPostsRecycler.setLayoutManager(linearLayoutManager);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        DisplayAllUserPosts();


    }

    private void DisplayAllUserPosts() {

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(savedPostRef, Posts.class).build();

        FirebaseRecyclerAdapter<Posts , PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options)


        {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model) {

                String postKey = getRef(position).getKey();

                holder.setUser_Name(model.getUserName());
                holder.setPost_Image(model.getPostImage());
                holder.setTitle(model.getTitle());


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent clickPost = new Intent(SavedPage.this,PostActivity.class);
                        clickPost.putExtra("postKey",postKey);
                        startActivity(clickPost);
                    }
                });



            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(SavedPage.this).inflate(R.layout.save_post_holder,parent,false);
                return new PostViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        savedPostsRecycler.setAdapter(firebaseRecyclerAdapter);
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setUser_Name(String user_Name){
            TextView userName = mView.findViewById(R.id.saved_post_username);
            userName.setText(user_Name);
        }

        public void setTitle(String title){
            TextView savedTitle = mView.findViewById(R.id.saved_post_title);
            savedTitle.setText(title);
        }


        public void setPost_Image( String post_Image){
            ImageView PostImage = mView.findViewById(R.id.saved_post_image);
            Picasso.get().load(post_Image).error(R.drawable.image_error).placeholder(R.drawable.image_error).into(PostImage);
        }



    }
}