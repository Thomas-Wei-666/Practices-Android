package com.example.photoalbumactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<DataTest> dataTests = new ArrayList<DataTest>();
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int WRITE_EXTERNAL_STORAGE_Code = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_act_menu,menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_Code: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "你已授权", Toast.LENGTH_SHORT).show();
                    Log.i("permission","granted");
                } else {
                    Log.i("permission","denied");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("警告");
                    builder.setMessage("你已拒绝授权" +
                            "/n" +
                            "点击确定退出程序");
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定",((dialog, which) -> {
                        finish();
                    }));
                }
                break;
            }
            default:
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, WRITE_EXTERNAL_STORAGE_Code);
        }


        initialize();

        RecyclerView photoRecyclerview = (RecyclerView) findViewById(R.id.rv_photo);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        PhotoRvAdapter photoRvAdapter = new PhotoRvAdapter(dataTests);
        photoRecyclerview.setAdapter(photoRvAdapter);
        photoRecyclerview.setLayoutManager(gridLayoutManager);
    }

    public void initialize() {
        String testText;
        int index = 0;
        for (; index < 20; index++) {
            testText = Integer.valueOf(index).toString();
            Log.i("int", testText);
            DataTest dataTest = new DataTest(testText);
            dataTests.add(dataTest);
        }
    }
}