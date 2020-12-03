package com.example.qxapp.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.bmob.v3.Bmob;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bmob.initialize(getActivity(),"af0f3ed9dd1637949a243bd203f9de39");
//      先刷新一次
        Refresh();
        initControl();
        initData();

//        逻辑处理
    }
    protected abstract void initData();

    protected abstract void initControl();

    protected abstract void Refresh();
}
