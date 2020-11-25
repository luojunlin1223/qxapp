package com.example.qxapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.qxapp.Adapter.SectionPageAdapter;
import com.example.qxapp.Fragment.FragmentRecom;
import com.example.qxapp.Fragment.FragmentHome;
import com.example.qxapp.Fragment.FragmentMine;
import com.example.qxapp.Viewpager.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    private NoScrollViewPager noScrollViewPager;
    private BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControl();
    }

    private void initControl() {
        noScrollViewPager =findViewById(R.id.NoSrollViewPager);
        bottomNavigationBar=findViewById(R.id.bottom);
        initViewPager();
        initBottomNavigationBar();
    }
//    初始化底部导航栏
    private void initBottomNavigationBar() {
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        bottomNavigationBar.setBarBackgroundColor(R.color.white)
                .setActiveColor(R.color.Navigation_bottom_base)
                .setInActiveColor(R.color.black);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.homepage_fill,"首页").setInactiveIconResource(R.drawable.homepage))
                           .addItem(new BottomNavigationItem(R.drawable.recommendation_fill,"推荐").setInactiveIconResource(R.drawable.recommendation))
                           .addItem(new BottomNavigationItem(R.drawable.mine_fill,"我的").setInactiveIconResource(R.drawable.mine))
        .setFirstSelectedPosition(0)
        .initialise();
    }
//    初始化ViewPager
    private void initViewPager() {
//        加载页面的上限值
        noScrollViewPager.setOffscreenPageLimit(3);
//        FragmentList
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentHome());
        fragmentList.add(new FragmentRecom());
        fragmentList.add(new FragmentMine());

        noScrollViewPager.setAdapter(new SectionPageAdapter(getSupportFragmentManager(), fragmentList));
        noScrollViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

//            切换page自动切换底部导航栏
            @Override
            public void onPageSelected(int position) {
                bottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        noScrollViewPager.setCurrentItem(0);
        noScrollViewPager.setNoScroll(true);
    }

//  切换导航栏自动切换page
    @Override
    public void onTabSelected(int position) {
        noScrollViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}