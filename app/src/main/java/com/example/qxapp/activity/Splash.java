package com.example.qxapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.MainActivity;
import com.example.qxapp.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//程序入口 显示欢迎页面
//        显示时长为了两秒
        Timer timer = new Timer();
        timer.schedule(timerTask, 2000);

    }
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Bmob.initialize(Splash.this,"af0f3ed9dd1637949a243bd203f9de39");
                //已登录：主界面 未登录：登陆界面
                if (BmobUser.isLogin()) {
                    startActivity(new Intent(Splash.this,MainActivity.class));
                } else {
                    startActivity(new Intent(Splash.this,Login.class));
                }
                finish();
            }
        };

}
