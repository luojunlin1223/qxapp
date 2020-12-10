package com.example.qxapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.Adapter.InnerAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.SearchRecord;

import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

public class FragmentinnerSearch extends BaseFragment{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentinnersearch,container,false);
    }

    @Override
    protected void initData() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        上拉刷新
        swipeRefreshLayout.setOnRefreshListener(this::Refresh);

    }



    @Override
    protected void initControl() {
        recyclerView= Objects.requireNonNull(getActivity()).findViewById(R.id.inner_recyclerview);
        swipeRefreshLayout=Objects.requireNonNull(getActivity()).findViewById(R.id.inner_swipe);

    }

    @Override
    protected void Refresh() {
        String bql="select * from SearchRecord order by count desc";
        BmobQuery<SearchRecord> bmobQuery = new BmobQuery<>();
        bmobQuery.setSQL(bql);
        bmobQuery.setLimit(1000);
        bmobQuery.doSQLQuery(new SQLQueryListener<SearchRecord>() {
            @Override
            public void done(BmobQueryResult<SearchRecord> bmobQueryResult, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    List<SearchRecord> list=bmobQueryResult.getResults();
                    if(list!=null&&list.size()>0){
                        InnerAdapter innerAdapter = new InnerAdapter(getActivity(), list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(innerAdapter);
                    }else {
                        Toast.makeText(getContext(),"没有相关记录",Toast.LENGTH_LONG).show();
                    }

                } else {
//                    上拉刷新失败
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
