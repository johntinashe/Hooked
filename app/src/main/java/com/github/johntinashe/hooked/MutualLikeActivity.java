package com.github.johntinashe.hooked;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.github.johntinashe.hooked.Utils.Utils;

public class MutualLikeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutual_like);
        toolbar();
    }

    private void toolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Utils.setToolbar(this,getSupportActionBar());

        Button sendMessage = findViewById(R.id.send_msg_btn);
        Button continueBtn = findViewById(R.id.keep_brw_btn);
        final Intent settings = new Intent(this,SettingsActivity.class);
        final Intent messages = new Intent(this,MessagesActivity.class);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(messages);
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(settings);
            }
        });

    }
}
