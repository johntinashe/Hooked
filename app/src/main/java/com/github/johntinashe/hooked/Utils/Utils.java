package com.github.johntinashe.hooked.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;

import com.github.johntinashe.hooked.LoginActivity;
import com.github.johntinashe.hooked.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Utils {

    public static  void setToolbar (Context context, ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayShowCustomEnabled(true);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View view = inflater != null ? inflater.inflate(R.layout.main_toolbar, null,false) : null;
            actionBar.setCustomView(view);
        }
    }


    public static void checkAuth(Activity activity) {
        FirebaseAuth mAuth;
        FirebaseUser mUser;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            Intent intent = new Intent(activity.getApplicationContext(),LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.finish();
            activity.startActivity(intent);
        }

    }



}
