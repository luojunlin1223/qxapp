package com.example.qxapp.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.Adapter.RecomAdapter;
import com.example.qxapp.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Recommondation extends AppCompatActivity{
    private ImageButton cancelbtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommondation);
        initControl();
        initData();
        Refresh();
    }

    void initData() {
        cancelbtn.setOnClickListener(v -> finish());

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        上拉刷新
        swipeRefreshLayout.setOnRefreshListener(this::Refresh);
    }

    void initControl() {
        cancelbtn=findViewById(R.id.cancelbtn);
        swipeRefreshLayout=findViewById(R.id.swipe);
        recyclerView=findViewById(R.id.recyclerview);

    }
    void Refresh() {
        BmobQuery<com.example.qxapp.activity.Bean.Recommondation> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("user",BmobUser.getCurrentUser(BmobUser.class));
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(1000);
        bmobQuery.include("user");
        bmobQuery.findObjects(new FindListener<com.example.qxapp.activity.Bean.Recommondation>() {
            @Override
            public void done(List<com.example.qxapp.activity.Bean.Recommondation> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    RecomAdapter recomAdapter =new RecomAdapter(getApplicationContext(), list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(recomAdapter);
                }else{
//                    上拉刷新失败
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"获取数据失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}