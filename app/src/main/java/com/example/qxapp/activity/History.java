package com.example.qxapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qxapp.Adapter.HistoryAdapter;
import com.example.qxapp.Adapter.InnerAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.SearchRecord;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class History extends AppCompatActivity {
    private ImageButton cancelbtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initControl();
        initData();
        Refresh();
    }

    void initData() {
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        BmobQuery<SearchRecord> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(1000);
        bmobQuery.findObjects(new FindListener<SearchRecord>() {
            @Override
            public void done(List<SearchRecord> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    HistoryAdapter historyAdapter=new HistoryAdapter(getApplicationContext(), list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(historyAdapter);
                }else{
//                    上拉刷新失败
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"获取数据失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}