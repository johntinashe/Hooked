package com.github.johntinashe.hooked;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.register_btn) TextView registerBtn;
    @BindView(R.id.email) EditText emailEditText;
    @BindView(R.id.password_login) EditText passwordEditText;

    private AlertDialog dialog;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDB;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            goToMain();
        }

    }

    private void goToMain() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    public void signIn(View view) {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(email.equals("") || password.equals("")) {
            errorMessage("Please enter all fields !");
        } else  {
            startProgress("Logging in please wait...");
            if (mUser == null) {
                mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult.getUser() != null) {
                            if (authResult.getUser().isEmailVerified()) {
                                Map<String,Long> login = new HashMap<>();
                                login.put("last_login",System.currentTimeMillis());
                                mDB.collection("users").document(authResult.getUser().getUid())
                                        .set(login,SetOptions.merge());
                                finishProgress();
                                goToMain();
                            }else {
                                mAuth.signOut();
                                finishProgress();
                                errorMessage("Verify your email first");
                            }
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        finishProgress();
                        errorMessage(e.getMessage());
                    }
                });
            }else {
                goToMain();
            }
        }
    }

    private void startProgress(String title) {
        dialog = new SpotsDialog(this, R.style.Custom);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(title);
        dialog.show();
    }

    private void finishProgress() {
        dialog.dismiss();
    }

    public void errorMessage(String message) {

        TSnackbar snackbar = TSnackbar.make(findViewById(R.id.loginActivity), message, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
