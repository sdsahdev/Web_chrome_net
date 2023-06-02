package com.example.web_view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    LinearLayout bt1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = findViewById(R.id.bt1);
        bt1.setOnClickListener(v-> {
            Intent intent =new Intent(MainActivity.this, Web_Activity.class);
            startActivities(new Intent[]{intent});
        });
    }
}