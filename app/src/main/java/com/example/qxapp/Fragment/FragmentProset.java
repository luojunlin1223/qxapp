package com.example.qxapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qxapp.R;
import com.example.qxapp.activity.AlterProset.AlterPriceRange;
import com.example.qxapp.activity.AlterProset.Altername;
import com.example.qxapp.activity.Bean.Proset;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class FragmentProset extends Fragment {
    private TextView name;
    private TextView price_low;
    private TextView price_high;
    private MaterialEditText brand;
    private CheckBox taobao,jingdong,tianmao,suning;
    private ImageButton namebtn,rangebtn;
    private String id;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentproset,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        price_low= getView().findViewById(R.id.price_low);
        price_high= getView().findViewById(R.id.price_high);
        name=getView().findViewById(R.id.name);
        namebtn=getView().findViewById(R.id.namebtn);
        rangebtn=getView().findViewById(R.id.rangebtn);
        brand=getView().findViewById(R.id.brand);
        taobao=getView().findViewById(R.id.materialCheckBox);
        jingdong=getView().findViewById(R.id.materialCheckBox2);
        tianmao=getView().findViewById(R.id.materialCheckBox3);
        suning=getView().findViewById(R.id.materialCheckBox4);
        initData();
    }

    private void initData() {
        Bundle bundle = getArguments();
        name.setText(bundle.getString("name"));
        price_low.setText(String.valueOf(bundle.getInt("price_low")));
        price_high.setText(String.valueOf(bundle.getInt("price_high")));
        id=bundle.getString("id");
        brand.setText(bundle.getString("brand"));
        String where=bundle.getString("where");
        String[] whereitem = where.split(",");
        for(String item:whereitem){
            if(item.equals("淘宝")){
                taobao.setChecked(true);
            }
            if(item.equals("京东")){
                jingdong.setChecked(true);
            }
            if(item.equals("天猫")){
                tianmao.setChecked(true);
            }
            if(item.equals("苏宁")){
                suning.setChecked(true);
            }
        }
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
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                save();
            }
        });
        price_low.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                save();
            }
        });
        price_high.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                save();
            }
        });
        brand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                save();
            }
        });
        taobao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                save();
            }
        });
        jingdong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                save();
            }
        });
        tianmao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                save();
            }
        });
        suning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                save();
            }
        });

    }

    private void save() {
        String bran=brand.getText().toString();
        if(bran.matches("^([a-zA-Z0-9\u4e00-\u9fa5]+[,|，])*[a-zA-Z0-9\u4e00-\u9fa5]+$")){
            Proset prose=new Proset();
            prose.setBrand(bran);
            prose.setName(name.getText().toString());
            prose.setUser(BmobUser.getCurrentUser(BmobUser.class));
            prose.setPrice_low(price_low.getText().toString());
            prose.setPrice_high(price_high.getText().toString());
            String where="";
            if(taobao.isChecked()){
                where+="淘宝,";
            }
            if(jingdong.isChecked()){
                where+="京东,";
            }
            if(tianmao.isChecked()){
                where+="天猫,";
            }
            if(suning.isChecked()){
                where+="苏宁,";
            }
            where=where.substring(0,where.length()-1);
            prose.setWhere(where);
            prose.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    Toast.makeText(getContext(),"更新成功",Toast.LENGTH_LONG).show();
                }
            });
        }else{
            brand.setHint("输入你想要的品牌（多个品牌用逗号分隔,如果不输入则默认为所有品牌)");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001&&resultCode==1){
            name.setText(data.getStringExtra("name"));
        }else if(requestCode==1002&&resultCode==1){
            price_low.setText(data.getStringExtra("price_low"));
            price_high.setText(data.getStringExtra("price_high"));
        }
    }
}
