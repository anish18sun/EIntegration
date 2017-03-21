package com.hypervisor.eintegrator.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.activity.LoginActivity;
import com.hypervisor.eintegrator.adapter.EntitlementsAdapter;
import com.hypervisor.eintegrator.adapter.ItemAdapter;
import com.hypervisor.eintegrator.model.Accural;
import com.hypervisor.eintegrator.model.Entitlement;
import com.hypervisor.eintegrator.model.EntitlementInformation;
import com.hypervisor.eintegrator.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dexter on 19/3/17.
 */
public class EntitlementsFragment extends Fragment {


    private static final String TAG = "Entitlement";
    EntitlementsAdapter mAdapter;
    PrefManager prefManager;
    TextView bhamashahId;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private ArrayList<EntitlementInformation> entitlements;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entitlement, container, false);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        entitlements = new ArrayList<>();
        prefManager = new PrefManager(getContext());

        bhamashahId = (TextView) view.findViewById(R.id.tvBhamashahId);
        if (prefManager.getBhamashahId() == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent);
            getActivity().finish();
        }
        bhamashahId.setText(prefManager.getBhamashahId());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new EntitlementsAdapter(getContext(), entitlements);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ItemAdapter.RecyclerTouchListener(getContext(), recyclerView, new ItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        makeJsonObjectRequest();
        return view;
    }

    private void makeJsonObjectRequest() {
        String str = "{\n" +
                "\t\"requestId\":\"scheme#1083\",\n" +
                "\t\"entitlement\": {\n" +
                "\t\t\"entitlementId\":\"1414\",\n" +
                "\t\t\"entitlementMemId\":\"1\"\n" +
                "\t},\n" +
                "\t\"isSaved\":\"Y\"\n" +
                "}";
        try {
            JSONObject jsonObject = new JSONObject(str);
            String exist = jsonObject.getString("isSaved");
            Entitlement entitlement = null;
            if (exist.equalsIgnoreCase("y")) {
                Gson gson = new Gson();
                entitlement = gson.fromJson(jsonObject.getString("entitlement"), Entitlement.class);
                EntitlementInformation information = new EntitlementInformation();
                information.setEntitlement(entitlement);
                mAdapter.addItem(information, entitlements.size());
                String strAccural = "{\n" +
                        "\t\"entitlementId\":\"1414\",\n" +
                        "\t\"entitlementMemId\":null,\n" +
                        "\t\"transactionId\":\"23232\",\n" +
                        "\t\"dueDate\":\"01/04/2015\",\n" +
                        "\t\"dueAmount\":\"800\"\n" +
                        "}";

                try {
                    JSONObject jsonObjectAccural = new JSONObject(strAccural);
                    Gson gsonAccural = new Gson();
                    Accural accural = gsonAccural.fromJson(String.valueOf(jsonObjectAccural), Accural.class);
                    information.setAccural(accural);
                    mAdapter.notifyItemChanged(entitlements.size() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hidepDialog();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}