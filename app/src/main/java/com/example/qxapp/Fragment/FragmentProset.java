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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;

import com.example.qxapp.Preference.SeekBarPreference;
import com.example.qxapp.R;
import com.example.qxapp.activity.AlterProset.AlterPricePercentage;
import com.example.qxapp.activity.AlterProset.AlterPriceRange;
import com.example.qxapp.activity.AlterProset.Altername;
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
    private ImageButton namebtn,rangebtn,percentagebtn;
    private String id;

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
        namebtn=getView().findViewById(R.id.namebtn);
        rangebtn=getView().findViewById(R.id.rangebtn);
        percentagebtn=getView().findViewById(R.id.percentagebtn);
        initData();
    }

    private void initData() {
        Bundle bundle = getArguments();
        name.setText(bundle.getString("name"));
        price_percentage.setText(String.valueOf(bundle.getInt("percentage")));
        price_low.setText(String.valueOf(bundle.getInt("price_low")));
        price_high.setText(String.valueOf(bundle.getInt("price_high")));
        id=bundle.getString("id");
        namebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Altername.class);
                intent.putExtra("name",name.getText());
                getActivity().startActivityForResult(intent,1001);
            }
        });
        rangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AlterPriceRange.class);
                intent.putExtra("price_low",price_low.getText());
                intent.putExtra("price_high",price_high.getText());
                getActivity().startActivityForResult(intent,1002);
            }
        });
        percentagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AlterPricePercentage.class);
                intent.putExtra("percentage",price_percentage.getText());
                getActivity().startActivityForResult(intent,1003);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001&&resultCode==1){
            name.setText(data.getStringExtra("name"));
        }else if(requestCode==1002&&resultCode==1){
            price_low.setText(data.getStringExtra("price_low"));
            price_high.setText(data.getStringExtra("price_high"));
        }else if(requestCode==1003&&resultCode==1){
            price_percentage.setText(data.getStringExtra("percentage"));
        }
        Proset proset=new Proset();
        proset.setName(name.getText().toString());
        proset.setPrice_percentage(Integer.parseInt(price_percentage.getText().toString()));
        proset.setUser(BmobUser.getCurrentUser(BmobUser.class));
        proset.setPrice_high(price_high.getText().toString());
        proset.setPrice_low(price_low.getText().toString());
        proset.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
            }
        });
    }
}
