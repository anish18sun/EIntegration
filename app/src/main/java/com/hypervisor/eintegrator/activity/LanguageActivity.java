package com.hypervisor.eintegrator.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.hypervisor.eintegrator.utils.PrefManager;
import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.services.DeviceService;
import com.hypervisor.eintegrator.utils.Constants;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    TextView bEnglish, bHindi, bMarwari;
    String lang;
    FloatingActionButton floatingActionButton;
    Intent intent;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        prefManager = new PrefManager(this);
        intent = null;
        System.out.println("STATE : " + prefManager.isFirstTimeLaunch() + " " + prefManager.getLanguage());
        if (!prefManager.isFirstTimeLaunch() && prefManager.getLanguage() != null) {
            launchHomeScreen();
            finish();
        }
        if(prefManager.getLanguage() != null){
            launchHomeScreen();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        lang = null;
        bEnglish = (TextView) findViewById(R.id.bEnglish);
        bHindi = (TextView) findViewById(R.id.bHindi);
        bMarwari = (TextView) findViewById(R.id.bMarwari);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        bEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = Constants.LAN_ENG;
                bEnglish.setTextColor(getResources().getColor(android.R.color.white));
                bHindi.setTextColor(getResources().getColor(R.color.colorNotSelected));
                bMarwari.setTextColor(getResources().getColor(R.color.colorNotSelected));
            }
        });
        bHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = Constants.LAN_HINDI;
                bHindi.setTextColor(getResources().getColor(android.R.color.white));
                bEnglish.setTextColor(getResources().getColor(R.color.colorNotSelected));
                bMarwari.setTextColor(getResources().getColor(R.color.colorNotSelected));
            }
        });
        bMarwari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = Constants.LAN_MARWARI;
                bMarwari.setTextColor(getResources().getColor(android.R.color.white));
                bEnglish.setTextColor(getResources().getColor(R.color.colorNotSelected));
                bHindi.setTextColor(getResources().getColor(R.color.colorNotSelected));
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lang == null) {
                    Snackbar.make(v, "Select language", Snackbar.LENGTH_LONG).show();
                } else {
                    prefManager.setLanguage(lang);
                    if (lang.equalsIgnoreCase(Constants.LAN_ENG)) {
                        setLocale("en");
                        prefManager.setLanguage(Constants.LAN_ENG);
                    } else if (lang.equalsIgnoreCase(Constants.LAN_HINDI)) {
                        setLocale("hi");
                        prefManager.setLanguage(Constants.LAN_HINDI);
                    } else {
                        setLocale("hu");
                        prefManager.setLanguage(Constants.LAN_MARWARI);
                    }
                }
                launchHomeScreen();
            }
        });
        intent = new Intent(this, DeviceService.class);
        intent.setAction(Constants.ACTION_PLAY_FILE);
        intent.putExtra(Constants.FILE_RAW, R.raw.kripya_apni_bhasha_chunein);
        startService(intent);
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(LanguageActivity.this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, WelcomeActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    protected void onStop() {
        if (intent != null)
            stopService(intent);
        super.onStop();
    }
}
