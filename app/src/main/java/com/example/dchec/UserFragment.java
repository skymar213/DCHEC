package com.example.dchec;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class UserFragment extends Fragment {

    private DatabaseReference  postRef , natureRef , gratuitRef , payantRef , gratuitNourritureRef , gratuitVetementRef , gratuitChaussureRef , gratuitMaisonRef , gratuitAutreRef
            , payantNourritureRef , payantVetementRef , payantChaussureRef , payantMaisonRef ,payantAutreRef ;
    public static DatabaseReference userChosenRef;
    private FirebaseAuth mAuth;
    private String current_user_id;

    private RecyclerView postList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_fragment , container ,false);

        postList = view.findViewById(R.id.user_post_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity() , 2 , LinearLayoutManager.VERTICAL , false);
        postList.setLayoutManager(gridLayoutManager);


        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        postRef = FirebaseDatabase.getInstance().getReference().child("posts");

        if (!PostsFragment.isDismissed){
            userChosenRef = postRef;
        } else
        {
            userChosenRef = PostsFragment.userChosenDialogRef;
            PostsFragment.isDismissed = false;
        }


        DisplayAllUserPosts();
        
        return view;
    }

    private void DisplayAllUserPosts() {

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(userChosenRef, Posts.class).build();

        FirebaseRecyclerAdapter<Posts , PostViewHolder > firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options)


        {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model) {

                String postKey = getRef(position).getKey();

                holder.setUser_Name(model.getUserName());
                holder.setPost_Image(model.getPostImage());
                holder.setTitle(model.getTitle());
                holder.setPrice(model.getPrice());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent clickPost = new Intent(getActivity(),PostActivity.class);
                        clickPost.putExtra("postKey",postKey);
                        clickPost.putExtra("Uid",model.getUid());
                        startActivity(clickPost);
                    }
                });



            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.user_post_holder,parent,false);
                return new PostViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        postList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setUser_Name(String user_Name){
            TextView userName = mView.findViewById(R.id.user_post_user_name);
            userName.setText(user_Name);
        }

        public void setTitle(String title){
            TextView userName = mView.findViewById(R.id.user_post_title);
            userName.setText(title);
        }

        public void setPrice(String price){
            TextView userPrice = mView.findViewById(R.id.user_post_price);
                userPrice.setText(price);

        }

        public void setPost_Image( String post_Image){
            ImageView PostImage = mView.findViewById(R.id.user_post_image);
            Picasso.get().load(post_Image).error(R.drawable.image_error).placeholder(R.drawable.image_error).into(PostImage);
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
