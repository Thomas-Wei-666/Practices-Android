package com.example.basicnotifyactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btSendNotification = (Button) findViewById(R.id.bt_send_notification);
        btSendNotification.setOnClickListener(v -> {
            sendNotify(MainActivity.this);
        });
    }

    private void sendNotify(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//需要判断是否是androidO(8.0)以上的系统
            NotificationChannel channel = new NotificationChannel("001", "myChannel001", NotificationManager.IMPORTANCE_DEFAULT);//channel id 很重要！
            manager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(context, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context, "001").setContentTitle("Congratulations").
                setContentText("you have successfully send a notification!").setContentIntent(pendingIntent).setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setAutoCancel(true).setChannelId("001").
                        setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.img5))).build();//必须设置channel id不然不显示
        manager.notify(1, notification);
    }
}