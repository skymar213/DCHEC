package com.example.dchec;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileFragment extends Fragment {
    TextView txtUsername;
    String postTitle, postImg,poster;
    RecyclerView recyclerPost;
    postsAdapter.onPostClickListener onPostClickListener;
    DatabaseReference userRef , associationRef;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        MainActivity.isSimpleUser = MainActivity.sharedPreferences.getBoolean("isSimpleUser" , true);

        recyclerPost = view.findViewById(R.id.recyclerPosts);
        recyclerPost.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        txtUsername = view.findViewById(R.id.txtUsernameProfile);

        txtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        associationRef = FirebaseDatabase.getInstance().getReference().child("association").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );






        if(MainActivity.isSimpleUser){
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("userName").getValue().toString();
                        txtUsername.setText(username);
                        DisplayAllUserPosts();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
        }
        associationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("userName").getValue().toString();
                    txtUsername.setText(username);
                    DisplayAllAssociationPosts();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        return view;
    }


    private void DisplayAllUserPosts() {

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(FirebaseDatabase.getInstance().getReference().child("profilePosts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()), Posts.class).build();

        FirebaseRecyclerAdapter<Posts, ProfileFragment.UserPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, UserPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProfileFragment.UserPostViewHolder holder, int position, @NonNull Posts model) {

                String postKey = getRef(position).getKey();

                holder.setUserName(model.getUserName());
                holder.setPost_Image(model.getPostImage());
                holder.setTitle(model.getTitle());
                holder.setPrice(model.getPrice());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent clickPost = new Intent(getActivity(),MyPostActivty.class);
                        clickPost.putExtra("postKey",postKey);
                        startActivity(clickPost);
                    }
                });



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
            TextView userName = mView.findViewById(R.id.txtUsernamePost);
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
            Picasso.get().load(post_Image).error(R.drawable.image_error).placeholder(R.drawable.image_error).into(PostImage);
        }



    }
    private void DisplayAllAssociationPosts() {

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(FirebaseDatabase.getInstance().getReference().child("associationProfilePosts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()), Posts.class).build();

        FirebaseRecyclerAdapter<Posts , AssociationFragment.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, AssociationFragment.PostViewHolder>(options)


        {
            @Override
            protected void onBindViewHolder(@NonNull AssociationFragment.PostViewHolder holder, int position, @NonNull Posts model) {

                holder.setUser_Name(model.getUserName());
                holder.setTitle(model.getTitle());
                holder.setDescription(model.getDescription());



            }

            @NonNull
            @Override
            public AssociationFragment.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.association_profile_post_holder,parent,false);
                return new AssociationFragment.PostViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerPost.setAdapter(firebaseRecyclerAdapter);
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
