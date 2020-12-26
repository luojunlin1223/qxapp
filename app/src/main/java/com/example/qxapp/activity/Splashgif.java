package com.example.qxapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.qxapp.MainActivity;
import com.example.qxapp.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class Splashgif extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashgif);

        Timer timer = new Timer();
        timer.schedule(timerTask, 5700);
    }
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            Bmob.initialize(Splashgif.this,"af0f3ed9dd1637949a243bd203f9de39");
            //已登录：主界面
            // 未登录：登陆界面
            if (BmobUser.isLogin()) {
                startActivity(new Intent(Splashgif.this, MainActivity.class));
            } else {
                startActivity(new Intent(Splashgif.this,Login.class));
            }
            finish();
        }
    };

}