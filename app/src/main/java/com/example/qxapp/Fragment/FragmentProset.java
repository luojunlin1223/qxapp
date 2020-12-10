package com.example.qxapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.print.PageRange;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;

import com.example.qxapp.Preference.SeekBarPreference;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Proset;
import com.example.qxapp.activity.Search;

import java.util.List;
import java.util.Objects;
import java.util.prefs.PreferenceChangeEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class FragmentProset extends Fragment {
    private TextView name;
    private TextView price_low;
    private TextView price_high;
    private TextView price_percentage;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentproset,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        price_low= getView().findViewById(R.id.price_low);
        price_high= getView().findViewById(R.id.price_high);
        price_percentage= getView().findViewById(R.id.percentage);
        name=getView().findViewById(R.id.name);
        initData();
    }

    private void initData() {
        Bundle bundle = getArguments();
        name.setText(bundle.getString("name"));
        price_percentage.setText(String.valueOf(bundle.getInt("percentage")));
        price_low.setText(String.valueOf(bundle.getInt("price_low")));
        price_high.setText(String.valueOf(bundle.getInt("price_high")));
    }
}
