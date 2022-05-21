package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView selectImage;
    private EditText titleTxt , descriptionTxt , priceTxt;
    private TextView natureTxt , categorieTxt ;
    private RadioButton gratuitBtn , payantBtn , nourritureBtn , vetementBtn , chaussureBtn ,maisonBtn , autreBtn ;
    private static final int Gallery_Pick =1;
    private Uri ImageUri;
    private StorageReference postImageReference;
    private DatabaseReference userRef , associationRef, userPostRef, associationPostRef ,  natureRef , categoryRef , gratuitRef , payantRef , gratuitNourritureRef , gratuitVetementRef , gratuitChaussureRef , gratuitMaisonRef , gratuitAutreRef
            , payantNourritureRef , payantVetementRef , payantChaussureRef , payantMaisonRef ,payantAutreRef , chosenOneRef;
    private FirebaseAuth mAuth;
    private Button updatePost;
    private String description , title , downloadUrl , current_user_id , price;
    private ProgressDialog progressDialog;
    private CardView addPostCardView ;
    private boolean isFree = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        addPostCardView = findViewById(R.id.cardView);

        postImageReference = FirebaseStorage.getInstance().getReference().child("Post_Image");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        associationRef = FirebaseDatabase.getInstance().getReference().child("association");
        userPostRef = FirebaseDatabase.getInstance().getReference().child("posts");
        associationPostRef = FirebaseDatabase.getInstance().getReference().child("associationPosts");

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

        chosenOneRef = gratuitAutreRef;

        selectImage = findViewById(R.id.add_post_img);
        titleTxt = findViewById(R.id.add_post_title);
        descriptionTxt = findViewById(R.id.add_post_description);
        priceTxt = findViewById(R.id.add_post_price);
        updatePost = findViewById(R.id.update_post);
        natureTxt = findViewById(R.id.nature_txt);
        categorieTxt = findViewById(R.id.categorie_txt);

        gratuitBtn = findViewById(R.id.gratuit_radio_btn);
        payantBtn = findViewById(R.id.payant_radio_btn);
        nourritureBtn = findViewById(R.id.nourriture_radio_btn);
        vetementBtn = findViewById(R.id.vetement_radio_btn);
        chaussureBtn = findViewById(R.id.chaussure_radio_btn);
        maisonBtn = findViewById(R.id.maison_radio_btn);
        autreBtn = findViewById(R.id.autre_radio_btn);


        if (MainActivity.isSimpleUser){

            addPostCardView.setVisibility(View.VISIBLE);

            natureTxt.setVisibility(View.VISIBLE);
            categorieTxt.setVisibility(View.VISIBLE);

            gratuitBtn.setVisibility(View.VISIBLE);
            payantBtn.setVisibility(View.VISIBLE);
            nourritureBtn.setVisibility(View.VISIBLE);
            vetementBtn.setVisibility(View.VISIBLE);
            chaussureBtn.setVisibility(View.VISIBLE);
            maisonBtn.setVisibility(View.VISIBLE);
            autreBtn.setVisibility(View.VISIBLE);
        } else {
            addPostCardView.setVisibility(View.GONE);

            natureTxt.setVisibility(View.GONE);
            categorieTxt.setVisibility(View.GONE);
            priceTxt.setVisibility(View.GONE);

            gratuitBtn.setVisibility(View.GONE);
            payantBtn.setVisibility(View.GONE);
            nourritureBtn.setVisibility(View.GONE);
            vetementBtn.setVisibility(View.GONE);
            chaussureBtn.setVisibility(View.GONE);
            maisonBtn.setVisibility(View.GONE);
            autreBtn.setVisibility(View.GONE);
        }

        gratuitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceTxt.setVisibility(View.GONE);
                gratuitBtn.setChecked(true);
                payantBtn.setChecked(false);
            }
        });

        payantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceTxt.setVisibility(View.VISIBLE);
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




        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OppenGallery();
            }
        });

        updatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChosenOneVerification();
                ValidatePostInfo();
            }
        });

    }

    private void ChosenOneVerification() {
        if (gratuitBtn.isChecked()){
            isFree = true;
            if (nourritureBtn.isChecked()){
                chosenOneRef = gratuitNourritureRef;
            }else if (chaussureBtn.isChecked()){
                chosenOneRef = gratuitChaussureRef;
            }else if (vetementBtn.isChecked()){
                chosenOneRef = gratuitVetementRef;
            }else if (maisonBtn.isChecked()){
                chosenOneRef = gratuitMaisonRef;
            } else if (autreBtn.isChecked()) {
                chosenOneRef = gratuitAutreRef;
            }
        } else if (payantBtn.isChecked()) {
            isFree = false;
            if (nourritureBtn.isChecked()){
                chosenOneRef = payantNourritureRef;
            }else if (chaussureBtn.isChecked()){
                chosenOneRef = payantChaussureRef;
            }else if (vetementBtn.isChecked()){
                chosenOneRef = payantVetementRef;
            }else if (maisonBtn.isChecked()){
                chosenOneRef = payantMaisonRef;
            } else if (autreBtn.isChecked()) {
                chosenOneRef = payantAutreRef;
            }
        }
    }


    private void OppenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , Gallery_Pick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            selectImage.setImageURI(ImageUri);
        }
    }

    private void ValidatePostInfo() {
        description = descriptionTxt.getText().toString();
        title = titleTxt.getText().toString();
        price = priceTxt.getText().toString();
        if (selectImage == null){
            Toast.makeText(AddPostActivity.this, "Please select your image", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(description) | TextUtils.isEmpty(title)){
            Toast.makeText(AddPostActivity.this, "Please add some description or title", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setTitle("Please wait ..");
            progressDialog.setMessage("Updating the Post is on progress ...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            if (MainActivity.isSimpleUser){
                StoringImageToFireBaseStorage();
            }else {
                SavingPostInformationToDataBase();
            }
        }
    }

    private void StoringImageToFireBaseStorage() {

        StorageReference filPath = postImageReference.child(ImageUri.getLastPathSegment() + ".jpg");
        filPath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){

                    Toast.makeText(AddPostActivity.this, "Post Image has been stored", Toast.LENGTH_SHORT).show();

                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                downloadUrl = task.getResult().toString();
                                Toast.makeText(AddPostActivity.this, "Good Job !", Toast.LENGTH_SHORT).show();
                                SavingPostInformationToDataBase();

                            }else{
                                Toast.makeText(AddPostActivity.this, "Problem Occured : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(AddPostActivity.this, "eurror occured  : " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void SavingPostInformationToDataBase() {

        if (MainActivity.isSimpleUser){

            userRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String userFullName = snapshot.child("userName").getValue().toString();

                        HashMap postMap = new HashMap();
                        postMap.put("uid" , current_user_id);
                        postMap.put("userName" , userFullName);
                        postMap.put("description" , description);
                        postMap.put("title" , title);
                        postMap.put("postImage" , downloadUrl);
                        postMap.put("price" , price);

                        userPostRef.child(current_user_id + " " + title ).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){

                                    progressDialog.dismiss();
                                    Toast.makeText(AddPostActivity.this, "post updated", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(AddPostActivity.this, "Problme occured : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        });

                        if (isFree){

                            gratuitRef.child(current_user_id + " " + title + "catg" ).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){

                                        progressDialog.dismiss();
                                        Toast.makeText(AddPostActivity.this, "post updated", Toast.LENGTH_SHORT).show();
                                        sendUserToHomeActivity();

                                    }else{
                                        Toast.makeText(AddPostActivity.this, "Problme occured : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

                            chosenOneRef.child(current_user_id + " " + title + "catg" ).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){

                                        progressDialog.dismiss();
                                        Toast.makeText(AddPostActivity.this, "post updated", Toast.LENGTH_SHORT).show();
                                        sendUserToHomeActivity();

                                    }else{
                                        Toast.makeText(AddPostActivity.this, "Problme occured : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

                        }else{

                            payantRef.child(current_user_id + " " + title + "catg" ).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){

                                        progressDialog.dismiss();
                                        Toast.makeText(AddPostActivity.this, "post updated", Toast.LENGTH_SHORT).show();
                                        sendUserToHomeActivity();

                                    }else{
                                        Toast.makeText(AddPostActivity.this, "Problme occured : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

                            chosenOneRef.child(current_user_id + " " + title + "catg" ).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){

                                        progressDialog.dismiss();
                                        Toast.makeText(AddPostActivity.this, "post updated", Toast.LENGTH_SHORT).show();
                                        sendUserToHomeActivity();

                                    }else{
                                        Toast.makeText(AddPostActivity.this, "Problme occured : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            associationRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if ( snapshot.exists()){

                        String associationName = snapshot.child("userName").getValue().toString();

                        HashMap postMap = new HashMap();
                        postMap.put("uid" , current_user_id);
                        postMap.put("userName" , associationName);
                        postMap.put("description" , description);
                        postMap.put("title" , title);



                        associationPostRef.child(current_user_id +" " + title).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    sendUserToHomeActivity();
                                    progressDialog.dismiss();
                                    Toast.makeText(AddPostActivity.this, "post updated", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(AddPostActivity.this, "Problme occured : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
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

    private void sendUserToHomeActivity() {
        Intent intent = new Intent(AddPostActivity.this , HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}