package com.example.dchec;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SearchFragment extends Fragment {

    EditText edtSearch;
    RecyclerView recyclerU, recyclerA;
    ArrayList<User> users = new ArrayList<>();
    ArrayList<User> associations = new ArrayList<>();
    usersAdapter.onUserClickListener onUserClickListener;
    usersAdapter usersAdapter,associationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        edtSearch = view.findViewById(R.id.edtSearch);
        recyclerU = view.findViewById(R.id.recyclerU);
        recyclerA = view.findViewById(R.id.recyclerA);

        recyclerU.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerA.setLayoutManager(new LinearLayoutManager(getContext()));

        getUsers();
        getAssociations();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
                searchAssociations(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    private void searchAssociations(String toLowerCase) {
        Query query = FirebaseDatabase.getInstance().getReference("association").orderByChild("userName")
                .startAt(toLowerCase)
                .endAt(toLowerCase + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                associations.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        associations.add(dataSnapshot.getValue(User.class));
                    }
                }
                associationAdapter = new usersAdapter(associations,getContext());
                recyclerA.setAdapter(associationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAssociations() {
        associations.clear();
        FirebaseDatabase.getInstance().getReference("association").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (edtSearch.getText().toString().equals("")) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            associations.add(dataSnapshot.getValue(User.class));
                        }
                    }
                    associationAdapter = new usersAdapter(associations, getContext());
                    recyclerA.setAdapter(associationAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void searchUsers(String toString) {
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("userName")
                .startAt(toString)
                .endAt(toString + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(dataSnapshot.getValue(User.class));
                    }
                }
                usersAdapter = new usersAdapter(users,getContext());
                recyclerU.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUsers() {
            users.clear();
            FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (edtSearch.getText().toString().equals("")) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                users.add(dataSnapshot.getValue(User.class));
                            }
                        }
                        usersAdapter = new usersAdapter(users, getContext());
                        recyclerU.setAdapter(usersAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    
    }



