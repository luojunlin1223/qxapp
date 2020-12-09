package com.example.qxapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.qxapp.Fragment.FragmentProset;
import com.example.qxapp.R;

import org.greenrobot.eventbus.EventBus;

import java.sql.Time;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobUser;

public class Proset extends AppCompatActivity {
    private ImageButton canclebtn;
    private String name;
    private double price_low,price_high;
    private int percentage;
    private TextView test;
    private Boolean isUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proset);
        getFragmentManager().beginTransaction().replace(R.id.proset_fragment,new FragmentProset()).commit();
        initControl();
        initData();
    }

    private void initData() {
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initControl() {
        canclebtn=findViewById(R.id.cancelbtn);
        test=findViewById(R.id.test);
    }
}