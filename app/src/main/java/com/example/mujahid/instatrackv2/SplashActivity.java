package com.example.mujahid.instatrackv2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar;
    public static int splash_time_out=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefManager.getInstance(SplashActivity.this).isLogin()){
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(SplashActivity.this,Login.class);
                    startActivity(intent);
                }

            }
        },splash_time_out);
    }
}
