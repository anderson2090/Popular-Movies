package com.example.usama.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.usama.popularmovies.fragments.SettingsFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_setting);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
