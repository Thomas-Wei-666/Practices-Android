package com.example.siginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String usrName = "thomas";
    private final String password = "000000";

    private EditText etUserName;
    private EditText etPassword;
    private Button btSignIn;
    private CheckBox cbRememberPassword;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName = (EditText) findViewById(R.id.et_usr_id);
        etPassword = (EditText) findViewById(R.id.et_password);
        cbRememberPassword = (CheckBox) findViewById(R.id.cb_rm_password);
        btSignIn = (Button) findViewById(R.id.bt_sign_in);

        preferences = getSharedPreferences("myfile",MODE_PRIVATE);
        boolean isRemembered = preferences.getBoolean("rememberPassword", false);
        if (isRemembered) {
            restorePassword();
        }


        btSignIn.setOnClickListener(v -> {
            if (checkId(etUserName.getText().toString(), etPassword.getText().toString())) {
                if (cbRememberPassword.isChecked()) {
                    storePassword(etUserName.getText().toString(), etPassword.getText().toString());
                }
                Intent intent = new Intent(MainActivity.this, UsrSpaceActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", ((dialog1, which) -> {
                    etPassword.setText("");
                }));
                dialog.setTitle("提示");
                dialog.setMessage("用户名或密码错误");
            }
        });
    }

    private boolean checkId(String inputUserName, String inputPassword) {
        return inputUserName.equals(usrName) && inputPassword.equals(password);
    }

    private void storePassword(String id, String myPassword) {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("Password", myPassword);
        editor.putString("usrName", id);
        editor.putBoolean("rememberPassword",true);
        editor.apply();


    }

    private void restorePassword() {
        etUserName.setText(preferences.getString("usrName", ""));
        etPassword.setText(preferences.getString("Password", ""));

    }
}