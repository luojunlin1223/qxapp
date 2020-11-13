package com.example.qxapp.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.jd_Product;
import com.example.qxapp.activity.Bean.tmall_Product;

import java.util.List;

public class jd_SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    boolean isfootview = true;
    private Context context;
    private List<jd_Product> data;
    private final int N_TYPE=0;
    private final int F_TYPE=1;
    //  预加载的数据的条目
    private int Max_num=15;

    public jd_SearchAdapter(Context context, List<jd_Product> data){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        View footview=LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
        if(viewType==F_TYPE){
            return new jd_SearchAdapter.RecyclerViewHolder(footview,F_TYPE);
        }else{
            return new jd_SearchAdapter.RecyclerViewHolder(view,N_TYPE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //是否有footview

        if(isfootview &&getItemViewType(position)==F_TYPE){
            //底部加载
            final jd_SearchAdapter.RecyclerViewHolder recyclerViewHolder= (jd_SearchAdapter.RecyclerViewHolder) holder;
            Handler handler=new Handler();
            handler.postDelayed(() -> {
//              总条目自增8条
                Max_num+=8;
                notifyDataSetChanged();
            },2000);
        }else {
            //获取内容
            final jd_SearchAdapter.RecyclerViewHolder recyclerViewHolder= (jd_SearchAdapter.RecyclerViewHolder) holder;
            jd_Product product=data.get(position);
            recyclerViewHolder.name.setText(product.getName());
            recyclerViewHolder.price.setText(String.valueOf(product.getPrice()));
            recyclerViewHolder.sell.setText(String.valueOf(product.getSell()));
            recyclerViewHolder.url.setText(product.getUrl());
//          用户点击特定的itemView的时候
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
        public TextView name,price,sell,url;
        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if(view_type==N_TYPE){
                name=itemview.findViewById(R.id.product_item_name);
                price=itemview.findViewById(R.id.product_item_price);
                sell=itemview.findViewById(R.id.product_item_sell);
                url=itemview.findViewById(R.id.product_item_url);
            }
        }
    }
}
