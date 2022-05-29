package com.example.dchec;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    TextView txtUsername, txtPhoneNum;
    Button btnContacter;
    RecyclerView recyclerPost;

    DatabaseReference userRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        recyclerPost = view.findViewById(R.id.recyclerPosts);
        recyclerPost.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        DisplayAllUserPosts();
        txtPhoneNum = view.findViewById(R.id.txtPhoneNum);
        txtUsername = view.findViewById(R.id.txtUsername);
        btnContacter = view.findViewById(R.id.btnContacter);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("userName").getValue().toString();
                    txtUsername.setText(username);

                    String phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                    txtPhoneNum.setText(phoneNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }


    private void DisplayAllUserPosts() {

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(FirebaseDatabase.getInstance().getReference().child("profilePosts"), Posts.class).build();

        FirebaseRecyclerAdapter<Posts, ProfileFragment.UserPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, UserPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProfileFragment.UserPostViewHolder holder, int position, @NonNull Posts model) {

                holder.setUserName(model.getUserName());
                holder.setPost_Image(model.getPostImage());
                holder.setTitle(model.getTitle());
                holder.setPrice(model.getPrice());


            }

            @NonNull
            @Override
            public ProfileFragment.UserPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.posts_view_holder, parent, false);
                return new ProfileFragment.UserPostViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerPost.setAdapter(firebaseRecyclerAdapter);
    }
    public static class UserPostViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public UserPostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setUserName(String user_Name){
            TextView userName = mView.findViewById(R.id.txtUsername);
            userName.setText(user_Name);
        }

        public void setTitle(String title){
            TextView userName = mView.findViewById(R.id.txtPostTitle);
            userName.setText(title);
        }

        public void setPrice(String price){
            TextView userPrice = mView.findViewById(R.id.txtPrice);
            userPrice.setText(price);

        }

        public void setPost_Image( String post_Image){
            ImageView PostImage = mView.findViewById(R.id.imgPost);
            Picasso.get().load(post_Image).error(R.drawable.post_img).placeholder(R.drawable.post_img).into(PostImage);
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayAllUserPosts();
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayAllUserPosts();
    }



}
