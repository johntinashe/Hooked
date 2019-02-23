package com.github.johntinashe.hooked;

import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.github.johntinashe.hooked.Utils.Utils;
import com.github.johntinashe.hooked.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowseActivity extends AppCompatActivity {

    @BindView(R.id.browse_rv) RecyclerView recyclerView;

    private ArrayList<String> uris;
    FirestorePagingAdapter<User, UserViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ButterKnife.bind(this);
        toolbar();

        FirebaseFirestore mDB = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        Query baseQuery = mDB.collection("users").orderBy("age", Query.Direction.ASCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(1)
                .build();

        final FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, User.class)
                .build();


        adapter = new FirestorePagingAdapter<User, UserViewHolder>(options) {
                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.browse_item,parent,false);
                        return new UserViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {

//
//                            uris = new ArrayList<>();
//                            uris.add("https://images.pexels.com/photos/87840/daisy-pollen-flower-nature-87840.jpeg?cs=srgb&dl=plant-flower-macro-87840.jpg&fm=jpg");
//                            uris.add("https://bornrealist.com/wp-content/uploads/2017/11/Here-Are-Top-10-Cute-Animals-That-Might-Actually-Kill-You.jpg");
//                            uris.add("https://www.planwallpaper.com/static/images/animals-4.jpg");
//                            uris.add("https://static.boredpanda.com/blog/wp-content/uuuploads/albino-animals/albino-animals-3.jpg");
//
//                            holder.carouselView.setPageCount(uris.size());
                    }
                };

        recyclerView.setAdapter(adapter);


    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private CarouselView carouselView;
        private ConstraintLayout constraintLayout;

         UserViewHolder(@NonNull View itemView) {
            super(itemView);
            carouselView = itemView.findViewById(R.id.story_view);
            carouselView.setImageListener(imageListener);
            constraintLayout = itemView.findViewById(R.id.browseView);
        }

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                if (uris != null) {
                    Picasso.get().load(uris.get(position)).into(imageView);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }

    private void toolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Utils.setToolbar(this,getSupportActionBar());
    }

    public void match (View view) {
        startActivity(new Intent(this,MutualLikeActivity.class));
    }

    public void settings(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }

}