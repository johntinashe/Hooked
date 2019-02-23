package com.github.johntinashe.hooked;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.johntinashe.hooked.model.Message;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("John","Hey wasssup !" , null));
        messages.add(new Message("Tinashe", "hello you !", null));
        messages.add(new Message("John","Hey how are you doing boss?" , null));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,messages);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }
}
