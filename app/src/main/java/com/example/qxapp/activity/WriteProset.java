package com.example.qxapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class WriteProset extends AppCompatActivity {
    private ImageButton canclebtn;
    private Button savebtn;
    private MaterialEditText name,price_low,price_high;
    private TextView percentage;
    private SeekBar seekBar;
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
                save();
            }
        });
        name.setHint("输入你的预设名称");
        price_low.setHint("最低价格");
        price_high.setHint("最高价格");
        seekBar.setProgress(0);
        percentage.setText("0");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percentage.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
                        proset.setPrice_percentage(Integer.parseInt(percentage.getText().toString()));
                        proset.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
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

    private void initControl() {
        canclebtn=findViewById(R.id.cancelbtn);
        savebtn=findViewById(R.id.savebtn);
        name=findViewById(R.id.name);
        price_high=findViewById(R.id.price_high);
        price_low=findViewById(R.id.price_low);
        percentage=findViewById(R.id.percentage);
        seekBar=findViewById(R.id.seekBar);
    }
}