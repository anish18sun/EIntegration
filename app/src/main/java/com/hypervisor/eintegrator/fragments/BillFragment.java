package com.hypervisor.eintegrator.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.activity.ElectricityBill;
import com.hypervisor.eintegrator.adapter.BillTypeAdapter;
import com.hypervisor.eintegrator.adapter.ItemAdapter;

/**
 * Created by dexter on 19/3/17.
 */
public class BillFragment extends Fragment {

    BillTypeAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bhamashah, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new BillTypeAdapter(getContext());


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ItemAdapter.RecyclerTouchListener(getContext(), recyclerView, new ItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        // only electricity
                        intent = new Intent(getContext(), ElectricityBill.class);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }
}
