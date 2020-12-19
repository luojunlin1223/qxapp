package com.example.qxapp.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.R;

public class Update extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ImageButton canclebtn = findViewById(R.id.cancelbtn);
        canclebtn.setOnClickListener(v -> finish());
        TextView content = findViewById(R.id.content);
        String content1="版本号：1.0\n"
                +"开发者：1219\n"
                +"发布时间：2020-12-16";
        content.setText(content1);
    }
}