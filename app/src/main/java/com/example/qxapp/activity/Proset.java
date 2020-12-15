package com.example.qxapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.qxapp.Adapter.HistoryAdapter;
import com.example.qxapp.Adapter.ProsetAdapter;
import com.example.qxapp.Adapter.SimpleItemTouchHelperCallback;
import com.example.qxapp.Fragment.FragmentProset;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.SearchRecord;

import org.greenrobot.eventbus.EventBus;

import java.sql.Time;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Proset extends AppCompatActivity {
    private ImageButton cancelbtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ImageButton newbtn;
    private MessageReceiver mr= new MessageReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proset);

        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("action.updateproset");
        registerReceiver(mr, filter);

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
        newbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),WriteProset.class);
                startActivityForResult(intent,1000);
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
        newbtn=findViewById(R.id.newbtn);

    }
    void Refresh() {
        BmobQuery<com.example.qxapp.activity.Bean.Proset> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(1000);
        bmobQuery.findObjects(new FindListener<com.example.qxapp.activity.Bean.Proset>() {
            @Override
            public void done(List<com.example.qxapp.activity.Bean.Proset> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    ProsetAdapter prosetAdapter=new ProsetAdapter(getApplicationContext(), list);
                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(prosetAdapter);
                    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                    touchHelper.attachToRecyclerView(recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(prosetAdapter);
                }else{
//                    上拉刷新失败
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"获取数据失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000&&resultCode==1){
            Refresh();
        }
        if(requestCode==999&&resultCode==1){
            Refresh();
        }
    }
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("action.updateproset")){          // 如果是该广播，则执行XXXX操作
                Refresh();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mr);
    }
}