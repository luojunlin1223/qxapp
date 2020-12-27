package com.example.qxapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Recommondation;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class WriteRecom extends AppCompatActivity {
    private Button cancle,push;
    private MaterialEditText good_name,content;
    private TextView username;
    private int times=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_recom);
        inintControl();
        inintData();
    }

    private void inintData() {
        cancle.setOnClickListener(v -> finish());
        push.setOnClickListener(v -> {
            if(check()){
                times+=1;
                if(times==1){
                    push();
                }
                else{
                    finish();
                }
            }
        });
        username.setText(BmobUser.getCurrentUser(BmobUser.class).getUsername());
    }

    private boolean check() {
        if(good_name.getText().length()==0){
            Toast.makeText(getApplicationContext(),"商品名不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(content.getText().length()==0){
            Toast.makeText(getApplicationContext(),"商品名不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void push() {
        Recommondation recommondation=new Recommondation();
        recommondation.setUser(BmobUser.getCurrentUser(BmobUser.class));
        recommondation.setProduct(good_name.getText().toString());
        recommondation.setContent(content.getText().toString());
        recommondation.setThumbsdown(0);
        recommondation.setThumbsdown(0);
        recommondation.setCollect(0);
        recommondation.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void inintControl() {
        cancle=findViewById(R.id.cancel);
        push=findViewById(R.id.push);
        good_name=findViewById(R.id.good_name);
        content=findViewById(R.id.content);
        username=findViewById(R.id.username);
    }
}