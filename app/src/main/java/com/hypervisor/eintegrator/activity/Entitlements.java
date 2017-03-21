package com.hypervisor.eintegrator.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.adapter.BhamashahAdapter;
import com.hypervisor.eintegrator.adapter.EntitlementsAdapter;
import com.hypervisor.eintegrator.adapter.ItemAdapter;
import com.hypervisor.eintegrator.model.Accural;
import com.hypervisor.eintegrator.model.Entitlement;
import com.hypervisor.eintegrator.model.EntitlementInformation;
import com.hypervisor.eintegrator.model.FamilyHeadType;
import com.hypervisor.eintegrator.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Entitlements extends AppCompatActivity {

    private static final String TAG = "Entitlement" ;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    EntitlementsAdapter mAdapter;
    private ArrayList<EntitlementInformation> entitlements ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entitlements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        entitlements = new ArrayList<>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new EntitlementsAdapter(this,entitlements);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ItemAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        makeJsonObjectRequest();

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
                System.out.println("ENTITLEMENT : " + entitlement);
                EntitlementInformation information = new EntitlementInformation();
                information.setEntitlement(entitlement);
                mAdapter.addItem(information,entitlements.size());
                // now fetch accural
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Fetching Entitlement Details");
                builder.setCancelable(true);

                final AlertDialog closedialog= builder.create();

                closedialog.show();

                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        closedialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 5000); // the timer will count 5 seconds....
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
                    System.out.println("ACCURAL : " + accural);
                    information.setAccural(accural);
                    mAdapter.notifyItemChanged(entitlements.size()-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hidepDialog();
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                URL+"WDYYYGG?client_id=" + "ad7288a4-7764-436d-a727-783a977f1fe1", null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//                try {
//                    JSONObject jsonObject = response;
//                    String headOfFaamily = jsonObject.getString("hof_Details");
//                    Gson gson = new Gson();
//                    FamilyHeadType obj = gson.fromJson(headOfFaamily, FamilyHeadType.class);
//                    familyMembers.add(obj);
//                    JSONArray familyDetails = jsonObject.getJSONArray("family_Details");
//                    System.out.println(familyDetails);
//
//                    for (int i = 0; i < familyDetails.length(); i++) {
//                        FamilyHeadType obj1 = gson.fromJson(familyDetails.get(i).toString(), FamilyHeadType.class);
//                        familyMembers.add(obj1);
//                    }
//
//                    mAdapter.notifyDataSetChanged();
//                    System.out.println("FAMILY : " + familyMembers);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                hidepDialog();
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                hidepDialog();
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
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
