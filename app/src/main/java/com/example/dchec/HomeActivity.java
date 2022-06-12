package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private String uid="";

    private FirebaseAuth mAuth ;
    private DatabaseReference userRef , associationRef;
    private String currentUserId;
    private FloatingActionButton addPostBtn ;


    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private BottomNavigationView bottomNavigationView;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private static GoogleSignInAccount account;

    private static FrameLayout container;
    private static FrameLayout.LayoutParams lp;

    private TextView navUserName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        uid = getIntent().getStringExtra("uid");


        bottomNavigationView = findViewById(R.id.bottom_nav);
        navigationView = findViewById(R.id.navigation_view);





        addPostBtn = findViewById(R.id.add_post_btn);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.tool_bar);
        container = findViewById(R.id.container);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        associationRef = FirebaseDatabase.getInstance().getReference().child("association");


        View viewHeader = navigationView.inflateHeaderView(R.layout.navigation_header);
        navUserName = viewHeader.findViewById(R.id.nav_user_full_name);

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    if(snapshot.hasChild("userName")){
                        String userName = snapshot.child("userName").getValue().toString();
                        navUserName.setText(userName);
                    }else {
                        Toast.makeText(HomeActivity.this, "user name does not exist", Toast.LENGTH_SHORT).show();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



         lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);




        replaceFragment(new PostsFragment());


        setSupportActionBar(toolbar);



        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_menu);

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToAddPostActivity();

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                menuUserSelected(item);
                return false;
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomUserSelected(item);
                return true;
            }
        });



        //methods for getting account email and account username
        // account.getDisplayName   account.getEmail
        account = GoogleSignIn.getLastSignedInAccount(this);
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("343696964671-qgmjedg8tcvep8tkvlct111a0ppbo9cn.apps.googleusercontent.com")
                .requestEmail()
                .build();

         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null){
            sendToLogInActivity();
        } else {
            CheckUserExistence();
            currentUserId = mAuth.getCurrentUser().getUid();
        }




        //checking if user is signing in with google
      //  if (account != null);




    }

    private void sendUserToAddPostActivity() {
        Intent intent = new Intent(HomeActivity.this , AddPostActivity.class);
        startActivity(intent);
    }


    private void LogOutMethod() {
        if (account != null){
            mGoogleSignInClient.signOut();
        }else {
            mAuth.signOut();
        }
        sendToLogInActivity();
    }


    private void sendToLogInActivity() {
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void CheckUserExistence() {


        final String current_user_id = mAuth.getCurrentUser().getUid();

        if (MainActivity.isSimpleUser){

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!(snapshot.hasChild(current_user_id))){
                        sendUserToSetUpActivity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {

            associationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!(snapshot.hasChild(current_user_id))){
                        sendUserToSetUpActivity();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }

    private void sendUserToSetUpActivity() {
        Intent setUpIntent = new Intent(HomeActivity.this , setUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setUpIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null){
            sendToLogInActivity();
        } else {
            CheckUserExistence();
        }
    }

    private void replaceFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container , fragment);
        transaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {

            super.onBackPressed();
        }
    }

    private void menuUserSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_account:
                sendUserToAccountActivity();
                break;

            case R.id.nav_messages:
                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_messages);
                toolbar.setVisibility(View.GONE);
                replaceFragment(new MessageFragment());
                break;

            case R.id.nav_info:
                toolbar.setVisibility(View.GONE);
                replaceFragment(new InfoFragment());
                break;

            case R.id.nav_home:
                toolbar.setVisibility(View.VISIBLE);
                replaceFragment(new PostsFragment());
                break;

            case R.id.nav_save:
                toolbar.setVisibility(View.GONE);
                replaceFragment(new SaveFragment());
                break;

            case R.id.nav_logout:
                LogOutMethod();
                break;


        }

        drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void sendUserToAccountActivity() {
        Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    private void bottomUserSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.bottom_nav_profile :
                replaceFragment(new ProfileFragment());
                toolbar.setVisibility(View.GONE);
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_nav_messages:
                toolbar.setVisibility(View.GONE);
                replaceFragment(new MessageFragment());
                Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();
                break;


            case R.id.bottom_nav_home:
                toolbar.setVisibility(View.VISIBLE);
                replaceFragment(new PostsFragment());
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bottom_nav_search:
                toolbar.setVisibility(View.GONE);
                replaceFragment(new SearchFragment());
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;



        }

    }

    public String getUid() {
        return uid;
    }
}