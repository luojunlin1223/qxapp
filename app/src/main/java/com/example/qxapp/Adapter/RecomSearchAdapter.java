package com.example.qxapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Recommondation;
import com.example.qxapp.activity.Receive;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RecomSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    boolean isfootview = true;
    private Context context;
    private List<Recommondation> data;
    private final int N_TYPE=0;
    private final int F_TYPE=1;
    //  预加载的数据的条目
    private int Max_num=15;

    public RecomSearchAdapter(Context context, List<Recommondation> data){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recom_search_item,parent,false);
        View footview=LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
        if(viewType==F_TYPE){
            return new RecomSearchAdapter.RecyclerViewHolder(footview,F_TYPE);
        }else{
            return new RecomSearchAdapter.RecyclerViewHolder(view,N_TYPE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //是否有footview

        if(isfootview &&getItemViewType(position)==F_TYPE){
            //底部加载
            final RecomSearchAdapter.RecyclerViewHolder recyclerViewHolder= (RecomSearchAdapter.RecyclerViewHolder) holder;
            Handler handler=new Handler();
            handler.postDelayed(() -> {
//              总条目自增8条
                Max_num+=8;
                notifyDataSetChanged();
            },1000);
        }else {
            //获取内容


            final RecomSearchAdapter.RecyclerViewHolder recyclerViewHolder= (RecomSearchAdapter.RecyclerViewHolder) holder;
            Recommondation recommondation=data.get(position);
            recyclerViewHolder.thumbsupbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thumbsup(recyclerViewHolder.time);
                }
            });
            recyclerViewHolder.thumbdownbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thumbdown(recyclerViewHolder.time);
                }
            });


            recyclerViewHolder.username.setText(BmobUser.getCurrentUser(BmobUser.class).getUsername());
            recyclerViewHolder.content.setText(recommondation.getContent());
            recyclerViewHolder.thumbdown.setText(String.valueOf(recommondation.getThumbsdown()));
            recyclerViewHolder.thumbsup.setText(String.valueOf(recommondation.getThumbsup()));
            recyclerViewHolder.prodcut.setText(recommondation.getProduct());
            recyclerViewHolder.time.setText(recommondation.getCreatedAt());
//          用户点击特定的itemView的时候
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(context, Receive.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }
            });
        }

    }

    private void thumbdown(TextView time) {
        BmobQuery<Recommondation> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("createdAt",time);
        bmobQuery.findObjects(new FindListener<Recommondation>() {
            @Override
            public void done(List<Recommondation> list, BmobException e) {
                if(e==null){
                    Recommondation recommondation=new Recommondation();
                    recommondation.setThumbsdown(list.get(0).getThumbsdown()+1);
                    recommondation.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e!=null){
                                Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void thumbsup(TextView time) {
        BmobQuery<Recommondation>bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("createdAt",time);
        bmobQuery.findObjects(new FindListener<Recommondation>() {
            @Override
            public void done(List<Recommondation> list, BmobException e) {
                if(e==null){
                    Recommondation recommondation=new Recommondation();
                    recommondation.setThumbsup(list.get(0).getThumbsup()+1);
                    recommondation.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e!=null){
                                Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });

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
        public TextView username,content,prodcut,thumbsup,thumbdown,time;
        public ImageButton thumbsupbtn,thumbdownbtn;
        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if(view_type==N_TYPE){
                username=itemview.findViewById(R.id.username);
                content=itemview.findViewById(R.id.content);
                prodcut=itemview.findViewById(R.id.product);
                thumbdown=itemview.findViewById(R.id.thumbsdown);
                thumbsup=itemview.findViewById(R.id.thumbsup);
                time=itemview.findViewById(R.id.time);

                thumbsupbtn=itemview.findViewById(R.id.thumbsup_btn);
                thumbdownbtn=itemview.findViewById(R.id.thumbsdown_btn);
            }
        }
    }
}
