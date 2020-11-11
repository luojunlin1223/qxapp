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

import com.example.qxapp.Adapter.SearchAdapter;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Product;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class Search extends AppCompatActivity {
    private EditText searchcontent;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_search);
        initControl();
//        强制开启搜索栏输入法
        searchcontent.requestFocus();
        InputMethodManager inputMethodManager= (InputMethodManager) searchcontent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }

    private void search() {
        String url="https://list.tmall.com/search_product.htm?q="+searchcontent.getText().toString();
        new Thread(() -> {
            try {
                Connection connection=Jsoup.connect(url);
                connection.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
                Document document=connection.get();
                Elements ulList = document.select("div[id='J_ItemList']");
                Elements liList = ulList.select("div[class='product']");
                for(Element item:liList){
                   Product product=new Product();
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
        }).start();
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

        spinner=findViewById(R.id.spinner);
        spinner.setDropDownHorizontalOffset(150);
        spinner.setDropDownVerticalOffset(50);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        BmobQuery<Product> productBmobQuery = new BmobQuery<>();
//        获取到排序方法
        int position=spinner.getSelectedItemPosition();
        switch (position){
            case 0:productBmobQuery.order("price");break;
            case 1:productBmobQuery.order("sell");break;
        }
        productBmobQuery.setLimit(1000);
        productBmobQuery.findObjects(new FindListener<Product>() {
            @Override
            public void done(List<Product> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    SearchAdapter searchAdapter=new SearchAdapter(getApplicationContext(), list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(searchAdapter);
                }else{
//                    上拉刷新失败
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(Search.this,"获取数据失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
