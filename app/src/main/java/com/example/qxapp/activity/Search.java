package com.example.qxapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qxapp.Adapter.jd_SearchAdapter;
import com.example.qxapp.Adapter.tmall_SearchAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.jd_Product;
import com.example.qxapp.activity.Bean.tmall_Product;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Search extends AppCompatActivity {
    private EditText searchcontent;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner sort_spinner;
    private Spinner from_spinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_search);
//        初始化控件
        initControl();
//        强制开启搜索栏输入法
        searchcontent.requestFocus();
        InputMethodManager inputMethodManager= (InputMethodManager) searchcontent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }

    private void search() {
        String tianmao_url="https://list.tmall.com/search_product.htm?q="+searchcontent.getText().toString();
        String jingdong_url="https://search.jd.com/Search?keyword="+searchcontent.getText().toString();
        int fromposition=from_spinner.getSelectedItemPosition();
//        选择来源进行搜索
        switch (fromposition){
            case 0:{
                new Thread(() -> {
                    try {
                        Connection connection=Jsoup.connect(tianmao_url);
                        connection.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
                        Document document=connection.get();
                        Elements ulList = document.select("div[id='J_ItemList']");
                        Elements liList = ulList.select("div[class='product']");
                        for(Element item:liList){
                            tmall_Product product=new tmall_Product();
                            product.setName(item.select("p[class='productTitle']").select("a").attr("title"));
                            product.setPrice(item.select("p[class='productPrice']").select("em").attr("title"));
                            product.setSell(item.select("div[class='product']").select("p[class='productStatus']").select("span").select("em").text());
                            product.setUrl(item.select("p[class='productTitle']").select("a").attr("href"));
                            product.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        Toast.makeText(Search.this,"添加成功",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(Search.this,"添加失败",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        Refresh();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();break;
            }
            case 1:{
                new Thread(()->{
                    try {
                        OkHttpClient client=new OkHttpClient();
                        Request request=new Request.Builder()
                                .url(jingdong_url)
                                .build();
                        Response response=client.newCall(request).execute();

                        String html= Objects.requireNonNull(response.body()).string();
                        Document document=Jsoup.parse(html);
                        Element element=document.getElementById("J_goodsList");
                        Elements elements=element.getElementsByTag("li");
                        for(Element item:elements){
                            jd_Product product=new jd_Product();
                            product.setName(item.getElementsByClass("p-name").select("em").text());
                            product.setPrice(item.getElementsByClass("p-price").select("i").text());
                            product.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        Toast.makeText(Search.this,"添加成功",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(Search.this,"添加失败",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        Refresh();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();break;
            }
        }

    }

    private void initControl() {
        searchcontent=findViewById(R.id.search_content);
        Button cancelbtn = findViewById(R.id.cancel);
        cancelbtn.setOnClickListener(v -> finish());
        Button searchbtn = findViewById(R.id.search);
        searchbtn.setOnClickListener(v -> search());
        recyclerView=findViewById(R.id.search_recyclerview);
        swipeRefreshLayout=findViewById(R.id.search_swipe);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        上拉刷新
        swipeRefreshLayout.setOnRefreshListener(this::Refresh);


        sort_spinner=findViewById(R.id.sort_spinner);
        sort_spinner.setDropDownHorizontalOffset(150);
        sort_spinner.setDropDownVerticalOffset(50);
        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        from_spinner=findViewById(R.id.from_spinner);
        from_spinner.setDropDownHorizontalOffset(150);
        from_spinner.setDropDownVerticalOffset(50);
        from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void Refresh() {
        //        获取到排序方法
        int sort_position=sort_spinner.getSelectedItemPosition();
//        获取数据来源
        int from_position=from_spinner.getSelectedItemPosition();
        switch (from_position){
            case 0:{
                BmobQuery<tmall_Product> tmall_productBmobQuery = new BmobQuery<>();
                switch (sort_position){
                    case 0:tmall_productBmobQuery.order("price");break;
                    case 1:tmall_productBmobQuery.order("sell");break;
                }
                tmall_productBmobQuery.setLimit(1000);
                tmall_productBmobQuery.findObjects(new FindListener<tmall_Product>() {
                    @Override
                    public void done(List<tmall_Product> list, BmobException e) {
                        swipeRefreshLayout.setRefreshing(false);
                        if(e==null){
                            tmall_SearchAdapter tmall_searchAdapter=new tmall_SearchAdapter(getApplicationContext(), list);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(tmall_searchAdapter);
                        }else{
//                    上拉刷新失败
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(Search.this,"获取数据失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            }
            case 1:{
                BmobQuery<jd_Product> jd_productBmobQuery=new BmobQuery<>();
                switch (sort_position){
                    case 0:jd_productBmobQuery.order("price");break;
                    case 1:jd_productBmobQuery.order("sell");break;
                }
                jd_productBmobQuery.setLimit(1000);
                jd_productBmobQuery.findObjects(new FindListener<jd_Product>() {
                    @Override
                    public void done(List<jd_Product> list, BmobException e) {
                        swipeRefreshLayout.setRefreshing(false);
                        if(e==null){
                            jd_SearchAdapter jd_searchAdapter=new jd_SearchAdapter(getApplicationContext(),list);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(jd_searchAdapter);
                        }else{
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(Search.this,"获取数据失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;}
        }

    }
}
