package com.example.qxapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Community;
import com.example.qxapp.activity.Receive;

import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    boolean isfootview = true;
    private Context context;
    private List<Community> data;
    private final int N_TYPE=0;
    private final int F_TYPE=1;
    //  预加载的数据的条目
    private int Max_num=8;

    public ChannelAdapter(Context context, List<Community> data){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item,parent,false);
        View footview=LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
        if(viewType==F_TYPE){
            return new ChannelAdapter.RecyclerViewHolder(footview,F_TYPE);
        }else{
            return new ChannelAdapter.RecyclerViewHolder(view,N_TYPE);
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
            final ChannelAdapter.RecyclerViewHolder recyclerViewHolder= (ChannelAdapter.RecyclerViewHolder) holder;
            Community community=data.get(position);
            recyclerViewHolder.name.setText(community.getName());
            recyclerViewHolder.info.setText(community.getInfo());
            recyclerViewHolder.time.setText(community.getCreatedAt());
//          用户点击特定的itemView的时候
            recyclerViewHolder.itemView.setOnClickListener(v -> {
                int position1 =recyclerViewHolder.getAdapterPosition();
//                        携带数据跳转接收的Activity
                Intent in=new Intent(context, Receive.class);
//                        为了让跳转的时候不出现更新问题直接携带数据跳转
//                        in.putExtra("username",post.getName());
//                        in.putExtra("content",post.getContent());
//                        in.putExtra("time",post.getCreatedAt());
                in.putExtra("id",data.get(position1).getObjectId());
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
        public TextView name,info,time;
        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if(view_type==N_TYPE){
                name=itemview.findViewById(R.id.community_name);
                info=itemview.findViewById(R.id.community_info);
                time=itemview.findViewById(R.id.community_time);
            }
        }
    }
}
