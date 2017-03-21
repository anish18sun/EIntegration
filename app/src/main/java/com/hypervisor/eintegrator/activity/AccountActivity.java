package com.hypervisor.eintegrator.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.model.FamilyHeadType;
import com.hypervisor.eintegrator.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {


    private static final String TAG = "Bhamashah" ;
    private ProgressDialog pDialog;
    private static final String URL = "https://apitest.sewadwaar.rajasthan.gov.in/app/live/Service/hofAndMember/ForApp/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        makeJsonObjectRequest();
    }
    private void makeJsonObjectRequest() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL+"WDYYYGG?client_id=" + "ad7288a4-7764-436d-a727-783a977f1fe1", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject jsonObject = response;
                    String headOfFaamily = jsonObject.getString("hof_Details");

                    System.out.println(headOfFaamily);
                    Gson gson = new Gson();
                    FamilyHeadType obj = gson.fromJson(headOfFaamily, FamilyHeadType.class);
                    System.out.println(obj);

                    JSONArray familyDetails = jsonObject.getJSONArray("family_Details");
                    System.out.println(familyDetails);

                    ArrayList<FamilyHeadType> familyMembers = new ArrayList<>();
                    for (int i = 0; i < familyDetails.length(); i++) {
                        FamilyHeadType obj1 = gson.fromJson(familyDetails.get(i).toString(), FamilyHeadType.class);
                        familyMembers.add(obj1);
                    }

                    System.out.println("FAMILY : " + familyMembers);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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
