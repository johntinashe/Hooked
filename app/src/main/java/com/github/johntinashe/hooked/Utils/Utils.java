package com.github.johntinashe.hooked.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johntinashe.hooked.BrowseActivity;
import com.github.johntinashe.hooked.LoginActivity;
import com.github.johntinashe.hooked.MessagesActivity;
import com.github.johntinashe.hooked.R;
import com.github.johntinashe.hooked.SettingsActivity;
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


    public static void setNavigationView(
            NavigationView navigationView,
            final DrawerLayout drawerLayout,
            ImageView drawerToggle,
            final Activity activity,
            LinearLayout linearLayout,
            final FirebaseAuth auth) {

        final View navHeaderView = navigationView.inflateHeaderView(R.layout.navigation_header);
        final TextView name = navHeaderView.findViewById(R.id.userNameHeader);
        final TextView status = navHeaderView.findViewById(R.id.userStatusHeader);
        final ImageView imageView = navHeaderView.findViewById(R.id.userProfileImgHeader);
//        header = navHeaderView;
        //  FirebaseFirestore db = FirebaseFirestore.getInstance();
//        if (auth.getCurrentUser() == null)
//            return;
//        DocumentReference documentReference = db.collection("users").document(auth.getCurrentUser().getUid());
//        documentReference.addSnapshotListener(activity,new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                if(documentSnapshot != null) {
//                    if(documentSnapshot.exists()){
//                        User user = documentSnapshot.toObject(User.class);
//                        name.setText(String.format("%s %s", user.getName(), user.getSurname()));
//                        status.setText(user.getMembership());
//
//                        if(!user.getThumb_image().equals("default")){
//                            Picasso.with(activity.getApplicationContext()).load(user.getThumb_image())
//                                    .placeholder(R.drawable.avatar).into(imageView);
//                        }
//                    }
//                }
//            }
//        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() != null) {
                    auth.signOut();
                    Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.getApplicationContext().startActivity(intent);
                    activity.finish();
                }
            }
        });

        drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.browse: {
                        Intent intent = new Intent(activity.getApplicationContext(), BrowseActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.getApplicationContext().startActivity(intent);
                        drawerLayout.closeDrawer(Gravity.START);
                        return true;
                    }
                    case R.id.messages: {
                        Intent intent = new Intent(activity.getApplicationContext(), MessagesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.getApplicationContext().startActivity(intent);
                        drawerLayout.closeDrawer(Gravity.START);
                        return true;
                    }
                    case R.id.settings: {
                        Intent intent = new Intent(activity.getApplicationContext(), SettingsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        drawerLayout.closeDrawer(Gravity.START);
                        activity.getApplicationContext().startActivity(intent);
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }



}
