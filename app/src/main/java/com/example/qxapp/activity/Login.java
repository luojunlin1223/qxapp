package com.example.qxapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.MainActivity;
import com.example.qxapp.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login extends AppCompatActivity{
    private MaterialEditText username,password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//获取控件
        initControl();
    }

    private void initControl() {
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

        Button loginbtn = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
//        设置登录按钮监听器
        loginbtn.setOnClickListener(v -> {
            final BmobUser user = new BmobUser();
            user.setUsername(Objects.requireNonNull(username.getText()).toString().trim());
            user.setPassword(Objects.requireNonNull(password.getText()).toString().trim());
            user.login(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser u, BmobException e) {
                    if (e == null) {
                        Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_LONG).show();
//                            登录成功跳转主界面后销毁登录界面
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "登陆失败", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
//        设置注册按钮监听器
        register.setOnClickListener(v -> {
            Login.this.startActivity(new Intent(Login.this, Register.class));
//                跳转注册页面不需要销毁登录界面
        });
    }


}
