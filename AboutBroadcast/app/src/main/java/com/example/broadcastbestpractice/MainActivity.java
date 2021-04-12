package com.example.broadcastbestpractice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    private final String setUserName = "ThomasWei666";

    private final String setPassword = "12345678";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etUserName = (EditText) findViewById(R.id.et_username);
        EditText etPassword = (EditText) findViewById(R.id.et_password);
        Button btLogIn = (Button) findViewById(R.id.bt_login);
        btLogIn.setOnClickListener(v -> {
            if (checkToLogIn(etUserName.getText().toString(), etPassword.getText().toString())) {
                Intent intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Loging in...", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkToLogIn(String userName, String password) {
        return userName.equals(setUserName) && password.equals(setPassword);
    }
}