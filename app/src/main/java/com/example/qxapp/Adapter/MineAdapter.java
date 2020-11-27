package com.example.qxapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.R;
import com.example.qxapp.activity.Collect;
import com.example.qxapp.activity.History;
import com.example.qxapp.activity.Recommondation;
import com.example.qxapp.activity.Update;

import java.util.List;

public class MineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final int T_TYPE=0;
    private List<String> data;

    public MineAdapter(Context context,List<String>data){
        this.context=context;
        this.data=data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View t_view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ord_item,parent,false);
        if(viewType==T_TYPE){
            return new MineAdapter.OrdViewHolder(t_view);
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MineAdapter.OrdViewHolder ordViewHolder= (OrdViewHolder) holder;
        ordViewHolder.title.setText(data.get(position));
        switch (position){
            case 0: ordViewHolder.icon.setImageResource(R.drawable.history);break;
            case 1: ordViewHolder.icon.setImageResource(R.drawable.collect);break;
            case 2: ordViewHolder.icon.setImageResource(R.drawable.recommendation);break;
            case 3: ordViewHolder.icon.setImageResource(R.drawable.update);break;
        }
        ordViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position)
                {
                    case 0: context.startActivity(new Intent(context, History.class));break;
                    case 1: context.startActivity(new Intent(context, Collect.class));break;
                    case 2: context.startActivity(new Intent(context, Recommondation.class));break;
                    case 3: context.startActivity(new Intent(context, Update.class));break;
                }
            }
        });
//        onclick
    }

    @Override
    public int getItemViewType(int position) {
        return T_TYPE;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class OrdViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;
        public OrdViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.user_ord_title);
            icon=itemView.findViewById(R.id.icon);
        }
    }
}
