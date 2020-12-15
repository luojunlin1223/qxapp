package com.example.qxapp.activity.AlterProset;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Proset;
import com.example.qxapp.activity.ProsetReceive;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class Altername extends AppCompatActivity {
    private ImageButton canclebtn;
    private MaterialEditText name;
    private Button savebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altername);
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
        name.setText(getIntent().getStringExtra("name"));
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"名称不能不设为空",Toast.LENGTH_SHORT).show();
                    name.setText(getIntent().getStringExtra("name"));
                }else{
                    Intent intent=new Intent();
                    intent.putExtra("name",name.getText().toString());
                    setResult(1,intent);
                    finish();
                }
            }
        });
    }

    private void initControl() {
        canclebtn=findViewById(R.id.cancelbtn);
        savebtn=findViewById(R.id.savebtn);
        name=findViewById(R.id.name);
    }
}