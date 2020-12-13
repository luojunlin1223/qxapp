package com.example.qxapp.activity.AlterProset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.qxapp.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class AlterPriceRange extends AppCompatActivity {
    private ImageButton canclebtn;
    private MaterialEditText price_low, price_high;
    private Button savebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_price_range);

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
        price_low.setText(getIntent().getStringExtra("price_low"));
        price_high.setText(getIntent().getStringExtra("price_high"));
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("price_low",price_low.getText().toString());
                intent.putExtra("price_high",price_high.getText().toString());
                setResult(1,intent);
                finish();
            }
        });
    }

    private void initControl() {
        canclebtn=findViewById(R.id.cancelbtn);
        savebtn=findViewById(R.id.savebtn);
        price_high=findViewById(R.id.alter_price_high);
        price_low=findViewById(R.id.alter_price_low);
    }

}