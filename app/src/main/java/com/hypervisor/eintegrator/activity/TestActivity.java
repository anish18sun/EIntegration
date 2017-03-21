package com.hypervisor.eintegrator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.services.DeviceService;
import com.hypervisor.eintegrator.services.MyService;
import com.hypervisor.eintegrator.utils.Constants;

public class TestActivity extends AppCompatActivity {
    private static final String ACTION_PLAY_FILE = "com.example.action.PLAYFILE";
    private static final String ACTION_PLAY_URL = "com.example.action.PLAYURL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, DeviceService.class);
        intent.setAction(ACTION_PLAY_FILE);
        intent.putExtra(Constants.FILE_RAW,R.raw.kripya_apni_bhasha_chunein);
        startService(intent);

    }

}
