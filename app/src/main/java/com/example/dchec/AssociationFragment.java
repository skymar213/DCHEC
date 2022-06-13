package com.example.dchec;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AssociationFragment extends Fragment {

    private DatabaseReference postRef ;

    private RecyclerView postList;

    private FirebaseAuth mAuth;
    private String current_user_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.association_fragment, container ,false);

        postList = view.findViewById(R.id.association_post_recycler_view);




        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        postRef = FirebaseDatabase.getInstance().getReference().child("associationPosts");

        DisplayAllUserPosts();

        return view;
    }

    private void DisplayAllUserPosts() {

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postRef, Posts.class).build();

        FirebaseRecyclerAdapter<Posts , PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options)


        {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model) {

                holder.setUser_Name(model.getUserName());
                holder.setTitle(model.getTitle());
                holder.setDescription(model.getDescription());

                holder.mView.findViewById(R.id.btnContacterAsso).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(),MessageActivity.class)
                                .putExtra("nom_of_roommate",model.getUserName())
                                ;
                        startActivity(i);
                    }
                });


            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.association_post_holder,parent,false);
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
            TextView userName = mView.findViewById(R.id.association_name);
            userName.setText(user_Name);
        }

        public void setTitle(String title){
            TextView userName = mView.findViewById(R.id.association_title_txt);
            userName.setText(title);
        }

        public void setDescription(String description){
            TextView associationDescription = mView.findViewById(R.id.association_description);
            associationDescription.setText(description);
        }







    }

}
