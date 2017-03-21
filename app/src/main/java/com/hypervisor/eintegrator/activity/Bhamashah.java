package com.hypervisor.eintegrator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.hypervisor.eintegrator.adapter.ItemAdapter;
import com.hypervisor.eintegrator.model.FamilyHeadType;
import com.hypervisor.eintegrator.services.DeviceService;
import com.hypervisor.eintegrator.utils.AppController;
import com.hypervisor.eintegrator.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Bhamashah extends AppCompatActivity {

    private static final String TAG = "Bhamashah" ;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    BhamashahAdapter mAdapter;
    private ArrayList<FamilyHeadType> familyMembers ;
    Intent intentSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhamashah);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intentSound = null;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        familyMembers = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new BhamashahAdapter(this,familyMembers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ItemAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FamilyHeadType familyHeadType = familyMembers.get(position);
                String str = "your%20name%20is%20" + familyHeadType.getNAME_ENG().replace(" " , "%20") + "%20" +  "your%20father%20name%20is%20"
                        + familyHeadType.getFATHER_NAME_ENG().replace(" " , "%20").toLowerCase() + "%20your%20mother%20name%20is%20"
                        + familyHeadType.getMOTHER_NAME_ENG().replace(" " , "%20").toLowerCase() + "%20your%20Date%20of%20birth%20is%20"
                        + familyHeadType.getDOB().replace(" " , "%20");
                String url = "http://audiotravels.in:9005/tts?type=indiantts&text=+" + str + "&api_key=2d108780-0b86-11e6-b056-07d516fb06e1&user_id=80&numeric=currency&action=play";
                playSound(url);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        makeJsonObjectRequest();
    }

    public void stopAudio() {
        Intent intent = new Intent(this, DeviceService.class);
        stopService(intent);
    }

    private void playSound(String url){
        System.out.println("HELLO");
        intentSound = new Intent(this, DeviceService.class);
        intentSound.setAction(Constants.ACTION_PLAY_URL);
        intentSound.putExtra(Constants.URL_SOUND,url);
        startService(intentSound);
    }

    private void makeJsonObjectRequest() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL+"WDYYYGG" + Constants.CLIENT_ID_STR + Constants.CLIENT_ID, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject jsonObject = response;
                    String headOfFaamily = jsonObject.getString("hof_Details");
                    Gson gson = new Gson();
                    FamilyHeadType obj = gson.fromJson(headOfFaamily, FamilyHeadType.class);
                    familyMembers.add(obj);
                    JSONArray familyDetails = jsonObject.getJSONArray("family_Details");
                    System.out.println(familyDetails);

                    for (int i = 0; i < familyDetails.length(); i++) {
                        FamilyHeadType obj1 = gson.fromJson(familyDetails.get(i).toString(), FamilyHeadType.class);
                        familyMembers.add(obj1);
                    }

                    mAdapter.notifyDataSetChanged();
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
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
