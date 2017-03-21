package com.hypervisor.eintegrator.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hypervisor.eintegrator.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dexter on 19/3/17.
 */
public class BhamashahFragment extends Fragment {

    private static final String TAG = "Bhamashah";
    BhamashahAdapter mAdapter;
    Intent intentSound;
    PrefManager prefManager;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private ArrayList<FamilyHeadType> familyMembers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bhamashah, container, false);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        prefManager = new PrefManager(getContext());
        intentSound = null;
        familyMembers = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new BhamashahAdapter(getContext(), familyMembers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ItemAdapter.RecyclerTouchListener(getContext(), recyclerView, new ItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FamilyHeadType familyHeadType = familyMembers.get(position);
                String str = "Your name is " + familyHeadType.getNAME_ENG() + ". Your Father's Name is "
                        + familyHeadType.getFATHER_NAME_ENG() + ". Your Mother's Name is "
                        + familyHeadType.getMOTHER_NAME_ENG() + "Your Date of Birth is "
                        + familyHeadType.getDOB();
                String url = "http://audiotravels.in:9005/tts?type=indiantts&text=+" + str + "&api_key=2d108780-0b86-11e6-b056-07d516fb06e1&user_id=80&numeric=currency&action=play";
                playSound(url);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        makeJsonObjectRequest();
        return view;
    }

    public void stopAudio() {
        Intent intent = new Intent(getContext(), DeviceService.class);
        getContext().stopService(intent);
    }

    private void playSound(String url) {
        intentSound = new Intent(getContext(), DeviceService.class);
        intentSound.setAction(Constants.ACTION_PLAY_FILE);
        intentSound.putExtra(Constants.URL_SOUND, url);
        getContext().startService(intentSound);
    }

    private void makeJsonObjectRequest() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + "WDYYYGG" + Constants.CLIENT_ID_STR + Constants.CLIENT_ID, null, new Response.Listener<JSONObject>() {

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

                    for (int i = 0; i < familyDetails.length(); i++) {
                        FamilyHeadType obj1 = gson.fromJson(familyDetails.get(i).toString(), FamilyHeadType.class);
                        familyMembers.add(obj1);
                    }

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(),
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

    @Override
    public void onDestroy() {
        hidepDialog();
        super.onDestroy();
    }
}
