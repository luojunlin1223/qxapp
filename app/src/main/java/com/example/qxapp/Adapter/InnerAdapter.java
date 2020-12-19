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
import com.example.qxapp.activity.Bean.SearchRecord;

import java.util.List;

public class InnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    boolean isfootview = true;
    private Context context;
    private List<SearchRecord> data;
    private final int N_TYPE=0;
    private final int F_TYPE=1;
    //  预加载的数据的条目
    private int Max_num=8;

    public InnerAdapter(Context context, List<SearchRecord> data){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_item,parent,false);
        View footview=LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
        if(viewType==F_TYPE){
            return new InnerAdapter.RecyclerViewHolder(footview,F_TYPE);
        }else{
            return new InnerAdapter.RecyclerViewHolder(view,N_TYPE);
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
            final InnerAdapter.RecyclerViewHolder recyclerViewHolder= (InnerAdapter.RecyclerViewHolder) holder;
            SearchRecord searchRecord=data.get(position);
            recyclerViewHolder.rank.setText(String.valueOf(position+1));
            recyclerViewHolder.key.setText(searchRecord.getKey());
            recyclerViewHolder.count.setText(String.valueOf(searchRecord.getCount()));
//          用户点击特定的itemView的时候
            recyclerViewHolder.itemView.setOnClickListener(v -> {
//
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
        public TextView rank,key,count;
        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if(view_type==N_TYPE){
                rank=itemview.findViewById(R.id.rank);
                key=itemview.findViewById(R.id.key);
                count=itemview.findViewById(R.id.count);
            }
        }
    }
}
