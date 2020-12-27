package com.example.qxapp.activity;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.Adapter.RecomAdapter;
import com.example.qxapp.Adapter.RecomSearchAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Recommondation;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class RecomSearch extends AppCompatActivity {
    private MaterialEditText searchcontent;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recom_search);
        initControl();
    }
    private void search() {
        BmobQuery<Recommondation> bmobQuery=new BmobQuery<>();
        bmobQuery.include("user");
        bmobQuery.addWhereEqualTo("product",searchcontent.getText().toString());
        bmobQuery.findObjects(new FindListener<Recommondation>() {
            @Override
            public void done(List<Recommondation> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e == null)
                {
                    RecomAdapter recomAdapter=new RecomAdapter(getApplicationContext(), list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(recomAdapter);
                }else
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void initControl() {
        searchcontent=findViewById(R.id.search_content);
        searchcontent.setSingleLine();
        searchcontent.setLines(1);
        searchcontent.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchcontent.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId==EditorInfo.IME_ACTION_SEARCH){
                search();
            }
            return false;
        });

        Button cancelbtn = findViewById(R.id.cancel);

        cancelbtn.setOnClickListener(v -> finish());

        recyclerView=findViewById(R.id.search_recyclerview);
        swipeRefreshLayout=findViewById(R.id.search_swipe);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        上拉刷新
//        swipeRefreshLayout.setOnRefreshListener(this::Refresh);
////      中止上拉刷新
        swipeRefreshLayout.setEnabled(false);
    }





}
