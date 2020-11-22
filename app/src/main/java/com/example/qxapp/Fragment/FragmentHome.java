package com.example.qxapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.Adapter.HomeAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Post;
import com.example.qxapp.activity.Search;

import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FragmentHome extends BaseFragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView searchcontent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmenthome,container,false);
    }

    @Override
    protected void initData() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        上拉刷新
        swipeRefreshLayout.setOnRefreshListener(this::Refresh);
        searchcontent.setOnClickListener(v -> startActivity(new Intent(getActivity(), Search.class)));
    }

    @Override
    protected void initControl() {
        recyclerView= Objects.requireNonNull(getActivity()).findViewById(R.id.home_recyclerview);
        swipeRefreshLayout=getActivity().findViewById(R.id.home_swipe);
        searchcontent=getActivity().findViewById(R.id.home_searchcontent);
    }

    @Override
    protected void Refresh() {
        BmobQuery<Post> postBmobQuery = new BmobQuery<>();
        postBmobQuery.order("-createdAt");
        postBmobQuery.setLimit(1000);
        postBmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    HomeAdapter homeAdapter=new HomeAdapter(getActivity(), list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(homeAdapter);
                }else{
//                    上拉刷新失败
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
