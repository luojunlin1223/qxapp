package com.example.qxapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.ImageView;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Product;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    boolean isfootview = true;
    private Context context;
    private List<Product> data;
    private final int N_TYPE=0;
    private final int F_TYPE=1;
    //  预加载的数据的条目
    private int Max_num=15;

    public SearchAdapter(Context context, List<Product> data){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        View footview=LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
        if(viewType==F_TYPE){
            return new SearchAdapter.RecyclerViewHolder(footview,F_TYPE);
        }else{
            return new SearchAdapter.RecyclerViewHolder(view,N_TYPE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //是否有footview

        if(isfootview &&getItemViewType(position)==F_TYPE){
            //底部加载
            final SearchAdapter.RecyclerViewHolder recyclerViewHolder= (SearchAdapter.RecyclerViewHolder) holder;
            Handler handler=new Handler();
            handler.postDelayed(() -> {
//              总条目自增8条
                Max_num+=8;
                notifyDataSetChanged();
            },1000);
        }else {
            //获取内容
            final SearchAdapter.RecyclerViewHolder recyclerViewHolder= (SearchAdapter.RecyclerViewHolder) holder;
            Product product=data.get(position);
            recyclerViewHolder.name.setText(product.getName());
            recyclerViewHolder.price.setText(String.valueOf(product.getPrice()));
            recyclerViewHolder.url.setText(product.getUrl());
            recyclerViewHolder.where.setText(product.getWhere());
            recyclerViewHolder.brand.setText(product.getInfro());
            recyclerViewHolder.imageView.setImageURL(product.getImageurl());

//          用户点击特定的itemView的时候
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path;
                    switch (product.getWhere()){
                        case "淘宝":{
                            if(checkPackage("com.taobao.taobao")){
                                String nid;
                                nid=product.getUrl();
                                nid=nid.substring(nid.indexOf("id"));
                                path="taobao://item.taobao.com/item.html?"+nid;
                                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(path));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }else {
                                Toast.makeText(context,"请安装淘宝！",Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case "天猫":{
                            if(checkPackage("com.taobao.taobao")){
                                String nid;
                                nid=product.getUrl();
                                nid=nid.substring(nid.indexOf("id"));
                                path="taobao://item.taobao.com/item.html?"+nid;
                                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(path));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }else {
                                Toast.makeText(context,"请安装天猫！",Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case "京东":{
                            if(checkPackage("com.jingdong.app.mall")){
                                String nid;
                                nid=product.getUrl();
                                nid=nid.substring(nid.indexOf("com/")+4,nid.indexOf(".html"));
                                path="openApp.jdMobile://virtual?params={\"category\":\"jump\",\"des\":\"productDetail\",\"skuId\":\""+nid+"\",\"sourceType\":\"JSHOP_SOURCE_TYPE\",\"sourceValue\":\"JSHOP_SOURCE_VALUE\"}";
                                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(path));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }else{
                                Toast.makeText(context,"请安装京东！",Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case "苏宁":{
                            if(checkPackage(" com.suning.mobile.ebuy")){
                                String nid;
                                nid=product.getUrl();
                                nid=nid.substring(nid.indexOf("com/")+4,nid.indexOf(".html"));
                                PackageManager packageManager=context.getPackageManager();
                                String packagename="com.suning.mobile.ebuy";
                                Intent intent=packageManager.getLaunchIntentForPackage(packagename);
                                context.startActivity(intent);
                            }else{
                                Toast.makeText(context,"请安装苏宁！",Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        default:
                            throw new IllegalStateException("Unexpected value: " + product.getWhere());
                    }

                }
            });
        }

    }

    @Override
//  设置ItemView的返回类型
    public int getItemViewType(int position) {
        if(position==Max_num-1){
//            底部type
            return F_TYPE;
        }else{
            return N_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(data.size(), Max_num);
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView name,price,url,where,brand;
        public ImageView imageView;
        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if(view_type==N_TYPE){
                name=itemview.findViewById(R.id.product_item_name);
                price=itemview.findViewById(R.id.product_item_price);
                url=itemview.findViewById(R.id.product_item_url);
                imageView=itemview.findViewById(R.id.product_image);
                where=itemview.findViewById(R.id.product_item_where);
                brand=itemview.findViewById(R.id.product_item_brand);
            }
        }
    }

    public boolean checkPackage(String packageName)
    {
        if (packageName == null || "".equals(packageName))
            return false;
        try
        {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager
                    .GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
