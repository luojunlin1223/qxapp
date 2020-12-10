package com.example.qxapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.example.qxapp.Adapter.SearchAdapter;
import com.example.qxapp.Fragment.FragmentProset;
import com.example.qxapp.Preference.SeekBarPreference;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Product;
import com.example.qxapp.activity.Bean.Proset;
import com.example.qxapp.activity.Bean.SearchRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Search extends AppCompatActivity {
    private EditText searchcontent;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner sort_spinner;
    private MultiSpinnerSearch from_spinner;
    private Spinner method_spinner;
    private CheckBox checkBox;
    private TextView textView;
    Thread tmallThread=new Thread(() -> {
        try {
            final String url = "https://list.tmall.com/search_product.htm?q=" + searchcontent.getText().toString();
            final String cookie="cna=qMU/EQh0JGoCAW5QEUJ1/zZm; enc=DUb9Egln3%2Fi4NrDfzfMsGHcMim6HWdN%2Bb4ljtnJs6MOO3H3xZsVcAs0nFao0I2uau%2FbmB031ZJRvrul7DmICSw%3D%3D; lid=%E5%90%91%E6%97%A5%E8%91%B5%E7%9B%9B%E5%BC%80%E7%9A%84%E5%A4%8F%E5%A4%A9941020; otherx=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; hng=CN%7Czh-CN%7CCNY%7C156; x=__ll%3D-1%26_ato%3D0; t=2c579f9538646ca269e2128bced5672a; _m_h5_tk=86d64a702eea3035e5d5a6024012bd40_1551170172203; _m_h5_tk_enc=c10fd504aded0dc94f111b0e77781314; uc1=cookie16=V32FPkk%2FxXMk5UvIbNtImtMfJQ%3D%3D&cookie21=U%2BGCWk%2F7p4mBoUyS4E9C&cookie15=UtASsssmOIJ0bQ%3D%3D&existShop=false&pas=0&cookie14=UoTZ5bI3949Xhg%3D%3D&tag=8&lng=zh_CN; uc3=vt3=F8dByEzZ1MVSremcx%2BQ%3D&id2=UNcPuUTqrGd03w%3D%3D&nk2=F5RAQ19thpZO8A%3D%3D&lg2=U%2BGCWk%2F75gdr5Q%3D%3D; tracknick=tb51552614; _l_g_=Ug%3D%3D; ck1=\"\"; unb=3778730506; lgc=tb51552614; cookie1=UUBZRT7oNe6%2BVDtyYKPVM4xfPcfYgF87KLfWMNP70Sc%3D; login=true; cookie17=UNcPuUTqrGd03w%3D%3D; cookie2=1843a4afaaa91d93ab0ab37c3b769be9; _nk_=tb51552614; uss=\"\"; csg=b1ecc171; skt=503cb41f4134d19c; _tb_token_=e13935353f76e; x5sec=7b22726174656d616e616765723b32223a22393031623565643538663331616465613937336130636238633935313935363043493362302b4d46454e76646c7243692b34364c54426f4d4d7a63334f44637a4d4455774e6a7378227d; l=bBIHrB-nvFBuM0pFBOCNVQhjb_QOSIRYjuSJco3Wi_5Bp1T1Zv7OlzBs4e96Vj5R_xYB4KzBhYe9-etui; isg=BDY2WCV-dvURoAZdBw3uwj0Oh2yUQwE5YzQQ9qAfIpm149Z9COfKoZwV-_8q0HKp";
            Connection connection=Jsoup.connect(url);
            connection.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
            Document document=connection.get();
            Elements ulList = document.select("div[id='J_ItemList']");
            Elements liList = ulList.select("div[class='product']");
            for(Element item:liList){
                Product product=new Product();
                product.setName(item.select("p[class='productTitle']").select("a").attr("title"));
                product.setPrice(item.select("p[class='productPrice']").select("em").attr("title"));
                String itemurl_t=item.select("p[class='productTitle']").select("a").attr("href");
                String itemurl="http:"+itemurl_t.substring(0,itemurl_t.indexOf("&"));
                Document itempage=getDocument(itemurl,cookie);
                String infro=itempage.getElementsByClass("attributes-list").select("ul").text();
                String imageurl="http:"+itempage.select("img").attr("src");
                product.setInfro(infro);
                product.setImageurl(imageurl);
                product.setUrl(itemurl);
                product.setWhere("天猫");
                product.setKey(searchcontent.getText().toString());
                product.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e1) {
                    }
                });
            }
            Refresh(sort_spinner.getSelectedItemPosition(),from_spinner.getSelectedItems());

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    });
    Thread jdThread=new Thread(() -> {
        final String url="https://search.jd.com/Search?keyword="+searchcontent.getText().toString();
        final String cookie="__jdu=1167767595; shshshfpa=24237be2-a59a-4deb-9543-5e24ea6cb106-1603810131; shshshfpb=zj8oRlybwBNW%20IKkZMw1M8A%3D%3D; areaId=4; ipLoc-djd=4-48202-52490-0; PCSYCityID=CN_500000_500100_500113; unpl=V2_ZzNtbUdVRxImX05cfR1YAWJXE1xKBUUQIAFAA3sfVAQwVBVVclRCFnQURldnGFUUZgMZXUZcQRRFCEdkeB5fA2AFEFlBZxVLK14bADlNDEY1WnwHBAJfF3ILQFJ8HlQMZAEUbXJUQyV1CXZUex5aA2AFFlVGZ3MSRTh2UX0cVQ1nMxNtQ2cBQSkIRFB5EVhIZwMVW0RQRRF9DHZVSxo%3d; __jdv=76161171|google-search|t_262767352_googlesearch|cpc|kwd-362776698237_0_4356bf9975554e009c74d87f1790ff69|1607361329145; __jdc=122270672; shshshfp=831928a80ffefa25ecc504fb5b01217e; 3AB9D23F7A4B3C9B=AXGETRC72S2EHT5BCTKO367BNCNQEZ7MH6AP6VHBEWP5XAIRAG3WDDG5VNTOZYBW2OPJ5RN4HVGY7ECFMD6OFQPTOA; wlfstk_smdl=he5jymfj7zdqpl1r5twoej8fureoys2f; __jda=122270672.1167767595.1603810128.1607361329.1607427462.14; shshshsID=0b0d463cd600f8fd0b5bc1909eedcaaf_2_1607428108042; __jdb=122270672.2.1167767595|14.1607427462";
        Document homepage= null;
        try {
            homepage = getDocument(url,"");
        } catch (IOException e12) {
            e12.printStackTrace();
        }
        assert homepage != null;
        Elements Ulist=homepage.select("div[id='J_goodsList']");
        Elements Llist=Ulist.select("div[class='gl-i-wrap']");
        for(Element item:Llist){
            Product product=new Product();
            String itemurl="https:"+item.select("a").attr("href");
            String imageurl="http:"+item.select("img").attr("data-lazy-img");
            product.setImageurl(imageurl);
            Document itempage= null;
            try {
                itempage = getDocument(itemurl,cookie);
            } catch (IOException e12) {
                e12.printStackTrace();
            }
//                            设置名字
            assert itempage != null;
            String infro=itempage.getElementsByClass("p-parameter").text();
            product.setInfro(infro);
            product.setName(itempage.getElementsByClass("sku-name").text());
//                            设置各种价格
            product.setPrice(item.select("div[class='p-price']").select("strong").select("i").text());
            if(itemurl.isEmpty()){
                product.setUrl("");
            }else{
                product.setUrl(itemurl);
            }
            product.setWhere("京东");
            product.setKey(searchcontent.getText().toString());
            product.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e12) {
                }
            });
        }
        Refresh(sort_spinner.getSelectedItemPosition(),from_spinner.getSelectedItems());
    });
    Thread taobaoThread=new Thread(() -> {
        final String url="https://s.taobao.com/search?q="+searchcontent.getText().toString();
        final String cookie="miid=708870911453952331; thw=cn; cna=93/eF2JdvlECAWpbFuoXG4Rp; sgcookie=E1006hNQOIoQhTOVLK%2Fan8%2FZwwbSmv03%2FmZ%2BYuIyR1%2BdLDryzHyjKdvlX%2F2L1y7NJs0yf6Rbd7O3JFFZQRT35lAoyw%3D%3D; uc3=lg2=UIHiLt3xD8xYTw%3D%3D&id2=UNN5FQJoyAszDA%3D%3D&nk2=oeCW7FMf2j0HaQ%3D%3D&vt3=F8dCufJHCjVuRcdLH%2B8%3D; lgc=%5Cu7F57%5Cu5CFB%5Cu67971223; uc4=id4=0%40UgQxkvUgMLe5cZjCJX6%2BHNwGs5nf&nk4=0%40o6gv2hukrGR8Ww67aNWlX%2BAkTO9W; tracknick=%5Cu7F57%5Cu5CFB%5Cu67971223; _cc_=URm48syIZQ%3D%3D; enc=zaG6G5c%2F0438JPe5QFJevakLCfM68keAD%2BZ%2BAf5YSSclb1sricyHn8GTMn2duf2zBPfj49nxmbv6NlONegCfiA%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; mt=ci=-1_0; t=59fb6c7c5472f0aec896289669f7437a; cookie2=19347cd7c3d6aafa340e157193c6ab68; _tb_token_=3b5b31eb83de; _m_h5_tk=94f9165296a4a1e13e9c92d505588d5e_1605508996207; _m_h5_tk_enc=726963c634b2cbbd7ece8b08dea38fc2; v=0; xlly_s=1; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; _samesite_flag_=true; JSESSIONID=B71E78BC911B74F66AE948343D49AC03; uc1=cookie14=Uoe0aDmVbeFtAw%3D%3D; isg=BDU15FPjhkYtWuITVb05Od7eRLHvsunELRMaDLdaoaz7jlSAfgITlFlM2FK4zgF8; l=eBOjecFqOuqCJnTSBOfwourza77O8IRfguPzaNbMiOCP_Yf65n8cWZ7VJITBCnGVHsMpJ3-cUFnuBYTpJyCqJxpsw3k_J_DmndC..; tfstk=cIhVB7XpuId2S_P6pSNNhuLf8SzAa3qgA_zUnA8pmYT5j12aYs4VXzY5XzzavQ2c.";
        Document homepage=null;
        try {
            homepage=getDocument(url,cookie);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assert homepage != null;
        Elements elements=homepage.getElementsByTag("script").eq(7);
        String jsoncontent=null;
        for(Element element:elements){
            for(DataNode node:element.dataNodes()){
                jsoncontent=jsoncontent+node.getWholeData();
            }
        }
        assert jsoncontent != null;
        String head="g_page_config = ";
        jsoncontent=jsoncontent.substring(jsoncontent.indexOf(head)+head.length()).trim();
        List<String> goods= new ArrayList<>(Arrays.asList(jsoncontent.split("\"similar\":")));
        goods.remove(0);
        for(String item:goods){
            Product product=new Product();
            String target_title="\"raw_title\":\"";
            String target_price="\"view_price\":\"";
            String target_pic="\"pic_url\":\"";
            String target_innerText="\"innerText\":\"";
            String target_nid="\"nid\":\"";
            String innerText=taobao_helper(item,target_innerText);

            product.setName(taobao_helper(item,target_title));
            product.setPrice(taobao_helper(item,target_price));
            product.setImageurl("http:"+taobao_helper(item,target_pic));
            product.setKey(searchcontent.getText().toString());
            String itemurl=null;
            if(innerText.contains("天猫")){
                product.setWhere("天猫");
                itemurl="https://detail.tmall.com/item.htm?id="+taobao_helper(item,target_nid);
            }else{
                product.setWhere("淘宝");
                itemurl="https://item.taobao.com/item.htm?id="+taobao_helper(item,target_nid);
            }
            try {
                Document itempage=getDocument(itemurl,cookie);
                String infro=itempage.getElementsByClass("attributes-list").text();
                if(infro.isEmpty()){
                    product.setInfro("没有信息");
                }else{
                    product.setInfro(infro);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.setUrl(itemurl);
            product.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                }
            });
        }
        Refresh(sort_spinner.getSelectedItemPosition(),from_spinner.getSelectedItems());
    });
    Thread SuningThread=new Thread(() -> {
        String url="https://search.suning.com/"+searchcontent.getText().toString()+"/";
        try {
            Document homepage=getDocument(url,"");
            Elements goodlist=homepage.select("ul[class='general clearfix']").select("li");
            for(Element item:goodlist){
                String itemurl= "http:" + item.select("div[class='title-selling-point']").select("a").attr("href");
                String imageurl="http:"+item.select("img").attr("src");
                Document itempage=getDocument(itemurl,"");
                String name=itempage.select("h1[id='itemDisplayName']").text();
                String infro=itempage.select("div[id='J-procon-desc']").text();
                List<String> id=new ArrayList<>(Arrays.asList(itemurl.split("com/")));
                id.remove(0);
                String shopid=id.get(0).substring(0,id.get(0).indexOf("/"));
                String prdid=id.get(0).substring(id.get(0).indexOf("/")+1,id.get(0).indexOf("."));

                String priceurl="http://pas.suning.com/nspcsale_0_000000000" +
                        prdid + "_000000000" + prdid + "_" + shopid + "_20_021_0210101_500353_1000267_9264_12113_Z001___R9006849_3.3_1___000278188__.html?callback=pcData&_=1558663936729";
                if(prdid.length()==11){
                    priceurl = "http://pas.suning.com/nspcsale_0_0000000" +
                            prdid + "_0000000" + prdid + "_" + shopid + "_20_021_0210101_500353_1000267_9264_12113_Z001___R9006849_3.3_1___000278188__.html?callback=pcData&_=1558663936729";
                }
                Document pricepage=getDocument(priceurl,"");
                String pricejson=pricepage.toString();
                String tag="\"netPrice\":\"";
                String price=pricejson.substring(pricejson.indexOf(tag)
                        +tag.length(),pricejson.indexOf(",",pricejson.indexOf(tag))-1);
                Product product=new Product();
                product.setInfro(infro);
                product.setImageurl(imageurl);
                product.setName(name);
                product.setPrice(price);
                product.setUrl(itemurl);
                product.setWhere("苏宁");
                product.setKey(searchcontent.getText().toString());
                product.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                    }
                });
            }
            Refresh(sort_spinner.getSelectedItemPosition(),from_spinner.getSelectedItems());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    });


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

