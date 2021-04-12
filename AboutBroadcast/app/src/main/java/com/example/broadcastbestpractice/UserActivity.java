package com.example.broadcastbestpractice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Button btForceToOffline = (Button) findViewById(R.id.bt_force_offline);
        btForceToOffline.setOnClickListener(v -> {
            Intent intent = new Intent("com.example.MY_FORCE_OFFLINE");
            sendBroadcast(intent);
        });
    }
}