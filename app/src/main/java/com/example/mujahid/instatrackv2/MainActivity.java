package com.example.mujahid.instatrackv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnGroupCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGroupCreate=(Button) findViewById(R.id.btnCreateGroup);
        btnGroupCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        
    }
}
