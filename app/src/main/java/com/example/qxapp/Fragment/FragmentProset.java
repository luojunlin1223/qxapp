package com.example.qxapp.Fragment;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.qxapp.Preference.SeekBarPreference;
import com.example.qxapp.R;

public class FragmentProset extends PreferenceFragment {
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
        price_low.setSummary(price_low.getText()+"元");
        price_high.setSummary(price_high.getText()+"元");
        price_percentage.setSummary(price_percentage.getProgress()+"%");
        price_low.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString()+"元");
                return true;
            }
        });
        price_high.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString()+"元");
                return true;
            }
        });
        price_percentage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString()+"%");
                return true;
            }
        });
    }

    private void initCtrol() {
        price_low= (EditTextPreference) findPreference("price_low");
        price_high= (EditTextPreference) findPreference("price_high");
        price_percentage= (SeekBarPreference) findPreference("percentage");
    }
}
