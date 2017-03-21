package com.hypervisor.eintegrator.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.hypervisor.eintegrator.utils.PrefManager;
import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.adapter.BillAdapter;
import com.hypervisor.eintegrator.adapter.ItemAdapter;
import com.hypervisor.eintegrator.model.BillDetails;
import com.hypervisor.eintegrator.model.TransactionDetails;
import com.hypervisor.eintegrator.utils.AppController;
import com.hypervisor.eintegrator.utils.Constants;
import com.hypervisor.eintegrator.utils.Decryptor;
import com.hypervisor.eintegrator.utils.Encryptor;
import com.hypervisor.eintegrator.utils.ViewDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ElectricityBill extends AppCompatActivity {

    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    BillAdapter mAdapter;
    ArrayList<BillDetails> billDetailsArrayList;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefManager = new PrefManager(this);
        billDetailsArrayList = new ArrayList<>();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(ElectricityBill.this, "123456789");
            }
        });
        if(prefManager.getPaymentMade() != null) {
            fab.hide();
            Toast.makeText(this,"Payment is already made",Toast.LENGTH_LONG).show();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        billDetails();
        mAdapter = new BillAdapter(this,billDetailsArrayList);

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

    private void billDetails() {
        showpDialog();
        String url = Constants.URL_BILL_DETAILS;
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("RESPONSE " + response);
                        String decrypt = Decryptor.decrypt(response);
                        JSONObject jsonObject = null;
                        ArrayList<BillDetails> billDetailsArrayList = new ArrayList<>();
                        try {
                            jsonObject = new JSONObject(decrypt);
                            JSONObject fetchDetails = jsonObject.getJSONObject("FetchDetails");
                            JSONObject transactionDetails = fetchDetails.getJSONObject("TransactionDetails");
                            JSONArray billDetails = fetchDetails.getJSONArray("BillDetails");
                            Gson gson = new Gson();
                            TransactionDetails objTransaction = gson.fromJson(String.valueOf(transactionDetails), TransactionDetails.class);
                            BillDetails service = new BillDetails();
                            service.setLabelName("Service Name");
                            service.setLableValue(objTransaction.getServiceName());
                            mAdapter.addItem(service,billDetailsArrayList.size());
                            billDetailsArrayList.add(service);
                            for (int i = 0; i < billDetails.length(); i++) {
                                JSONObject obj = billDetails.getJSONObject(i);
                                BillDetails billDetail = gson.fromJson(obj.toString(), BillDetails.class);
                                mAdapter.addItem(billDetail,billDetailsArrayList.size());
                                billDetailsArrayList.add(billDetail);
                            }
                            hidepDialog();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
                System.out.println("ERROR " + error.getMessage());
                hidepDialog();

            }
        }){

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String enc = Encryptor.encrypt(Constants.URL_FETCH_BILL_PARAM);
                params.put("encData", enc);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(jsonObjRequest);
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
