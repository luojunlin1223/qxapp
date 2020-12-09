package com.example.qxapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;

import com.example.qxapp.Preference.SeekBarPreference;
import com.example.qxapp.R;
import com.example.qxapp.activity.Bean.Proset;
import com.example.qxapp.activity.Search;

import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class FragmentProset extends PreferenceFragment {
    private EditTextPreference name;
    private EditTextPreference price_low;
    private EditTextPreference price_high;
    private SeekBarPreference price_percentage;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.fragmentproset);
        initCtrol();
        initData();

    }


    private void initData() {
        if(!price_low.getText().isEmpty()){
            price_low.setSummary(price_low.getText()+"元");
        }
        if(!price_high.getText().isEmpty()){
            price_high.setSummary(price_high.getText()+"元");
        }
        price_percentage.setSummary(price_percentage.getProgress()+"%");
        if(!name.getText().isEmpty()){
            name.setSummary(name.getText());
        }
        name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());
                ProsetUpdate();
                return true;
            }
        });
        price_low.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString()+"元");
                ProsetUpdate();
                return true;
            }
        });
        price_high.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString()+"元");
                ProsetUpdate();
                return true;
            }
        });
        price_percentage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString()+"%");
                ProsetUpdate();
                return true;
            }
        });

    }

    private void ProsetUpdate() {
        BmobQuery<Proset>query=new BmobQuery<>();
        query.addWhereEqualTo("name",name.getText());
        query.findObjects(new FindListener<Proset>() {
            @Override
            public void done(List<Proset> list, BmobException e) {
                if(e==null){
                    if(list.size()==0){
                        Proset proset=new Proset();
                        proset.setName(name.getText());
                        proset.setUser(BmobUser.getCurrentUser(BmobUser.class));
                        proset.setPrice_percentage(price_percentage.getProgress());
                        proset.setPrice_high(Double.parseDouble(price_high.getText()));
                        proset.setPrice_low(Double.parseDouble(price_low.getText()));
                        proset.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                            }
                        });
                    }else{
                        String objectid=list.get(0).getObjectId();
                        Proset proset=new Proset();
                        proset.setName(name.getText());
                        proset.setUser(BmobUser.getCurrentUser(BmobUser.class));
                        proset.setPrice_percentage(price_percentage.getProgress());
                        proset.setPrice_high(Double.parseDouble(price_high.getText()));
                        proset.setPrice_low(Double.parseDouble(price_low.getText()));
                        proset.update(objectid, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                            }
                        });
                    }

                }else{
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void initCtrol() {
        price_low= (EditTextPreference) findPreference("price_low");
        price_high= (EditTextPreference) findPreference("price_high");
        price_percentage= (SeekBarPreference) findPreference("percentage");
        name=(EditTextPreference) findPreference("name");
    }



}
