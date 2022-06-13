package com.example.dchec;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostsFragment extends Fragment {


    TextView utilisateurTxt , associationTxt;
    ImageButton categoryBtn;
    postsAdapter.onPostClickListener onPostClickListener;
    Boolean gratuitSelected , nourritureSelected  , chaussureSelected  , vetementSelected  , maisonSelected  , autreSelected   , categorieAllSelected ;

    private DatabaseReference postRef , natureRef ,categoryRef, gratuitRef , payantRef , gratuitNourritureRef , gratuitVetementRef , gratuitChaussureRef , gratuitMaisonRef , gratuitAutreRef
            , payantNourritureRef , payantVetementRef , payantChaussureRef , payantMaisonRef ,payantAutreRef ;

    public static DatabaseReference userChosenDialogRef;

    public static boolean isDismissed = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.posts_fragment , container , false);

        MainActivity.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        MainActivity.isSimpleUser = MainActivity.sharedPreferences.getBoolean("isSimpleUser" , true);

        postRef = FirebaseDatabase.getInstance().getReference().child("posts");



        natureRef = FirebaseDatabase.getInstance().getReference().child("nature");
        categoryRef = FirebaseDatabase.getInstance().getReference().child("categorie");

        gratuitRef = natureRef.child("gratuit");
        payantRef = natureRef.child("payant");

        gratuitNourritureRef = categoryRef.child("gratuit nourriture");
        gratuitVetementRef = categoryRef.child("gratuit vetement");
        gratuitChaussureRef = categoryRef.child("gratuit chaussure");
        gratuitMaisonRef = categoryRef.child("gratuit maison");
        gratuitAutreRef = categoryRef.child("gratuit autre");

        payantNourritureRef = categoryRef.child("payant nourriture");
        payantVetementRef = categoryRef.child("payant vetement");
        payantChaussureRef = categoryRef.child("payant chaussure");
        payantMaisonRef = categoryRef.child("payant maison");
        payantAutreRef = categoryRef.child("payant autre");


        utilisateurTxt = view.findViewById(R.id.utilisateur_post_txt);
        associationTxt = view.findViewById(R.id.association_post_txt);
        categoryBtn = view.findViewById(R.id.category_icon);

        if(MainActivity.isSimpleUser){
            replaceFragment(new UserFragment());
            utilisateurTxt.setTextColor(getResources().getColor(R.color.main_color));
            associationTxt.setTextColor(getResources().getColor(R.color.hint_grey));
            categoryBtn.setVisibility(View.VISIBLE);
        }else {
            replaceFragment(new AssociationFragment());
            utilisateurTxt.setTextColor(getResources().getColor(R.color.hint_grey));
            associationTxt.setTextColor(getResources().getColor(R.color.main_color));
            categoryBtn.setVisibility(View.GONE);
        }

        utilisateurTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilisateurTxt.setTextColor(getResources().getColor(R.color.main_color));
                utilisateurTxt.animate().scaleX(1.1f);
                utilisateurTxt.animate().scaleY(1.1f);

                associationTxt.setTextColor(getResources().getColor(R.color.hint_grey));
                associationTxt.animate().scaleX(1f);
                associationTxt.animate().scaleY(1f);

                categoryBtn.setVisibility(View.VISIBLE);


                replaceFragment(new UserFragment());
            }
        });

        associationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilisateurTxt.setTextColor(getResources().getColor(R.color.hint_grey));
                utilisateurTxt.animate().scaleX(1f);
                utilisateurTxt.animate().scaleY(1f);

                associationTxt.setTextColor(getResources().getColor(R.color.main_color));
                associationTxt.animate().scaleX(1.1f);
                associationTxt.animate().scaleY(1.1f);

                categoryBtn.setVisibility(View.GONE);

                replaceFragment(new AssociationFragment());
            }
        });


        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });

        return view;
    }

    private void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity() , R.style.DialogSheetBottomTheme);
        dialog.setContentView(R.layout.sheet_dialog_layout);

        gratuitSelected =true ;
        nourritureSelected = false ;
        chaussureSelected = false ;
        vetementSelected = false ;
        maisonSelected = false ;
        autreSelected = false ;
        categorieAllSelected = true;

        TextView categoryAll = dialog.findViewById(R.id.categorie_all_txt);
        TextView gratuit = dialog.findViewById(R.id.gratuit_txt);
        TextView payant = dialog.findViewById(R.id.payant_txt);
        TextView nourriture = dialog.findViewById(R.id.nourriture_txt);
        TextView vetement = dialog.findViewById(R.id.vetement_txt);
        TextView chaussure = dialog.findViewById(R.id.chaussure_txt);
        TextView maisaon = dialog.findViewById(R.id.maison_txt);
        TextView autre = dialog.findViewById(R.id.autre_txt);
        Button chercher = dialog.findViewById(R.id.btn_chercher);




        gratuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSelected(gratuit);
                textUnselected(payant);

                gratuitSelected = true;

            }
        });

        payant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSelected(payant);
                textUnselected(gratuit);

                gratuitSelected = false;

            }
        });

        nourriture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSelected(nourriture);
                textUnselected(vetement);
                textUnselected(chaussure);
                textUnselected(maisaon);
                textUnselected(autre);
                textUnselected(categoryAll);

                categorieAllSelected = false;
                nourritureSelected = true;
                vetementSelected = false;
                chaussureSelected = false;
                maisonSelected = false;
                autreSelected = false;
            }
        });

        vetement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textSelected(vetement);
                textUnselected(nourriture);
                textUnselected(chaussure);
                textUnselected(maisaon);
                textUnselected(autre);
                textUnselected(categoryAll);

                categorieAllSelected = false;
                nourritureSelected = false;
                vetementSelected = true;
                chaussureSelected = false;
                maisonSelected = false;
                autreSelected = false;
            }
        });

        chaussure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textSelected(chaussure);
                textUnselected(nourriture);
                textUnselected(vetement);
                textUnselected(maisaon);
                textUnselected(autre);
                textUnselected(categoryAll);

                categorieAllSelected = false;
                nourritureSelected = false;
                vetementSelected = false;
                chaussureSelected = true;
                maisonSelected = false;
                autreSelected = false;
            }
        });


        maisaon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textSelected(maisaon);
                textUnselected(nourriture);
                textUnselected(vetement);
                textUnselected(chaussure);
                textUnselected(autre);
                textUnselected(categoryAll);

                categorieAllSelected = false;
                nourritureSelected = false;
                vetementSelected = false;
                chaussureSelected = false;
                maisonSelected = true;
                autreSelected = false;
            }
        });

        autre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textSelected(autre);
                textUnselected(nourriture);
                textUnselected(vetement);
                textUnselected(maisaon);
                textUnselected(chaussure);
                textUnselected(categoryAll);

                categorieAllSelected = false;
                nourritureSelected = false;
                vetementSelected = false;
                chaussureSelected = false;
                maisonSelected = false;
                autreSelected = true;
            }
        });

        categoryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textSelected(categoryAll);
                textUnselected(nourriture);
                textUnselected(autre);
                textUnselected(vetement);
                textUnselected(maisaon);
                textUnselected(chaussure);

                categorieAllSelected = true;
                nourritureSelected = false;
                vetementSelected = false;
                chaussureSelected = false;
                maisonSelected = false;
                autreSelected = false;
            }
        });

        chercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkingUserChosenRef();
                replaceFragment(new UserFragment());
                dialog.dismiss();
                isDismissed =true;
            }
        });

        dialog.show();




    }

    private void checkingUserChosenRef() {
        if (gratuitSelected){
            if (nourritureSelected){
                    userChosenDialogRef = gratuitNourritureRef;

            }else if (chaussureSelected){
                    userChosenDialogRef = gratuitChaussureRef;

            }else if (vetementSelected){

                    userChosenDialogRef = gratuitVetementRef;

            }else if (maisonSelected){
                    userChosenDialogRef = gratuitMaisonRef;
            } else if (autreSelected) {
                    userChosenDialogRef = gratuitAutreRef;
            } else if (categorieAllSelected){
                    userChosenDialogRef = gratuitRef;
            }
        } else  {
            if (nourritureSelected){
                    userChosenDialogRef = payantNourritureRef;
            }else if (chaussureSelected){
                    userChosenDialogRef = payantChaussureRef;
            }else if (vetementSelected){
                    userChosenDialogRef = payantVetementRef;
            }else if (maisonSelected){
                    userChosenDialogRef = payantMaisonRef;
            } else if (autreSelected) {
                    userChosenDialogRef = payantAutreRef;
            }else if (categorieAllSelected){
                    userChosenDialogRef = payantRef;
            }
        }
    }

    private void textUnselected(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setBackground(getResources().getDrawable(R.drawable.category_unselected));
    }

    private void textSelected(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setBackground(getResources().getDrawable(R.drawable.category_selected));
    }




    private void replaceFragment(Fragment fragment){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.posts_container , fragment);
        transaction.commit();

    }



}
