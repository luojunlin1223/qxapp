package com.example.qxapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qxapp.R;
import com.example.qxapp.activity.Login;

import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class FragmentMine extends Fragment {
    private TextView username;
    private Button outbtn;
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
        BmobUser user=BmobUser.getCurrentUser(BmobUser.class);
        String id=user.getObjectId();
        BmobQuery<BmobUser> bmobUserBmobQuery=new BmobQuery<>();
        bmobUserBmobQuery.getObject(id, new QueryListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    username.setText(user.getUsername());
                }else {
                    Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initControl() {
        username= Objects.requireNonNull(getActivity()).findViewById(R.id.username);
        outbtn=getActivity().findViewById(R.id.out);
    }
}
