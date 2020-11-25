package com.example.qxapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.R;

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
        View t_view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ord_item,null,false);
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
        ordViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        public OrdViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.user_ord_title);
        }
    }
}
