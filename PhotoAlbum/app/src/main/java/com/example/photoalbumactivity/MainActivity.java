package com.example.photoalbumactivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    private List<PhotoData> photoDataList = new ArrayList<>();
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<String> photoPaths = new ArrayList<>();
    private PhotoRvAdapter photoRvAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private final int WRITE_EXTERNAL_STORAGE_Code = 0;
    private final int CHOOSE_PHOTO = 233;
    private final String KEY_PhotoLocals = "photoLocals";
    private int count = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itm_import: {
                startSystemPhotoAlbum();
                break;
            }
            case R.id.itm_save: {
                if (photoDataList.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定", null);
                    builder.setMessage("您还未导入任何图片");
                    builder.setTitle("提示");
                    builder.show();
                } else {
                    savePhotoPaths();
                }
                break;
            }
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePhotoPaths();
    }

    private void savePhotoPaths() {
        SharedPreferences.Editor editorLocation = sharedPreferences.edit();
        for (PhotoData e : photoDataList) {
            editorLocation.putString(KEY_PhotoLocals + count, e.getPhotoPaths());
            count++;
        }
        count = 0;
        editorLocation.apply();
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_Code: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "你已授权", Toast.LENGTH_SHORT).show();
                    Log.i("permission", "granted");
                } else {
                    Log.i("permission", "denied");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("警告");
                    builder.setMessage("你已拒绝授权" +
                            "/n" +
                            "点击确定退出程序");
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定", ((dialog, which) -> {
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
        sharedPreferences = this.getSharedPreferences(KEY_PhotoLocals, MODE_PRIVATE);
        if (!importSavedPhoto(sharedPreferences)) {
            Log.i("importSaved", "数据库为空");
        } else {
            refreshData();
        }


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tb_toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        setSupportActionBar(toolbar);
        swipeRefreshLayout.setOnRefreshListener(() -> refreshData());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, WRITE_EXTERNAL_STORAGE_Code);
        }


        RecyclerView photoRecyclerview = findViewById(R.id.rv_photo);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        photoRvAdapter = new PhotoRvAdapter(photoDataList, this, photoPaths, sharedPreferences);
        photoRecyclerview.setAdapter(photoRvAdapter);
        photoRecyclerview.setLayoutManager(gridLayoutManager);

    }

    private boolean importSavedPhoto(SharedPreferences sharedPreferences) {
        String e = "";
        if (sharedPreferences.getString(KEY_PhotoLocals + count, null) == null) {
            return false;
        }
        while ((e = sharedPreferences.getString(KEY_PhotoLocals + count, null)) != null) {
            photoPaths.add(e);
            count++;
        }
        count = 0;
        return true;
    }

    public void refreshData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        photoDataList.clear();
                        for (int i = 0; i < photoPaths.size(); i++) {
                            PhotoData photoData = new PhotoData(photoPaths.get(i));
                            photoDataList.add(photoData);
                        }
                        photoRvAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedPhotos = data.getData();
                        String[] projection = {MediaStore.Images.Media.DATA};
                        String path;
                        path = getImagePath(selectedPhotos, projection);
                        if (!isAlreadyImported(path)) {
                            photoPaths.add(path);
                        }
                        refreshData();
                        //Log.i("photopath", photoPaths.get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i("import photo", "failed");
                }
            }
        }
    }

    private boolean isAlreadyImported(String path) {
        int index = 0;
        for (; index < photoPaths.size(); index++) {
            if (path.equals(photoPaths.get(index))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("该照片已被导入");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", (dialog, which) -> {
                });
                builder.show();
                return true;
            }
        }
        return false;
    }

    private String getImagePath(Uri selectedPhotos, String[] projection) {
        Cursor cursor = getContentResolver().query(selectedPhotos, projection, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(projection[0]));
        cursor.close();
        return path;
    }

    public void startSystemPhotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
}