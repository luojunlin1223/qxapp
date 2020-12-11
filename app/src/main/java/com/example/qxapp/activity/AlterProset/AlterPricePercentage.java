package com.example.qxapp.activity.AlterProset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.qxapp.R;

public class AlterPricePercentage extends AppCompatActivity {
    private ImageButton canclebtn;
    private Button savebtn;
    private TextView percentage;
    private SeekBar percentage_seekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_price_percentage);
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
        percentage_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        percentage_seekbar.setProgress(Integer.parseInt(getIntent().getStringExtra("percentage")));
        percentage.setText(String.valueOf(percentage_seekbar.getProgress()));
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("percentage",percentage.getText().toString());
                setResult(1,intent);
                finish();
            }
        });
    }

    private void initControl() {
        canclebtn=findViewById(R.id.cancelbtn);
        savebtn=findViewById(R.id.savebtn);
        percentage=findViewById(R.id.percentage);
        percentage_seekbar=findViewById(R.id.percentage_seekbar);
    }

}