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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qxapp.Adapter.HomeAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Post;
import com.example.qxapp.activity.Search;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FragmentHome extends Fragment {
    private static ViewPager2 viewPager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmenthome,container,false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initControl();
        initData();
    }

    private void initData() {
    }

    private void initControl() {
        List<Fragment> fragmentList =new ArrayList<>();
        fragmentList.add(new FragmentinnerSearch());
        fragmentList.add(new FragmentouterSearch());
        viewPager2=getActivity().findViewById(R.id.home_viewpager);
        HomeAdapter homeAdapter=new HomeAdapter(getActivity(),fragmentList);
        viewPager2.setAdapter(homeAdapter);
        attachTabLayoutOnViewPager2();
    }

    private void attachTabLayoutOnViewPager2() {
        final List<String> tabName = new ArrayList<>();
        tabName.add("肖战");
        tabName.add("李逸飞");
        TabLayout tabLayout=getActivity().findViewById(R.id.home_tablayout);
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout,viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabName.get(position));
            }
        });
        tabLayoutMediator.attach();
    }
}