//      判断是否有过搜索记录 如果有则记录搜索 刷新；如果没有则记录搜索 启动搜索线程
        BmobQuery<SearchRecord> bmobQuery=new BmobQuery<>();
        if(searchcontent.getText().toString().isEmpty()){
            Toast.makeText(Search.this,"您输入的商品为空！",Toast.LENGTH_LONG).show();
        }else {
            bmobQuery.addWhereEqualTo("key",searchcontent.getText().toString());
            bmobQuery.findObjects(new FindListener<SearchRecord>() {
                @Override
                public void done(List<SearchRecord> list, BmobException e) {
                    if(e==null){
                        if(list.size()==0){
                            RecordSearch();
                            tmallThread.start();
                            jdThread.start();
                            taobaoThread.start();
                            SuningThread.start();
                        }else{
                            RecordSearch();
                            Refresh(sort_spinner.getSelectedItemPosition(),from_spinner.getSelectedItems());
                        }
                    }
                }
            });
        }
    }

    private Document getDocument(String url,String cookie) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
                .addHeader("Cookie",cookie)
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .build();
        Response response=client
                .newCall(request)
                .execute();

        String html= Objects.requireNonNull(response.body()).string();
        return Jsoup.parse(html);
    }

    private void RecordSearch() {
//      记录搜索内容 匹配关键字和用户 如果有相关记录自增搜索数 如果没有则置为1
        BmobQuery<SearchRecord> bmobQuery1=new BmobQuery<>();
        bmobQuery1.addWhereEqualTo("key",searchcontent.getText().toString());
        BmobQuery<SearchRecord> bmobQuery2=new BmobQuery<>();
        bmobQuery2.addWhereEqualTo("user",BmobUser.getCurrentUser(BmobUser.class));
        List<BmobQuery<SearchRecord>>bmobQueries=new ArrayList<>();
        bmobQueries.add(bmobQuery1);
        bmobQueries.add(bmobQuery2);
        BmobQuery<SearchRecord> bmobQuery=new BmobQuery<>();
        bmobQuery.and(bmobQueries);
        bmobQuery.findObjects(new FindListener<SearchRecord>() {
            @Override
            public void done(List<SearchRecord> list, BmobException e) {
                SearchRecord searchRecord=new SearchRecord();
                if(e==null) {
                    searchRecord.setCount(list.get(0).getCount()+1);
                    searchRecord.update(list.get(0).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                        }
                    });
                }else{
                    searchRecord.setUser(BmobUser.getCurrentUser(BmobUser.class));
                    searchRecord.setKey(searchcontent.getText().toString());
                    searchRecord.setCount(1);
                    searchRecord.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                        }
                    });
                }
            }
        });
    }

    private void initControl() {
        searchcontent=findViewById(R.id.search_content);
        searchcontent.setSingleLine();
        searchcontent.setLines(1);
        searchcontent.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchcontent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_SEARCH){
                    search();
                }
                return false;
            }
        });

        Button cancelbtn = findViewById(R.id.cancel);

        cancelbtn.setOnClickListener(v -> finish());

        recyclerView=findViewById(R.id.search_recyclerview);
        swipeRefreshLayout=findViewById(R.id.search_swipe);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_red_light,android.R.color.holo_blue_light);
