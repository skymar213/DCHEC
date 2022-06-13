package com.example.dchec;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText edtMessageInput;
    private TextView txtChattingWith;
    private ProgressBar progressBar;
    private ImageView imgToolbar,imgSend;
    Button btnArrow;
    private Boolean isSeen = false;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messages;

    String usernameOfTheRoommate, emailOfRoommate, chatRoomId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        usernameOfTheRoommate = getIntent().getStringExtra("nom_of_roommate");
        emailOfRoommate = getIntent().getStringExtra("email_of_roommate2");

        recyclerView = findViewById(R.id.recyclerMessages);
        imgSend = findViewById(R.id.imgSendMessage);
        btnArrow = findViewById(R.id.img_arrow);
        edtMessageInput = findViewById(R.id.edtText);
        txtChattingWith = findViewById(R.id.txtChattingWith);
        progressBar= findViewById(R.id.progressMessages);
        imgToolbar= findViewById(R.id.img_toolbar);

        txtChattingWith.setText(usernameOfTheRoommate);

        messages = new ArrayList<>();

        btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
            imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mag = edtMessageInput.getText().toString();
                if (!mag.equals("")){
                FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getUid(),emailOfRoommate,edtMessageInput.getText().toString()));
                edtMessageInput.setText("");
                }
            }
        });

        messageAdapter = new MessageAdapter(messages,getIntent().getStringExtra("my_img"),getIntent().getStringExtra("img_of_roommate"),MessageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        Glide.with(MessageActivity.this).load(getIntent().getStringExtra("img_of_roommate")).placeholder(R.drawable.profile_img).error(R.drawable.profile_img).into(imgToolbar);

        setUpChatRoom();



    }



    private void setUpChatRoom(){

        FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = Objects.requireNonNull(snapshot.getValue(User.class)).getUserName();

                    chatRoomId = myUsername + usernameOfTheRoommate;

                attachMessageListener(chatRoomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void attachMessageListener(String chatRoomId){
        FirebaseDatabase.getInstance().getReference("messages/"+ chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
