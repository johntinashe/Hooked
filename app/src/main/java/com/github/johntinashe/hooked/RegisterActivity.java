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
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.johntinashe.hooked.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText usernameET;
    @BindView(R.id.age)
    EditText ageET;
    @BindView(R.id.email_sign_up)
    EditText emailET;
    @BindView(R.id.password)
    EditText passwordET;
    @BindView(R.id.sign_up_btn)
    Button registerBtn;

    private FirebaseFirestore mDB;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        if (mFirebaseUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

    }


    public void registerNewUser(View view) {

        if (usernameET.getText().toString().equals("") || ageET.getText().toString().equals("") ||
                emailET.getText().toString().equals("") || passwordET.getText().toString().equals("")
                ) {
            Toast.makeText(this, "All fields are required !", Toast.LENGTH_SHORT).show();
        } else {
            String name = usernameET.getText().toString();
            int age = 0;
            try {
                age = Integer.parseInt(ageET.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Enter valid age!", Toast.LENGTH_SHORT).show();
            }
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            User user = new User(name, age);

            saveUser(user, email, password);
        }

    }

    void saveUser(final User user, String email, String pass) {

        if (mFirebaseUser == null) {
            startProgress("Please wait ...");

            mDB = FirebaseFirestore.getInstance();
            mAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(final AuthResult authResult) {
                    mDB.collection("users").document(authResult.getUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            authResult.getUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finishProgress();
                                    mAuth.signOut();
                                    Toast.makeText(RegisterActivity.this, "Verification email sent !", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    emailET.setText("");
                                    passwordET.setText("");
                                    usernameET.setText("");
                                    ageET.setText("");
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    finishProgress();
                                    errorMessage(e.getMessage());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finishProgress();
                            errorMessage(e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finishProgress();
                    errorMessage(e.getMessage());
                }
            });

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

        TSnackbar snackbar = TSnackbar.make(findViewById(R.id.registerActivity), message, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

}
