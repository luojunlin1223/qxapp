package com.example.qxapp.Adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.ImageView;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Product;
import com.example.qxapp.activity.Receive;

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
            recyclerViewHolder.imageView.setImageURL(product.getImageurl());

//          用户点击特定的itemView的时候
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path;
                    
                    switch (product.getWhere()){
                        case "淘宝":{
                            String nid;
                            nid=product.getUrl();
                            nid=nid.substring(nid.indexOf("id"));
                            path="taobao://item.taobao.com/item.html?"+nid;
                            break;
                        }
                        default:
                            throw new IllegalStateException("Unexpected value: " + product.getWhere());
                    }
                    String pkg,cls;
                    pkg="com.tencent.mobileqq";
                    cls="com.tencent.mobileqq.activity.SplashActivity";
                    ComponentName componet = new ComponentName(pkg, cls);
                    //pkg 就是第三方应用的包名
                    //cls 就是第三方应用的进入的第一个Activity
                    Intent intent = new Intent();
                    intent.setComponent(componet);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

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
        public TextView name,price,url;
        public ImageView imageView;
        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if(view_type==N_TYPE){
                name=itemview.findViewById(R.id.product_item_name);
                price=itemview.findViewById(R.id.product_item_price);
                url=itemview.findViewById(R.id.product_item_url);
                imageView=itemview.findViewById(R.id.product_image);
            }
        }
    }
}
