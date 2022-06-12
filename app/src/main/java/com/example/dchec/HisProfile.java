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

public class HisProfile extends AppCompatActivity {

    TextView txtHisUsername;
    Button btnContacter;
    RecyclerView recyclerHisPost;
    String clickedUid ="" ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_profile);

        recyclerHisPost = findViewById(R.id.recyclerHisPosts);
        clickedUid =getIntent().getStringExtra("uid");
        recyclerHisPost.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));

        txtHisUsername = findViewById(R.id.txtYourUsernamePost);
        btnContacter = findViewById(R.id.btnContacter);

        btnContacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),MessageActivity.class);
                intent.putExtra("nom_of_roommate",txtHisUsername.getText().toString());
                startActivity(intent);
            }
        });

        recyclerHisPost = findViewById(R.id.recyclerHisPosts);



        txtHisUsername.setText( getIntent().getStringExtra("nom_of_roommate"));


        DisplayAllHisUserPosts();



    }

    private void DisplayAllHisUserPosts() {

        if (clickedUid != null) {


            FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(FirebaseDatabase.getInstance().getReference().child("profilePosts").child(clickedUid), Posts.class).build();

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
                            Intent clickPost = new Intent(HisProfile.this, PostActivity.class);
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

    public static class UserPostViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public UserPostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;


        }

        public void setUserName(String user_Name) {
            TextView userName = mView.findViewById(R.id.txtUsernamePost);
            userName.setText(user_Name);
        }

        public void setTitle(String title) {
            TextView userName = mView.findViewById(R.id.txtPostTitle);
            userName.setText(title);
        }

        public void setPrice(String price) {
            TextView userPrice = mView.findViewById(R.id.txtPrice);
            userPrice.setText(price);

        }

        public void setPost_Image(String post_Image) {
            ImageView PostImage = mView.findViewById(R.id.imgPost);
            Picasso.get().load(post_Image).error(R.drawable.image_error).placeholder(R.drawable.image_error).into(PostImage);
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayAllHisUserPosts();
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayAllHisUserPosts();
    }

}