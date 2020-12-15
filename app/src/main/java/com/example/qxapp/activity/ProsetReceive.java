package com.example.qxapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.qxapp.Fragment.FragmentProset;
import com.example.qxapp.R;

import java.util.Objects;

public class ProsetReceive extends AppCompatActivity {
    private ImageButton cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proset_receive);
        FragmentProset fragmentProset = new FragmentProset();

        String name=getIntent().getStringExtra("name");
        int price_low=getIntent().getIntExtra("price_low",0);
        int price_high=getIntent().getIntExtra("price_high",0);
        String id=getIntent().getStringExtra("id");
        String brand=getIntent().getStringExtra("brand");
        String where=getIntent().getStringExtra("where");

        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("price_low",price_low);
        bundle.putInt("price_high",price_high);
        bundle.putString("id",id);
        bundle.putString("brand",brand);
        bundle.putString("where",where);
        fragmentProset.setArguments(bundle);

        getSupportFragmentManager()    //
                .beginTransaction()
                .replace(R.id.proset_fragment, fragmentProset,"Proset")   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();

        initContrl();
        initData();

    }

    private void initData() {
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction("action.updateproset");
                sendBroadcast(intent);
                finish();
            }
        });

    }

    private void initContrl() {
        cancle=findViewById(R.id.cancelbtn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentProset fragmentProset= (FragmentProset) getSupportFragmentManager().findFragmentByTag("Proset");
        fragmentProset.onActivityResult(requestCode,resultCode,data);
    }
}