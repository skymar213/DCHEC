package com.example.dchec;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    EditText edtSearch;
    Button btnUsers,btnAssociations;
    RecyclerView recyclerU, recyclerA;
    TextView txtUsers,txtAssociations;
    ConstraintLayout ccSearch;
    ArrayList<User> users = new ArrayList<>();
    ArrayList<User> associations = new ArrayList<>();
    usersSearchAdapter.onUserClickListener onUserSearchClickListener;
    usersSearchAdapter usersMessageAdapter,associationAdapter;
    static boolean fromSearch=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        edtSearch = view.findViewById(R.id.edtSearch);
        ccSearch = view.findViewById(R.id.ccSearch);
        recyclerU = view.findViewById(R.id.recyclerU);
        txtUsers = view.findViewById(R.id.txtUsers);
        txtAssociations = view.findViewById(R.id.txtAsso);
        recyclerA = view.findViewById(R.id.recyclerA);
        btnUsers = view.findViewById(R.id.btnUsers);
        btnAssociations = view.findViewById(R.id.btnAssociations);



        btnAssociations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerU.setVisibility(View.GONE);
                txtUsers.setVisibility(View.GONE);

                txtAssociations.setVisibility(View.VISIBLE);
                recyclerA.setVisibility(View.VISIBLE);

                btnAssociations.setBackgroundResource(R.drawable.selected_cat);
                btnAssociations.setTextColor(getResources().getColor(R.color.white));
                btnUsers.setBackgroundResource(R.drawable.search_back);
                btnUsers.setTextColor(getResources().getColor(R.color.black));


            }
        });
        btnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerU.setVisibility(View.VISIBLE);
                txtUsers.setVisibility(View.VISIBLE);

                txtAssociations.setVisibility(View.GONE);
                recyclerA.setVisibility(View.GONE);

                btnAssociations.setBackgroundResource(R.drawable.search_back);
                btnAssociations.setTextColor(getResources().getColor(R.color.black));

                btnUsers.setBackgroundResource(R.drawable.selected_cat);
                btnUsers.setTextColor(getResources().getColor(R.color.white));


            }
        });


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
                searchAssociations(charSequence.toString().toLowerCase());
                searchUsers(charSequence.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        onUserSearchClickListener = new usersSearchAdapter.onUserClickListener() {
            @Override
            public void onUserClicked(int position) {
                if (btnUsers.getCurrentTextColor() == getResources().getColor(R.color.white)){

                    Intent i = new Intent(getActivity(), HisProfile.class)
                            .putExtra("nom_of_roommate", users.get(position).getUserName())
                            .putExtra("uid", users.get(position).getUid())
                            .putExtra("prenom_of_roommate", users.get(position).getNickName())
                            .putExtra("email_of_roommate", users.get(position).getUid());
                    fromSearch = true;
                    startActivity(i);
                }else{

                    Intent i = new Intent(getActivity(), HisAssoProfile.class)
                            .putExtra("nom_of_roommate", associations.get(position).getUserName())
                            .putExtra("uid", associations.get(position).getUid())
                            .putExtra("prenom_of_roommate", associations.get(position).getNickName())
                            .putExtra("email_of_roommate", associations.get(position).getUid());
                    fromSearch = true;

                    startActivity(i);
                }




            }
        };




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
                associationAdapter = new usersSearchAdapter(associations,getContext(),onUserSearchClickListener);
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
                    associationAdapter = new usersSearchAdapter(associations, getContext(),onUserSearchClickListener);
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
                usersMessageAdapter = new usersSearchAdapter(users,getContext(),onUserSearchClickListener);
                recyclerU.setAdapter(usersMessageAdapter);
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
                        usersMessageAdapter = new usersSearchAdapter(users, getContext(),onUserSearchClickListener);
                        recyclerU.setAdapter(usersMessageAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    
    }



