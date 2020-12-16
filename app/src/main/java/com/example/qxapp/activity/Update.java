package com.example.qxapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.qxapp.R;

public class Update extends AppCompatActivity {
    private TextView content;
    private ImageButton canclebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        canclebtn=findViewById(R.id.cancelbtn);
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        content=findViewById(R.id.content);
        String content1="版本号：1.0\n"
                +"开发者：1219\n"
                +"发布时间：2020-12-16";
        content.setText(content1);
    }
}