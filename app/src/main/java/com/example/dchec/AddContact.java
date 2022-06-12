package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddContact extends AppCompatActivity {
    private RecyclerView recycler;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<String> usersList = new ArrayList<>();
    private ProgressBar progressBar;
    private usersMessageAdapter usersMessageAdapter;
    usersMessageAdapter.onUserClickListener onUserClickListener;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        progressBar =(ProgressBar) findViewById(R.id.progressBar2);
        users = new ArrayList<>();
        recycler = (RecyclerView) findViewById(R.id.recycler2);



        swipeRefreshLayout = findViewById(R.id.swipeLayout2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        onUserClickListener = new usersMessageAdapter.onUserClickListener() {
            @Override
            public void onUserClicked(int position) {

                usersList.add(users.get(position).getUid());

               Intent intent =new Intent(AddContact.this,HomeActivity.class);
                startActivity(intent);



            }
        };
        getUsers();
    }

    private void getUsers(){

        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    users.add(dataSnapshot.getValue(User.class));

                }


                usersMessageAdapter = new usersMessageAdapter(users,AddContact.this,onUserClickListener);
                recycler.setLayoutManager(new LinearLayoutManager(AddContact.this));
                recycler.setAdapter(usersMessageAdapter);
                progressBar.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public ArrayList<String> getUsersList() {
        return usersList;
    }
}