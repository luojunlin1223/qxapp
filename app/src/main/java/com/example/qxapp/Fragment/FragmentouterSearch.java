package com.example.qxapp.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.Adapter.InnerAdapter;
import com.example.qxapp.Adapter.outerAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.SearchRecord;
import com.example.qxapp.activity.Search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentouterSearch extends Fragment {
    private List<String> goodlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentoutersearch, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        recyclerView=getActivity().findViewById(R.id.outer_recyclerview);
        swipeRefreshLayout=getActivity().findViewById(R.id.outer_swipe);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        上拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://zhekou.manmanbuy.com/defaultsharehot.aspx";
                        Document homepage = null;
                        try {
                            homepage = getDocument(url, "");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Elements elements = homepage.select("li[class='item']");
                        for (Element item : elements) {
                            String content = item.getElementsByClass("content").select("h2").text();
                            goodlist.add(content);
                        }

                    }
                });
                thread.start();
                try {
                    thread.join();
                    swipeRefreshLayout.setRefreshing(false);
                    outerAdapter outerAdapter = new outerAdapter(getContext(), goodlist);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(outerAdapter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String url = "http://zhekou.manmanbuy.com/defaultsharehot.aspx";
                Document homepage = null;
                try {
                    homepage = getDocument(url, "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements elements = homepage.select("li[class='item']");
                for (Element item : elements) {
                    String content = item.getElementsByClass("content").select("h2").text();
                    goodlist.add(content);
                }
                swipeRefreshLayout.setRefreshing(false);
                outerAdapter outerAdapter = new outerAdapter(getContext(), goodlist);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(outerAdapter);
            }
        });


    }



    private Document getDocument(String url, String cookie) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
                .addHeader("Cookie", cookie)
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .build();
        Response response = client
                .newCall(request)
                .execute();

        String html = Objects.requireNonNull(response.body()).string();
        return Jsoup.parse(html);
    }
}