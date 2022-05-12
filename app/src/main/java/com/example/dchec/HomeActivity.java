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
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private DatabaseReference userRef;
    private String currentUserId;
    private Button logoutBtn;

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private BottomNavigationView bottomNavigationView;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private static GoogleSignInAccount account;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavigationView = findViewById(R.id.bottom_nav);
        navigationView = findViewById(R.id.navigation_view);



        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.tool_bar);




        replaceFragment(new PostsFragment());


        setSupportActionBar(toolbar);

        View viewHeader = navigationView.inflateHeaderView(R.layout.navigation_header);

        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_menu);


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
            case R.id.nav_profile :
                replaceFragment(new AccountFragment());
                toolbar.setVisibility(View.GONE);
                Toast.makeText(this, "compte", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_messages:
                toolbar.setVisibility(View.GONE);
                replaceFragment(new MessageFragment());
                Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_info:
                toolbar.setVisibility(View.GONE);
                replaceFragment(new InfoFragment());
                Toast.makeText(this, "info", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_home:
                toolbar.setVisibility(View.VISIBLE);
                replaceFragment(new PostsFragment());
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_save:
                toolbar.setVisibility(View.GONE);
                replaceFragment(new SaveFragment());
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                LogOutMethod();
                break;


        }

        drawerLayout.closeDrawer(GravityCompat.START);

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


}