//        上拉刷新
//        swipeRefreshLayout.setOnRefreshListener(this::Refresh);
////      中止上拉刷新
        swipeRefreshLayout.setEnabled(false);
        loadSort();
        loadfrom();

        method_spinner=findViewById(R.id.method_spinner);

        checkBox=findViewById(R.id.check);
        ArrayAdapter<String> method_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
        method_adapter.add("不可用");
        method_spinner.setAdapter(method_adapter);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ArrayAdapter<String> method_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
                    BmobQuery<Proset>query=new BmobQuery<>();
                    query.addWhereEqualTo("user",BmobUser.getCurrentUser(BmobUser.class));
                    query.findObjects(new FindListener<Proset>() {
                        @Override
                        public void done(List<Proset> list, BmobException e) {
                            if(e==null){
                                for(Proset item:list){
                                    method_adapter.add(item.getName());
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    method_spinner.setAdapter(method_adapter);

                    ArrayAdapter<String> sort_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
                    List<KeyPairBoolData> from_listArray=new ArrayList<>();
                    sort_adapter.add("不可用");
                    sort_spinner.setAdapter(sort_adapter);

                    from_listArray.clear();
                    from_spinner.setHintText("不可用");
                    from_spinner.setItems(from_listArray,selectedItems -> {
//                复选框所选择的所有的items
                    });
                    Refresh();
                }else{
                    ArrayAdapter<String> method_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
                    method_adapter.add("不可用");
                    method_spinner.setAdapter(method_adapter);
                    loadSort();
                    loadfrom();
                }
            }
        });
        textView=findViewById(R.id.test);


    }

    private void loadfrom() {
        from_spinner=findViewById(R.id.from_spinner);
        final List<String> from_list= Arrays.asList(getResources().getStringArray(R.array.from));
        final List<KeyPairBoolData> from_listArray=new ArrayList<>();
            for(int i = 0; i < from_list.size(); i++){
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(from_list.get(i));
            h.setSelected(true);
            from_listArray.add(h);
        }
        from_spinner.setShowSelectAllButton(false);
        from_spinner.setSearchEnabled(true);
        from_spinner.setHintText("选择信息来源");
        from_spinner.setItems(from_listArray, selectedItems -> {
//                复选框所选择的所有的items
            for (int i = 0; i < selectedItems.size(); i++) {
                Refresh(sort_spinner.getSelectedItemPosition(),selectedItems);
            }
        });
    }

    private void loadSort() {
        ArrayAdapter<String> sort_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item);
        final List<String> sort_list= Arrays.asList(getResources().getStringArray(R.array.sort));
        for(String item:sort_list){
            sort_adapter.add(item);
        }
        sort_spinner=findViewById(R.id.sort_spinner);
        sort_spinner.setAdapter(sort_adapter);
        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Refresh(sort_spinner.getSelectedItemPosition(),from_spinner.getSelectedItems());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Refresh(int sort_positon,List<KeyPairBoolData> from_positon) {
        BmobQuery<Product> productBmobQuery = new BmobQuery<>();
        List<BmobQuery<Product>> bmobQueries=new ArrayList<>();
        productBmobQuery.addWhereEqualTo("key",searchcontent.getText().toString());

        switch (sort_positon){
            case 0:productBmobQuery.order("price");break;
            case 1:productBmobQuery.order("createdAt");break;
        }

        for(int i=0;i<from_positon.size();i++){
            BmobQuery<Product> productBmobQueryor=new BmobQuery<>();
            productBmobQueryor.addWhereEqualTo("where",from_positon.get(i).getName());
            bmobQueries.add(productBmobQueryor);
        }
        productBmobQuery.or(bmobQueries);
        productBmobQuery.findObjects(new FindListener<Product>() {
            @Override
            public void done(List<Product> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    SearchAdapter searchAdapter=new SearchAdapter(getApplicationContext(), list);
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                    recyclerView.setAdapter(searchAdapter);
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
    private void Refresh(){
    }
    private String taobao_helper(String item,String target){
        return item.substring(item.indexOf(target)+target.length(),
                item.indexOf(",",item.indexOf(target)+target.length())-1);
    }

}

