package com.example.dchec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView utilisateurImage , associationImage;
    private CardView utilisateurCard , associationCard;
    private TextView utilisateurTxt , associationTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Dchec);
        setContentView(R.layout.activity_main);



        utilisateurImage = findViewById(R.id.utilisateur_select);
        associationImage = findViewById(R.id.association_select);
        utilisateurCard = findViewById(R.id.utilisateur_card_view);
        associationCard = findViewById(R.id.association_card_view);
        utilisateurTxt = findViewById(R.id.utilisateur_txt);
        associationTxt = findViewById(R.id.association_txt);


        utilisateurCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilisateurCard.setCardBackgroundColor(getResources().getColor(R.color.main_color));
                utilisateurTxt.setTextColor(getResources().getColor(R.color.white));
                utilisateurCard.setCardElevation(10);
                utilisateurCard.setElevation(10);
                utilisateurCard.animate().scaleX(1f);
                utilisateurCard.animate().scaleY(1f);


                associationCard.setCardBackgroundColor(getResources().getColor(R.color.white));
                associationTxt.setTextColor(getResources().getColor(R.color.main_color));
                associationCard.setCardElevation(0);
                associationCard.setElevation(0);
                associationCard.animate().scaleX(0.9f);
                associationCard.animate().scaleY(0.9f);


                Timer myTimer = new Timer();
                myTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        sendUserToLogInActivity();
                    }
                },250);
            }
        });


        associationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilisateurCard.setCardBackgroundColor(getResources().getColor(R.color.white));
                utilisateurTxt.setTextColor(getResources().getColor(R.color.main_color));
                utilisateurCard.setCardElevation(0);
                utilisateurCard.setElevation(0);
                utilisateurCard.animate().scaleX(0.9f);
                utilisateurCard.animate().scaleY(0.9f);


                associationCard.setCardBackgroundColor(getResources().getColor(R.color.main_color));
                associationTxt.setTextColor(getResources().getColor(R.color.white));
                associationCard.setCardElevation(10);
                associationCard.setElevation(10);
                associationCard.animate().scaleX(1f);
                associationCard.animate().scaleY(1f);
            }
        });


    }

    private void sendUserToLogInActivity() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}