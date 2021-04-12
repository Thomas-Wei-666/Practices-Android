package com.example.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.broadcastreceiver.MyReceiver;

public class BaseActivity extends AppCompatActivity {
    private MyReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Resume", "resumed");
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.MY_FORCE_OFFLINE");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        Log.i("Pause", "paused");
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("666", "succeed");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("FBI Warning");
            builder.setCancelable(false);
            builder.setMessage("You are forced to offline!");
            builder.setPositiveButton("OK", (Dialog, which) -> {
                ActivityCollector.finishALL();
                Intent intent1 = new Intent(context, MainActivity.class);
                context.startActivity(intent1);
            });
            builder.show();
        }
    }

}
