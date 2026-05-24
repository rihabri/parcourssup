package com.example.parcourssup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);


        loginBtn.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class)));


        registerBtn.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Step1Activity.class)));
    }
}