package com.example.qxapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qxapp.Adapter.HomeAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Search;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private static ViewPager2 viewPager2;
    private TextView textView;
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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Search.class);
                startActivity(intent);
            }
        });
    }

    private void initControl() {
        textView=getActivity().findViewById(R.id.home_searchcontent);

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
        tabName.add("站内热点");
        tabName.add("全网热点");
        TabLayout tabLayout=getActivity().findViewById(R.id.home_tablayout);
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout,viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabName.get(position));
                tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.green));
            }
        });
        tabLayoutMediator.attach();
    }
}
