package com.example.dchec;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageFragment extends Fragment {
    private RecyclerView recycler;
    public  ArrayList<User> users = new ArrayList<>();
    private ArrayList<String> usersList = new ArrayList<>();
    private ProgressBar progressBar;
    private DatabaseReference userRef,userRef2;
    String txtUsername,email;

    String uid;
    private usersMessageAdapter usersMessageAdapter;
    usersMessageAdapter.onUserClickListener onUserClickListener;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment , container , false);



        progressBar =(ProgressBar) view.findViewById(R.id.progressBar);
        users = new ArrayList<>();
        recycler = (RecyclerView) view.findViewById(R.id.recycler);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("userName").getValue().toString();

                    txtUsername = username;



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        HomeActivity homeActivity = (HomeActivity) getActivity();

       uid = homeActivity.getUid();






        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
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
            Intent i = new Intent(getActivity(), MessageActivity.class)

                    .putExtra("nom_of_roommate", users.get(position).getUserName())
                    .putExtra("prenom_of_roommate", users.get(position).getNickName())
                    .putExtra("email_of_roommate",users.get(position).getEmail())

            ;
            startActivity(i);



        }
    };
    getUsers();

        return view;
}
    private void getUsers(){

        FirebaseDatabase.getInstance().getReference("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot != null){
                        if (dataSnapshot.toString().contains(txtUsername)){

                            Message message = dataSnapshot.getValue(Message.class);
                            if (message.getSender() != null){
                                if (message.getSender().toString() == FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()){
                                    usersList.add(dataSnapshot.getValue(Message.class).getReceiver());
                                }
                            }


                        }
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            users.add(dataSnapshot.getValue(User.class));
                        }

                usersMessageAdapter = new usersMessageAdapter(users,getContext(),onUserClickListener);
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                recycler.setAdapter(usersMessageAdapter);
                progressBar.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




            }


}





