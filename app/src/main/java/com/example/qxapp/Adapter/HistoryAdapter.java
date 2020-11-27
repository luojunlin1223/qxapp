package com.example.qxapp.Adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qxapp.activity.Bean.SearchRecord;

import java.util.List;

public class HistoryAdapter extends InnerAdapter{

    public HistoryAdapter(Context context, List<SearchRecord> data) {
        super(context, data);
    }

}
