package com.example.dchec;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private RecyclerView recycler,recyclerAsso;
    public  ArrayList<User> users = new ArrayList<>();
    public  ArrayList<Association> associations = new ArrayList<>();
    private ArrayList<String> usersList = new ArrayList<>();
    private ProgressBar progressBar;
    private DatabaseReference currentUserRef,userMessageRef , userRef , associationRef;
    String currentUserId;
    String txtUsername;
    TextView btnUtili,btnAsso;

    boolean utilisateurClicked = true , associationClicked = false;

    private usersMessageAdapter usersMessageAdapter;
    private AssociationMessageAdapter associationMessageAdapter;
    usersMessageAdapter.onUserClickListener onUserClickListener;
    AssociationMessageAdapter.onAssociationClickListener onAssociationClickListener;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment , container , false);



        progressBar =(ProgressBar) view.findViewById(R.id.progressBar);
        btnAsso = view.findViewById(R.id.btnAsso);
        btnUtili = view.findViewById(R.id.btnUtilisateur);



        MainActivity.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        MainActivity.isSimpleUser = MainActivity.sharedPreferences.getBoolean("isSimpleUser",true);


        users = new ArrayList<>();
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
         recyclerAsso = (RecyclerView) view.findViewById(R.id.recyclerAsso);

         if (MainActivity.isSimpleUser){

             currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                     FirebaseAuth.getInstance().getCurrentUser().getUid()
             );
         }else {
             currentUserRef = FirebaseDatabase.getInstance().getReference().child("association").child(
                     FirebaseAuth.getInstance().getCurrentUser().getUid());
         }

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userMessageRef = FirebaseDatabase.getInstance().getReference().child("userMessagesRef");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        associationRef = FirebaseDatabase.getInstance().getReference().child("association");

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







        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                getAssociations();
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


    onAssociationClickListener = new AssociationMessageAdapter.onAssociationClickListener() {
        @Override
        public void onAssociationClicked(int position) {
            Intent i = new Intent(getActivity(), MessageActivity.class)

                    .putExtra("nom_of_roommate1", associations.get(position).getUserName())
                    .putExtra("email_of_roommate1",associations.get(position).getUid())

                    ;
            startActivity(i);
        }
    };

        if (utilisateurClicked){
            getUsers();
        }else if (associationClicked){
            getAssociations();
        }

    btnAsso.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getAssociations();

            associationClicked = true;
            utilisateurClicked = false;
            recyclerAsso.setVisibility(View.VISIBLE);

            recycler.setVisibility(View.GONE);

            btnAsso.setTextColor(getResources().getColor(R.color.main_color));
            btnUtili.setTextColor(getResources().getColor(R.color.hint_grey));
        }
    });
      btnUtili.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              getUsers();
              utilisateurClicked=true;
              associationClicked=false;
              recycler.setVisibility(View.VISIBLE);

              recyclerAsso.setVisibility(View.GONE);

              btnAsso.setTextColor(getResources().getColor(R.color.hint_grey));

              btnUtili.setTextColor(getResources().getColor(R.color.main_color));


          }
      });






        return view;
}

    private void getAssociations() {

        FirebaseDatabase.getInstance().getReference("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String othersUid="";
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

        userMessageRef.child(currentUserId).child("associationMessages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                associations.clear();


                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.getValue(Association.class).getId()!=null){
                        if (dataSnapshot.getValue(Association.class).getUid() != FirebaseAuth.getInstance().getCurrentUser().getUid()){
                            associations.add(dataSnapshot.getValue(Association.class));
                        }
                    }
                }


                associationMessageAdapter = new AssociationMessageAdapter(associations,getContext(),onAssociationClickListener);
                recyclerAsso.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerAsso.setAdapter(associationMessageAdapter);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void getUsers(){

        FirebaseDatabase.getInstance().getReference("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                String othersUid="";
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

        userMessageRef.child(currentUserId).child("userMessages").addValueEventListener(new ValueEventListener() {
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

                        userMessageRef.child(currentUserId).child("userMessages").child(othersUid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                            }
                        });


                        if (currentUserId!=null){
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

                                        userMessageRef.child(othersUid).child("userMessages").child(currentUserId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {

                                            }
                                        });


                                    }else {
                                        associationRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String myName = snapshot.child("userName").getValue().toString();
                                                String id = snapshot.child("id").getValue().toString();
                                                String myPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                                                String myLocalisation = snapshot.child("localisation").getValue().toString();
                                                String myPassword = snapshot.child("password").getValue().toString();
                                                String myUid = snapshot.child("Uid").getValue().toString();

                                                HashMap hashMap= new HashMap();
                                                hashMap.put("userName",myName);
                                                hashMap.put("phoneNumber",myPhoneNumber);
                                                hashMap.put("password",myPassword);
                                                hashMap.put("id",id);
                                                hashMap.put("localisation",myLocalisation);
                                                hashMap.put("Uid",myUid);

                                                userMessageRef.child(othersUid).child("associationMessages").child(currentUserId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }else {
                        associationRef.child(othersUid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    String myName = snapshot.child("userName").getValue().toString();
                                    String id = snapshot.child("id").getValue().toString();
                                    String myPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                                    String myLocalisation = snapshot.child("localisation").getValue().toString();
                                    String myPassword = snapshot.child("password").getValue().toString();
                                    String myUid = snapshot.child("Uid").getValue().toString();

                                    HashMap hashMap= new HashMap();
                                    hashMap.put("userName",myName);
                                    hashMap.put("phoneNumber",myPhoneNumber);
                                    hashMap.put("password",myPassword);
                                    hashMap.put("id",id);
                                    hashMap.put("localisation",myLocalisation);
                                    hashMap.put("Uid",myUid);

                                    userMessageRef.child(currentUserId).child("associationMessages").child(othersUid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {

                                        }
                                    });

                                    if (currentUserId!=null){
                                        associationRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    String myName = snapshot.child("userName").getValue().toString();
                                                    String id = snapshot.child("id").getValue().toString();
                                                    String myPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                                                    String myLocalisation = snapshot.child("localisation").getValue().toString();
                                                    String myPassword = snapshot.child("password").getValue().toString();
                                                    String myUid = snapshot.child("Uid").getValue().toString();

                                                    HashMap hashMap= new HashMap();
                                                    hashMap.put("userName",myName);
                                                    hashMap.put("phoneNumber",myPhoneNumber);
                                                    hashMap.put("password",myPassword);
                                                    hashMap.put("id",id);
                                                    hashMap.put("localisation",myLocalisation);
                                                    hashMap.put("Uid",myUid);

                                                    userMessageRef.child(othersUid).child("associationMessages").child(currentUserId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {

                                                        }
                                                    });


                                                }else {
                                                    userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
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

                                                            userMessageRef.child(othersUid).child("userMessages").child(currentUserId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                                @Override
                                                                public void onComplete(@NonNull Task task) {

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

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

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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





