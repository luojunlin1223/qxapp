package com.example.qxapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.Adapter.MineAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Recommondation;
import com.example.qxapp.activity.Bean.Recommondation;
import com.example.qxapp.activity.Bean.SearchRecord;
import com.example.qxapp.activity.Login;
import com.example.qxapp.activity.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class FragmentMine extends Fragment {
    private TextView username;
    private Button outbtn;
    private RecyclerView recyclerView;
    private TextView searchcount,recomcount,thumbsupcount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentmine,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initControl();
        getMyinfo();
//        加载个人信息
        outbtn.setOnClickListener(v -> {
//         清除用户缓存
            BmobUser.logOut();
            //                跳转登录界面
            startActivity(new Intent(getActivity(), Login.class));
            Objects.requireNonNull(getActivity()).finish();

        });
    }

    private void getMyinfo() {
       username.setText(BmobUser.getCurrentUser(BmobUser.class).getUsername());
       BmobQuery<SearchRecord>recordBmobQuery=new BmobQuery<>();
        recordBmobQuery.addWhereEqualTo("user",BmobUser.getCurrentUser(BmobUser.class));
        recordBmobQuery.findObjects(new FindListener<SearchRecord>() {
            @Override
            public void done(List<SearchRecord> list, BmobException e) {
                if (e == null) {
                    int count=0;
                    for(SearchRecord item:list){
                        count += item.getCount();
                    }
                    searchcount.setText(String.valueOf(count));
                } else {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
        BmobQuery<Recommondation> recommondationBmobQuery=new BmobQuery<>();
        recommondationBmobQuery.addWhereEqualTo("user",BmobUser.getCurrentUser(BmobUser.class));
        recommondationBmobQuery.findObjects(new FindListener<Recommondation>() {
            @Override
            public void done(List<Recommondation> list, BmobException e) {
                if (e == null) {
                    int recom_count=0;
                    int thumbs_upcount=0;
                    for(Recommondation item:list){
                        recom_count +=1;
                        thumbs_upcount+=item.getThumbsup();
                    }
                    recomcount.setText(String.valueOf(recom_count));
                    thumbsupcount.setText(String.valueOf(thumbs_upcount));
                } else {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initControl() {
        username= Objects.requireNonNull(getActivity()).findViewById(R.id.username);
        outbtn=getActivity().findViewById(R.id.out);
        recyclerView=getActivity().findViewById(R.id.mine_recycler);
        searchcount=getActivity().findViewById(R.id.user_search_count);
        recomcount=getActivity().findViewById(R.id.recom_count);
        thumbsupcount=getActivity().findViewById(R.id.thumbsup_count);

        List<String> data=new ArrayList<>();
        data.add("历史");
        data.add("收藏");
        data.add("我的推荐");
        data.add("版本更新");
        MineAdapter mineAdapter=new MineAdapter(getActivity(),data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mineAdapter);
    }

}
