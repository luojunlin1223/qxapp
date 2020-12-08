package com.example.qxapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.qxapp.Fragment.FragmentProset;
import com.example.qxapp.R;

public class Proset extends AppCompatActivity {
    private ImageButton canclebtn;
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
    }
}