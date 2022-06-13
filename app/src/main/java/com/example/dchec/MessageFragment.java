package com.example.dchec;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageFragment extends Fragment {
    private RecyclerView recycler;
    public  ArrayList<User> users = new ArrayList<>();
    private ArrayList<String> usersList = new ArrayList<>();
    private ProgressBar progressBar;
    private DatabaseReference currentUserRef,userMessageRef , userRef;
    String currentUserId;
    String txtUsername;

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

        currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userMessageRef = FirebaseDatabase.getInstance().getReference().child("userMessages");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

        currentUserRef.addValueEventListener(new ValueEventListener() {
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

                    .putExtra("nom_of_roommate1", users.get(position).getUserName())
                    .putExtra("prenom_of_roommate", users.get(position).getNickName())
                    .putExtra("email_of_roommate1",users.get(position).getUid())

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
                String othersUid;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot != null){
                        if (dataSnapshot.toString().contains(txtUsername)){

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                Message message = dataSnapshot1.getValue(Message.class);

                                    if (message.getSender()== FirebaseAuth.getInstance().getCurrentUser().getUid().toString()){
                                        othersUid = dataSnapshot1.getValue(Message.class).getReceiver();
                                        addUserToUserMessages(othersUid);
                                    }else if (message.getReceiver() == FirebaseAuth.getInstance().getCurrentUser().getUid().toString()){
                                        othersUid = dataSnapshot1.getValue(Message.class).getSender();
                                        addUserToUserMessages(othersUid);
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

        FirebaseDatabase.getInstance().getReference("userMessages").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (dataSnapshot.getValue(User.class).getUid() != FirebaseAuth.getInstance().getCurrentUser().getUid()){
                            users.add(dataSnapshot.getValue(User.class));
                        }
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

    private void addUserToUserMessages(String othersUid) {
        userRef.child(othersUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String myName = snapshot.child("userName").getValue().toString();
                    String myNickName = snapshot.child("nickName").getValue().toString();
                    String myPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                    String myLocalisation = snapshot.child("localisation").getValue().toString();
                    String myPassword = snapshot.child("password").getValue().toString();
                    String myUid = snapshot.child("Uid").getValue().toString();

                    HashMap hashMap= new HashMap();
                    hashMap.put("userName",myName);
                    hashMap.put("phoneNumber",myPhoneNumber);
                    hashMap.put("password",myPassword);
                    hashMap.put("nickName",myNickName);
                    hashMap.put("localisation",myLocalisation);
                    hashMap.put("Uid",myUid);

                    userMessageRef.child(currentUserId).child(othersUid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String myName = snapshot.child("userName").getValue().toString();
                    String myNickName = snapshot.child("nickName").getValue().toString();
                    String myPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                    String myLocalisation = snapshot.child("localisation").getValue().toString();
                    String myPassword = snapshot.child("password").getValue().toString();
                    String myUid = snapshot.child("Uid").getValue().toString();

                    HashMap hashMap= new HashMap();
                    hashMap.put("userName",myName);
                    hashMap.put("phoneNumber",myPhoneNumber);
                    hashMap.put("password",myPassword);
                    hashMap.put("nickName",myNickName);
                    hashMap.put("localisation",myLocalisation);
                    hashMap.put("Uid",myUid);

                    userMessageRef.child(othersUid).child(currentUserId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}





