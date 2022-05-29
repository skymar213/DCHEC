package com.example.dchec;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    TextView txtUsername, txtPhoneNum;
    Button btnContacter;
    RecyclerView recyclerPost;
    DatabaseReference userRef,postsRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment , container , false);

        recyclerPost = view.findViewById(R.id.recyclerPosts);
        txtPhoneNum = view.findViewById(R.id.txtPhoneNum);
        txtUsername = view.findViewById(R.id.txtUsername);
        btnContacter = view.findViewById(R.id.btnContacter);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
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







        return view ;
    }


    public void getPost(){

        List<Posts>data = new ArrayList<>();

        postsRef = FirebaseDatabase.getInstance().getReference().child("profilePosts").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String title = snapshot.child("title").getValue().toString();
                    String proPic = snapshot.child("postImage").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
