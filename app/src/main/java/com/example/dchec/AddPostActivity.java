package com.example.dchec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;

public class AddPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView addPostImg;
    private EditText titleTxt , descriptionTxt;
    private RadioButton gratuitBtn , payantBtn , nourritureBtn , vetementBtn , chaussureBtn ,maisonBtn , autreBtn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        addPostImg = findViewById(R.id.add_post_img);
        titleTxt = findViewById(R.id.add_post_title);
        descriptionTxt = findViewById(R.id.add_post_description);

        gratuitBtn = findViewById(R.id.gratuit_radio_btn);
        payantBtn = findViewById(R.id.payant_radio_btn);
        nourritureBtn = findViewById(R.id.nourriture_radio_btn);
        vetementBtn = findViewById(R.id.vetement_radio_btn);
        chaussureBtn = findViewById(R.id.chaussure_radio_btn);
        maisonBtn = findViewById(R.id.maison_radio_btn);
        autreBtn = findViewById(R.id.autre_radio_btn);

        gratuitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gratuitBtn.setChecked(true);
                payantBtn.setChecked(false);
            }
        });

        payantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gratuitBtn.setChecked(false);
                payantBtn.setChecked(true);
            }
        });

        nourritureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nourritureBtn.setChecked(true);
                vetementBtn.setChecked(false);
                chaussureBtn.setChecked(false);
                maisonBtn.setChecked(false);
                autreBtn.setChecked(false);
            }
        });

        vetementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nourritureBtn.setChecked(false);
                vetementBtn.setChecked(true);
                chaussureBtn.setChecked(false);
                maisonBtn.setChecked(false);
                autreBtn.setChecked(false);
            }
        });

        chaussureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nourritureBtn.setChecked(false);
                vetementBtn.setChecked(false);
                chaussureBtn.setChecked(true);
                maisonBtn.setChecked(false);
                autreBtn.setChecked(false);
            }
        });

        maisonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nourritureBtn.setChecked(false);
                vetementBtn.setChecked(false);
                chaussureBtn.setChecked(false);
                maisonBtn.setChecked(true);
                autreBtn.setChecked(false);
            }
        });

        autreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nourritureBtn.setChecked(false);
                vetementBtn.setChecked(false);
                chaussureBtn.setChecked(false);
                maisonBtn.setChecked(false);
                autreBtn.setChecked(true);
            }
        });

        toolbar = findViewById(R.id.add_post_tool_bar);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.nav_black));
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}