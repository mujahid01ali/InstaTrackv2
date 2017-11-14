package com.example.mujahid.instatrackv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    TextView textPhone;
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        textPhone=(TextView) findViewById(R.id.etPhoneSignup);
        btnNext=(Button) findViewById(R.id.btnSignup);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String url="";
    }
}
