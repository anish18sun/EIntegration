package com.hypervisor.eintegrator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.fragments.BhamashahFragment;
import com.hypervisor.eintegrator.fragments.BillFragment;
import com.hypervisor.eintegrator.fragments.EntitlementsFragment;
import com.hypervisor.eintegrator.fragments.HomeFragment;
import com.hypervisor.eintegrator.utils.Constants;
import com.hypervisor.eintegrator.utils.InternetConnection;
import com.hypervisor.eintegrator.utils.PrefManager;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MainActivity";
    Toolbar toolbar;
    PrefManager prefManager;
    CharSequence[] values = {"English", "हिंदी", "मारवाड़ी"};
    AlertDialog alertDialog;
    private  DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        prefManager = new PrefManager(this);
        System.out.println("LANGUAGE : " + prefManager.getBhamashahId());
        if(prefManager.getBhamashahId() == null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header);

        TextView id = (TextView) headerLayout.findViewById(R.id.tvBhamashahId);
        id.setText(prefManager.getBhamashahId());

        Fragment fragment = new HomeFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String btnName = null;

        switch (itemId) {
            // Android home
            case android.R.id.home: {
                drawer.openDrawer(GravityCompat.START);
            }
            break;
            case R.id.action_language: {
                // change language
                dialogLanguageChange();
            }
            break;
            case R.id.action_chat:
                Intent intent = new Intent(this,ChatActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void dialogLanguageChange() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Language/भाषा चुनें");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        setLocale("en");
                        prefManager.setLanguage(Constants.LAN_ENG);
                        break;
                    case 1:
                        setLocale("hi");
                        prefManager.setLanguage(Constants.LAN_HINDI);
                        break;
                    case 2:
                        setLocale("hu");
                        prefManager.setLanguage(Constants.LAN_MARWARI);
                        break;
                }
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Fragment fragment = null;

        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                clearBackStack();
                fragment = new HomeFragment();
                break;
            case R.id.bhamashah:
                if(InternetConnection.isNetworkAvailable(MainActivity.this)){
                    fragment = new BhamashahFragment();
                }else{
                    showSnackBar();
                }
                break;
            case R.id.entitlement:
                if(InternetConnection.isNetworkAvailable(MainActivity.this)){
                    fragment = new EntitlementsFragment();
                }else{
                    showSnackBar();
                }
                break;
            case R.id.bills:
                if(InternetConnection.isNetworkAvailable(MainActivity.this)){
                    fragment = new BillFragment();
                }else{
                    showSnackBar();
                }
                break;
            case R.id.logout:
                clearPreferences();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                clearBackStack();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        return true;
    }

    private void showSnackBar(){
        Snackbar snackbar = Snackbar
                .make(drawer, "Check your Internet Connection", Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void clearPreferences() {
        PrefManager prefManager = new PrefManager(MainActivity.this);
        prefManager.setBhamashahId(null);
    }
}
