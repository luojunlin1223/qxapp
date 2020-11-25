package com.example.qxapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.R;

public class Receive extends AppCompatActivity {
    private TextView username,content,time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_receive);

        initControl();
        initData();

    }

    private void initData() {
        username.setText("这是一个测试而已");
    }

    private void initControl() {
        username=findViewById(R.id.username);
        content=findViewById(R.id.content);
        time=findViewById(R.id.time);
    }
}
