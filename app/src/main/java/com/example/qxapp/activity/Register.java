package com.example.qxapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Register extends AppCompatActivity {
    private EditText username,password;
    private ImageButton canclebtn;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initControl();
    }

    private void initControl() {
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        canclebtn=findViewById(R.id.cancelbtn);
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(v -> {
            final BmobUser user = new BmobUser();
            if (username.getText().toString().equals("")) {
                Toast.makeText(Register.this, "用户没有输入用户名", Toast.LENGTH_LONG).show();
            } else if (password.getText().toString().equals("")) {
                Toast.makeText(Register.this, "用户没有输入密码", Toast.LENGTH_LONG).show();
            } else if(username.getText().toString().length()<4){
                Toast.makeText(Register.this, "用户名长度小于4", Toast.LENGTH_LONG).show();
            }else if(password.getText().toString().length()<4){
                Toast.makeText(Register.this, "密码长度小于4", Toast.LENGTH_LONG).show();
            }else
            {
                user.setUsername(username.getText().toString().trim());
                user.setPassword(password.getText().toString().trim());
                user.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser user, BmobException e) {
                        if (e == null) {
                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Register.this,Login.class));
//                              注册成功跳转登录界面 销毁注册界面
                            finish();
                        } else {
                            Toast.makeText(Register.this, "注册失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}