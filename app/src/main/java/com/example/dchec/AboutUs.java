package com.example.dchec;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class AboutUs extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        toolbar = findViewById(R.id.tool_bar_about_us);

        setSupportActionBar(toolbar);



        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Qui somme nous ?");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}