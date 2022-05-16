package com.example.dchec;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PostsFragment extends Fragment {


    TextView utilisateurTxt , associationTxt;
    ImageButton categoryBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.posts_fragment , container , false);

        replaceFragment(new UserFragment());


        utilisateurTxt = view.findViewById(R.id.utilisateur_post_txt);
        associationTxt = view.findViewById(R.id.association_post_txt);
        categoryBtn = view.findViewById(R.id.category_icon);

        utilisateurTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilisateurTxt.setTextColor(getResources().getColor(R.color.main_color));
                utilisateurTxt.animate().scaleX(1.1f);
                utilisateurTxt.animate().scaleY(1.1f);

                associationTxt.setTextColor(getResources().getColor(R.color.hint_grey));
                associationTxt.animate().scaleX(1f);
                associationTxt.animate().scaleY(1f);

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
            }
        });

        payant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textSelected(payant);
                textUnselected(gratuit);
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
            }
        });

        chercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();



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
