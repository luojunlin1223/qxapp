package com.example.qxapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Proset;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import kotlin.text.Regex;

public class WriteProset extends AppCompatActivity {
    private ImageButton canclebtn;
    private Button savebtn;
    private MaterialEditText name,price_low,price_high,brand;
    private CheckBox toabao,jingdong,tianmao,suning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_proset);
        initControl();
        initData();
    }

    private void initData() {
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Vaildate()){
                    save();
                }
            }
        });
        name.setHint("输入你的预设名称");
        price_low.setHint("最低价格");
        price_high.setHint("最高价格");
    }

    private void save() {
        BmobQuery<Proset>query=new BmobQuery<>();
        BmobQuery<Proset>user_query=new BmobQuery<>();
        BmobQuery<Proset>name_query=new BmobQuery<>();
        user_query.addWhereEqualTo("user", BmobUser.getCurrentUser(BmobUser.class));
        name_query.addWhereEqualTo("name",name.getText());
        List<BmobQuery<Proset>> bmobQueries=new ArrayList<>();
        bmobQueries.add(user_query);
        bmobQueries.add(name_query);
        query.and(bmobQueries);
        query.findObjects(new FindListener<Proset>() {
            @Override
            public void done(List<Proset> list, BmobException e) {
                if(e==null){
                    if(list.size()==0){
                        Proset proset=new Proset();
                        proset.setName(name.getText().toString());
                        proset.setUser(BmobUser.getCurrentUser(BmobUser.class));
                        proset.setPrice_low(price_low.getText().toString());
                        proset.setPrice_high(price_high.getText().toString());
                        proset.setBrand(brand.getText().toString());
                        String where="";
                        if(toabao.isChecked()){
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
                        proset.setWhere(where);
                        proset.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                setResult(1);
                                finish();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"这个预设已经存在",Toast.LENGTH_SHORT).show();;
                    }
                }else{
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private boolean Vaildate() {
        if(name.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"预设名称不能为空!",Toast.LENGTH_SHORT).show();
            name.setText("");
            return false;
        }
        if(price_low.getText().length()==0){
            Toast.makeText(getApplicationContext(),"最低价格不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(price_high.getText().length()==0){
            Toast.makeText(getApplicationContext(),"最高价格不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Integer.parseInt(price_low.getText().toString())>=Integer.parseInt(price_high.getText().toString())){
            Toast.makeText(getApplicationContext(),"最低价格高于最高价格!",Toast.LENGTH_SHORT).show();
            price_low.setText("");
            price_high.setText("");
            return false;
        }
        String regex=brand.getText().toString();
        if(regex.matches("^([a-zA-Z0-9\u4e00-\u9fa5]+[,|，])*[a-zA-Z0-9\u4e00-\u9fa5]+$")||regex.isEmpty()){
            return true;
        }else{
            Toast.makeText(getApplicationContext(),"品牌输入格式错误!",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initControl() {
        canclebtn=findViewById(R.id.cancelbtn);
        savebtn=findViewById(R.id.savebtn);
        name=findViewById(R.id.name);
        price_high=findViewById(R.id.price_high);
        price_low=findViewById(R.id.price_low);
        toabao=findViewById(R.id.materialCheckBox);
        jingdong=findViewById(R.id.materialCheckBox2);
        tianmao=findViewById(R.id.materialCheckBox3);
        suning=findViewById(R.id.materialCheckBox4);
        brand=findViewById(R.id.brand);
    }
}