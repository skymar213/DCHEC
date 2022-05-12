package com.example.dchec;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.auth.User;

import java.util.zip.Inflater;

public class PostsFragment extends Fragment {


    TextView utilisateurTxt , associationTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.posts_fragment , container , false);

        replaceFragment(new UserFragment());


        utilisateurTxt = view.findViewById(R.id.utilisateur_post_txt);
        associationTxt = view.findViewById(R.id.association_post_txt);

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

        return view;
    }


    private void replaceFragment(Fragment fragment){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.posts_container , fragment);
        transaction.commit();

    }



}
