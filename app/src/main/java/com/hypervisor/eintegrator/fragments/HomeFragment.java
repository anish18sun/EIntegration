package com.hypervisor.eintegrator.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.activity.Bhamashah;
import com.hypervisor.eintegrator.adapter.ItemAdapter;
import com.hypervisor.eintegrator.utils.InternetConnection;
import com.hypervisor.eintegrator.utils.PrefManager;

/**
 * Created by dexter on 19/3/17.
 */
public class HomeFragment extends Fragment {
    PrefManager prefManager;
    private ItemAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        prefManager = new PrefManager(getContext());
        String lang = prefManager.getLanguage();
        adapter = new ItemAdapter(getContext(), lang);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new ItemAdapter.RecyclerTouchListener(getContext(), recyclerView, new ItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getContext(), Bhamashah.class);
                        startActivity(intent);
                        break;
                    case 1:
                        if (!InternetConnection.isNetworkAvailable(getContext()))
                            Toast.makeText(getContext(), getResources()
                                    .getString(R.string.internet_not_connected), Toast.LENGTH_LONG)
                                    .show();
                        else
                            replaceFragment(new BillFragment());
                        break;
                    case 2:
                        if (!InternetConnection.isNetworkAvailable(getContext()))
                            Toast.makeText(getContext(), getResources()
                                    .getString(R.string.internet_not_connected), Toast.LENGTH_LONG)
                                    .show();
                        else
                            replaceFragment(new EntitlementsFragment());
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
