package com.aurelia_truly_jessica.transvice_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent moveToLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(moveToLogin);
                finish();
            }
        },3000L);
    }
}