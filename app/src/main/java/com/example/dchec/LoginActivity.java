package com.example.dchec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private TextView forgetPassword , titleText , qstText , choiceTxt ,recupirerTxt , annulerTxt ;
    private Button accountBtn;
    private TextInputLayout inputConfirmPassword ,inputPassword;
    private Boolean hasAccoount;
    private EditText userEmail , userPassword , userConfirmPassword ;
    private TextView loginEmailText , loginPasswordText ,confirmPasswordText ;
    private ImageView shadowImage;
    private CardView passwordCard;

    private FirebaseAuth firebaseAuth , mAuth;

    boolean isProblem = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


         forgetPassword = findViewById(R.id.forget_mtp);
         titleText = findViewById(R.id.title_text);
         qstText = findViewById(R.id.qst_text);
         choiceTxt = findViewById(R.id.choice_txt);
         accountBtn = findViewById(R.id.account_btn);
         inputConfirmPassword = findViewById(R.id.confirm_password);
         inputPassword = findViewById(R.id.password_layout);
         userEmail = findViewById(R.id.user_email);
         userPassword = findViewById(R.id.user_password);
         userConfirmPassword = findViewById(R.id.user_confirm_password);
         loginEmailText = findViewById(R.id.login_email_text);
         loginPasswordText = findViewById(R.id.login_password_text);
         confirmPasswordText = findViewById(R.id.login_confirm_password_text);
         shadowImage = findViewById(R.id.shadow_page);
         passwordCard = findViewById(R.id.password_card);
         recupirerTxt = findViewById(R.id.recupirer);
         annulerTxt = findViewById(R.id.annuler);

         progressDialog = new ProgressDialog(this);

         firebaseAuth = FirebaseAuth.getInstance();


         hasAccoount = true;




            choiceTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!hasAccoount){

                    choiceTxt.setText("S'incrire");
                    titleText.setText("Se connecter");
                    qstText.setText("Vous n’avez pas de compte ?");
                    forgetPassword.setVisibility(View.VISIBLE);
                    inputConfirmPassword.setVisibility(View.GONE);
                    accountBtn.setText("Connexion");
                    confirmPasswordText.setVisibility(View.GONE);

                    hasAccoount = true;
                    }else{
                        choiceTxt.setText("Se connecter");
                        titleText.setText("S'incrire");
                        qstText.setText("Vous avez déja un compte ?");
                        forgetPassword.setVisibility(View.GONE);
                        inputConfirmPassword.setVisibility(View.VISIBLE);
                        accountBtn.setText("Inscription");


                        hasAccoount = false;
                    }
                }
            });


            accountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!hasAccoount){
                        creatNewAccount();
                    }else{
                        AllowUserToLogIn();
                    }
                }
            });

            forgetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shadowImage.setVisibility(View.VISIBLE);
                    passwordCard.setVisibility(View.VISIBLE);
                }
            });

            annulerTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shadowImage.setVisibility(View.GONE);
                    passwordCard.setVisibility(View.GONE);
                }
            });

            recupirerTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendToForgetPasswordActivity();
                }
            });



    }

    private void sendToForgetPasswordActivity() {
        Intent intent = new Intent(LoginActivity.this , ForgetPassword.class);
        startActivity(intent);
    }

    private void AllowUserToLogIn() {

        String loginEmail = userEmail.getText().toString();
        String loginPassword = userPassword.getText().toString();

        if (TextUtils.isEmpty(loginEmail)) {
            loginEmailText.setVisibility(View.VISIBLE);
        }else{
            loginEmailText.setVisibility(View.GONE);

        }

        if (TextUtils.isEmpty(loginPassword)) {
            loginPasswordText.setVisibility(View.VISIBLE);

        }else{
            loginPasswordText.setVisibility(View.GONE);

        }

        if (TextUtils.isEmpty(loginEmail) || TextUtils.isEmpty(loginPassword) ){
            isProblem = true ;
        }else{
            isProblem = false;
        }

        if(!isProblem){
            progressDialog.setTitle("Please wait ..");
            progressDialog.setMessage("loging in  on progress ...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            firebaseAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(LoginActivity.this, "you are loged in", Toast.LENGTH_SHORT).show();
                            sendUserToHomeActivity();
                            progressDialog.dismiss();

                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "svp , verifier votre email", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "eurreur : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });

        }
    }

    private void sendUserToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void creatNewAccount() {

        String  email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confirmPassword = userConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            loginEmailText.setVisibility(View.VISIBLE);
        }else{
            loginEmailText.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(password)) {
            loginPasswordText.setVisibility(View.VISIBLE);

        }else{
            loginPasswordText.setVisibility(View.GONE);

        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordText.setVisibility(View.VISIBLE);
        }else if( !password.equals(confirmPassword)){
            confirmPasswordText.setVisibility(View.VISIBLE);
            Toast.makeText(LoginActivity.this, "your  password doesn't match", Toast.LENGTH_SHORT).show();
        } else {
            confirmPasswordText.setVisibility(View.GONE);
        }


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)){
            isProblem = true ;
        }else{
            isProblem = false;
        }


        if (!isProblem){

            progressDialog.setTitle("Please wait ..");
            progressDialog.setMessage("Creating account on progress ...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "loged in successfully , please verify you email adresse ", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    sendUserToSetUpActivity();
                                }else{
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, "eurror : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }


    private void sendUserToSetUpActivity() {
        Intent setUpIntent = new Intent(LoginActivity.this , setUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setUpIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            sendUserToHomeActivity();
        }
    }



}