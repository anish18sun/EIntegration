package com.hypervisor.eintegrator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.adapter.BillTypeAdapter;
import com.hypervisor.eintegrator.adapter.EntitlementsAdapter;
import com.hypervisor.eintegrator.adapter.ItemAdapter;

public class BillPayment extends AppCompatActivity {

    private RecyclerView recyclerView;
    BillTypeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new BillTypeAdapter(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ItemAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = null;
                switch(position){
                    case 0:
                        // only electricity
                        intent = new Intent(BillPayment.this,ElectricityBill.class);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String btnName = null;

        switch(itemId) {
            // Android home
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return false;
    }

}
