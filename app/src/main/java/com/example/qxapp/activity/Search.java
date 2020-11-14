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
import com.example.qxapp.activity.Bean.SearchRecord;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
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
        Thread tmallThread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection=Jsoup.connect(tianmao_url);
                    connection.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
                    Document document=connection.get();
                    Elements ulList = document.select("div[id='J_ItemList']");
                    Elements liList = ulList.select("div[class='product']");
                    for(Element item:liList){
                        Product product=new Product();
                        product.setName(item.select("p[class='productTitle']").select("a").attr("title"));
                        product.setPrice(item.select("p[class='productPrice']").select("em").attr("title"));
                        String url=item.select("p[class='productTitle']").select("a").attr("href");
                        product.setUrl(url.substring(0,url.indexOf("&")));
                        product.setWhere("天猫");
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
                    RecordSearch();
                    Refresh();

            } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread jdThread=new Thread(new Runnable() {
            @Override
            public void run() {
                Document homepage= null;
                try {
                    homepage = getDocument(jingdong_url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements Ulist=homepage.select("div[id='J_goodsList']");
                Elements Llist=Ulist.select("div[class='gl-i-wrap']");
                for(Element item:Llist){
                    Product product=new Product();
                    String itemurl="https:"+item.getElementsByClass("p-name p-name-type-2").select("a").attr("href");
                    String skuIds=itemurl.substring(itemurl.lastIndexOf('/')+1,itemurl.lastIndexOf('.'));
                    String skuUrl="https://p.3.cn/prices/mgets?skuIds="+skuIds;
                    Document itempage= null;
                    try {
                        itempage = getDocument(itemurl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                            设置名字
                    product.setName(itempage.getElementsByClass("sku-name").text());
//                            设置各种价格
                    JsonParser jsonParser=new JsonParser();
                    JsonArray jsonElements= null;
                    try {
                        jsonElements = jsonParser.parse(getDocument(skuUrl).body().text()).getAsJsonArray();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    product.setPrice(jsonElements.get(0).getAsJsonObject().get("p").getAsString());

                    product.setUrl(itemurl);
                    product.setWhere("京东");
                    product.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                        }
                    });
                }
                RecordSearch();
                Refresh();
            }
        });
        tmallThread.start();
        jdThread.start();
    }

    private Document getDocument(String url) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
                .build();
        Response response=client.newCall(request).execute();

        String html= Objects.requireNonNull(response.body()).string();
        return Jsoup.parse(html);
    }

    private void RecordSearch() {
        SearchRecord searchRecord=new SearchRecord();
        searchRecord.setUser(BmobUser.getCurrentUser(BmobUser.class));
        searchRecord.setKey(searchcontent.getText().toString());

        searchRecord.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
            }
        });

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
                BmobQuery<Product> productBmobQuery = new BmobQuery<>();
                switch (sort_position){
                    case 0:productBmobQuery.order("price");break;
                    case 1:productBmobQuery.order("createdAt");break;
                }
                productBmobQuery.setLimit(1000);
                productBmobQuery.addWhereEqualTo("where","天猫");
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
                break;
            }
            case 1:{
                BmobQuery<Product> productBmobQuery=new BmobQuery<>();
                switch (sort_position){
                    case 0:productBmobQuery.order("price");break;
                    case 1:productBmobQuery.order("sell");break;
                }
                productBmobQuery.setLimit(1000);
                productBmobQuery.addWhereEqualTo("where","京东");
                productBmobQuery.findObjects(new FindListener<Product>() {
                    @Override
                    public void done(List<Product> list, BmobException e) {
                        swipeRefreshLayout.setRefreshing(false);
                        if(e==null){
                            SearchAdapter searchAdapter=new SearchAdapter(getApplicationContext(),list);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(searchAdapter);
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

