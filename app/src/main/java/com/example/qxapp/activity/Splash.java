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
        timer.schedule(timerTask, 700);

    }
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this,Splashgif.class));
//              完成跳转后关闭splashactivity
                finish();
            }
        };

}
