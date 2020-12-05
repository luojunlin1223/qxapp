package com.example.qxapp.Adapter;

import android.annotation.SuppressLint;
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

import com.example.qxapp.Fragment.FragmentRecom;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.CollectRecord;
import com.example.qxapp.activity.Bean.ThumbsupdownRecord;
import com.example.qxapp.activity.Bean.Recommondation;
import com.example.qxapp.activity.Receive;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.internal.platform.ConscryptPlatform;

public class RecomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    boolean isfootview = true;
    private Context context;
    private List<Recommondation> data;
    private final int N_TYPE=0;
    private final int F_TYPE=1;
    //  预加载的数据的条目
    private int Max_num=8;
    private int Max_thumbsup=5;
    private int Max_thumbsdown=5;
    public RecomAdapter(Context context, List<Recommondation> data){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recom_item,parent,false);
        View footview=LayoutInflater.from(parent.getContext()).inflate(R.layout.foot_item,parent,false);
        if(viewType==F_TYPE){
            return new RecomAdapter.RecyclerViewHolder(footview,F_TYPE);
        }else{
            return new RecomAdapter.RecyclerViewHolder(view,N_TYPE);
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

            final RecomAdapter.RecyclerViewHolder recyclerViewHolder= (RecomAdapter.RecyclerViewHolder) holder;
            Recommondation recommondation=data.get(position);

            recyclerViewHolder.thumbsupbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BmobQuery<Recommondation>query=new BmobQuery<>();
                    query.getObject(recommondation.getObjectId(), new QueryListener<Recommondation>() {
                        @Override
                        public void done(Recommondation recommondation, BmobException e) {
                            if(e==null){
                                BmobQuery<ThumbsupdownRecord>recordBmobQuery=new BmobQuery<>();
                                BmobQuery<ThumbsupdownRecord>recordBmobQuery1=new BmobQuery<>();
                                BmobQuery<ThumbsupdownRecord>recordBmobQuery2=new BmobQuery<>();
                                recordBmobQuery1.addWhereEqualTo("user",BmobUser.getCurrentUser(BmobUser.class));
                                recordBmobQuery2.addWhereEqualTo("recommondation",recommondation);
                                List<BmobQuery<ThumbsupdownRecord>> queries=new ArrayList<>();
                                queries.add(recordBmobQuery1);
                                queries.add(recordBmobQuery2);
                                recordBmobQuery.and(queries);
                                recordBmobQuery.findObjects(new FindListener<ThumbsupdownRecord>() {
                                    @Override
                                    public void done(List<ThumbsupdownRecord> list, BmobException e) {
                                        if(e==null){
                                            if(list.size()==0){
                                                ThumbsupdownRecord thumbsupdownRecord=new ThumbsupdownRecord();
                                                thumbsupdownRecord.setUser(BmobUser.getCurrentUser(BmobUser.class));
                                                thumbsupdownRecord.setRecommondation(recommondation);
                                                thumbsupdownRecord.setTumbsdown_count(0);
                                                thumbsupdownRecord.setTumbsup_count(1);
                                                thumbsupdownRecord.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if(e==null){
                                                            data.get(position).setThumbsup(recommondation.getThumbsup()+1);
                                                            notifyItemChanged(position);
                                                        }else {
                                                            Toast.makeText(context,"记录表保存失败"+e.toString(),Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else{
                                                if(list.get(0).getTumbsup_count()>Max_thumbsup){
                                                    Toast.makeText(context,"赞次数超过5次！",Toast.LENGTH_LONG).show();
                                                }else{
                                                    recommondation.setThumbsup(recommondation.getThumbsup()+1);
                                                    recommondation.update(recommondation.getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if(e!=null)
                                                            {
                                                                Toast.makeText(context,"踩失败"+e.toString(),Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                    list.get(0).setTumbsup_count(list.get(0).getTumbsup_count()+1);
                                                    list.get(0).update(list.get(0).getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if(e==null){
                                                                data.get(position).setThumbsup(recommondation.getThumbsup());
                                                                notifyItemChanged(position);
                                                            }
                                                        }
                                                    });

                                                }
                                            }
                                        }else {
                                            Toast.makeText(context,"记录表查询错误"+e.toString(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            });
            recyclerViewHolder.thumbdownbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BmobQuery<Recommondation>query=new BmobQuery<>();
                    query.getObject(recommondation.getObjectId(), new QueryListener<Recommondation>() {
                        @Override
                        public void done(Recommondation recommondation, BmobException e) {
                            if(e==null){
                                BmobQuery<ThumbsupdownRecord>recordBmobQuery=new BmobQuery<>();
                                BmobQuery<ThumbsupdownRecord>recordBmobQuery1=new BmobQuery<>();
                                BmobQuery<ThumbsupdownRecord>recordBmobQuery2=new BmobQuery<>();
                                recordBmobQuery1.addWhereEqualTo("user",BmobUser.getCurrentUser(BmobUser.class));
                                recordBmobQuery2.addWhereEqualTo("recommondation",recommondation);
                                List<BmobQuery<ThumbsupdownRecord>> queries=new ArrayList<>();
                                queries.add(recordBmobQuery1);
                                queries.add(recordBmobQuery2);
                                recordBmobQuery.and(queries);
                                recordBmobQuery.findObjects(new FindListener<ThumbsupdownRecord>() {
                                    @Override
                                    public void done(List<ThumbsupdownRecord> list, BmobException e) {
                                        if(e==null){
                                            if(list.size()==0){
                                                ThumbsupdownRecord thumbsupdownRecord=new ThumbsupdownRecord();
                                                thumbsupdownRecord.setUser(BmobUser.getCurrentUser(BmobUser.class));
                                                thumbsupdownRecord.setRecommondation(recommondation);
                                                thumbsupdownRecord.setTumbsdown_count(1);
                                                thumbsupdownRecord.setTumbsup_count(0);
                                                thumbsupdownRecord.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if(e==null){
                                                            Toast.makeText(context,"记录表保存成功",Toast.LENGTH_LONG).show();
                                                            data.get(position).setThumbsdown(recommondation.getThumbsdown()+1);
                                                            notifyItemChanged(position);
                                                        }else {
                                                            Toast.makeText(context,"记录表保存失败"+e.toString(),Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else{
                                                if(list.get(0).getTumbsdown_count()>Max_thumbsdown){
                                                    Toast.makeText(context,"踩数超过5次",Toast.LENGTH_LONG).show();
                                                }else{
                                                    recommondation.setThumbsdown(recommondation.getThumbsdown()+1);
                                                    recommondation.update(recommondation.getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if(e!=null)
                                                            {
                                                                Toast.makeText(context,"踩失败"+e.toString(),Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                    list.get(0).setTumbsdown_count(list.get(0).getTumbsdown_count()+1);
                                                    list.get(0).update(list.get(0).getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if(e==null){
                                                                data.get(position).setThumbsdown(recommondation.getThumbsdown());
                                                                notifyItemChanged(position);
                                                            }
                                                        }
                                                    });

                                                }
                                            }
                                        }else {
                                            Toast.makeText(context,"记录表查询错误"+e.toString(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            });
            recyclerViewHolder.collectbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BmobQuery<Recommondation>query=new BmobQuery<>();
                    query.getObject(recommondation.getObjectId(), new QueryListener<Recommondation>() {
                        @Override
                        public void done(Recommondation recommondation, BmobException e) {
                            if(e==null){
                                BmobQuery<CollectRecord>recordBmobQuery=new BmobQuery<>();
                                BmobQuery<CollectRecord>recordBmobQuery1=new BmobQuery<>();
                                BmobQuery<CollectRecord>recordBmobQuery2=new BmobQuery<>();
                                recordBmobQuery1.addWhereEqualTo("user",BmobUser.getCurrentUser(BmobUser.class));
                                recordBmobQuery2.addWhereEqualTo("recommondation",recommondation);
                                List<BmobQuery<CollectRecord>> queries=new ArrayList<>();
                                queries.add(recordBmobQuery1);
                                queries.add(recordBmobQuery2);
                                recordBmobQuery.and(queries);
                                recordBmobQuery.findObjects(new FindListener<CollectRecord>() {
                                    @Override
                                    public void done(List<CollectRecord> list, BmobException e) {
                                        if(e==null){
                                            if(list.size()==0){
                                                CollectRecord collectRecord=new CollectRecord();
                                                collectRecord.setUser(BmobUser.getCurrentUser(BmobUser.class));
                                                collectRecord.setRecommondation(recommondation);
                                                collectRecord.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if(e==null){
                                                            data.get(position).setCollect(recommondation.getCollect()+1);
                                                            notifyItemChanged(position);
                                                        }else {
                                                            Toast.makeText(context,"记录表保存失败"+e.toString(),Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else{
                                                Toast.makeText(context,"已经收藏过了！",Toast.LENGTH_LONG).show();
                                            }
                                        }else {
                                            Toast.makeText(context,"记录表查询错误"+e.toString(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            });

            recyclerViewHolder.username.setText(BmobUser.getCurrentUser(BmobUser.class).getUsername());
            recyclerViewHolder.content.setText(recommondation.getContent());
            recyclerViewHolder.thumbdown.setText(String.valueOf(recommondation.getThumbsdown()));
            recyclerViewHolder.thumbsup.setText(String.valueOf(recommondation.getThumbsup()));
            recyclerViewHolder.prodcut.setText(recommondation.getProduct());
            recyclerViewHolder.time.setText(recommondation.getCreatedAt());
            recyclerViewHolder.collect.setText(String.valueOf(recommondation.getCollect()));
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
        public TextView username,content,prodcut,thumbsup,thumbdown,time,collect;
        public ImageButton thumbsupbtn,thumbdownbtn,collectbtn;
        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if(view_type==N_TYPE){
                username=itemview.findViewById(R.id.username);
                content=itemview.findViewById(R.id.content);
                prodcut=itemview.findViewById(R.id.product);
                thumbdown=itemview.findViewById(R.id.thumbsdown);
                thumbsup=itemview.findViewById(R.id.thumbsup);
                time=itemview.findViewById(R.id.time);
                collect=itemview.findViewById(R.id.collect);

                thumbsupbtn=itemview.findViewById(R.id.thumbsup_btn);
                thumbdownbtn=itemview.findViewById(R.id.thumbsdown_btn);
                collectbtn=itemview.findViewById(R.id.collect_btn);
            }
        }
    }

}
