package com.example.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

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


