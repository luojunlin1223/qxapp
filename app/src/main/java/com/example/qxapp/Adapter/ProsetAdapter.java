package com.example.qxapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Proset;
import com.example.qxapp.activity.Bean.SearchRecord;
import com.example.qxapp.activity.ProsetReceive;
import com.example.qxapp.activity.Receive;

import java.util.List;

import io.reactivex.internal.operators.parallel.ParallelRunOn;

public class ProsetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    boolean isfootview = true;
    private Context context;
    private List<Proset> data;
    private final int N_TYPE=0;
    private final int F_TYPE=1;
    //  预加载的数据的条目
    private int Max_num=8;

    public ProsetAdapter(Context context, List<Proset> data){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.proset_item,parent,false);
        View footview=LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
        if(viewType==F_TYPE){
            return new ProsetAdapter.RecyclerViewHolder(footview,F_TYPE);
        }else{
            return new ProsetAdapter.RecyclerViewHolder(view,N_TYPE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //是否有footview

        if(isfootview &&getItemViewType(position)==F_TYPE){
            //底部加载
            Handler handler=new Handler();
            handler.postDelayed(() -> {
//              总条目自增8条
                Max_num+=8;
                notifyDataSetChanged();
            },2000);
        }else {
            //获取内容
            final ProsetAdapter.RecyclerViewHolder recyclerViewHolder= (ProsetAdapter.RecyclerViewHolder) holder;
            Proset proset=data.get(position);
            recyclerViewHolder.proset.setText(proset.getName());
            recyclerViewHolder.time.setText(proset.getCreatedAt());
//          用户点击特定的itemView的时候
            recyclerViewHolder.itemView.setOnClickListener(v -> {
//                        携带数据跳转接收的Activity
                Intent in=new Intent(context, ProsetReceive.class);
                in.putExtra("name",proset.getName());
                in.putExtra("price_low",proset.getPrice_low());
                in.putExtra("price_high",proset.getPrice_high());
                in.putExtra("id",proset.getObjectId());
                in.putExtra("brand",proset.getBrand());
                in.putExtra("where",proset.getWhere());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
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
        public TextView proset,time;
        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if(view_type==N_TYPE){
                proset=itemview.findViewById(R.id.proset_name);
                time=itemview.findViewById(R.id.time);
            }
        }
    }

}
