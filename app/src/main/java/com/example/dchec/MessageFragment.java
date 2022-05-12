package com.example.dchec;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageFragment extends Fragment {
    private RecyclerView recycler;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private usersAdapter usersAdapter;
    usersAdapter.onUserClickListener onUserClickListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String myImageUrl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment , container , false);

        progressBar =(ProgressBar) view.findViewById(R.id.progressBar);
        users = new ArrayList<>();
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    onUserClickListener = new usersAdapter.onUserClickListener() {
        @Override
        public void onUserClicked(int position) {
            startActivity(new Intent(getActivity(),MessageFragment.class)
                    .putExtra("nom_of_roommate", users.get(position).getNom())
                    .putExtra("prenom_of_roommate", users.get(position).getPrenom())
                    .putExtra("email_of_roommate",users.get(position).getEmail())
                    .putExtra("image_of_roommate",users.get(position).getProfilePicture())
                    .putExtra("my_image",myImageUrl)

            );

        }
    };
    getUsers();
        return view;
}
    private void getUsers(){
        users.clear();
        FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    users.add(dataSnapshot.getValue(User.class));
                }
                usersAdapter = new usersAdapter(users,getContext(),onUserClickListener);
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                recycler.setAdapter(usersAdapter);
                progressBar.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);

                for(User user:users){
                    if(user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        myImageUrl = user.getProfilePicture();
                        return;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}




