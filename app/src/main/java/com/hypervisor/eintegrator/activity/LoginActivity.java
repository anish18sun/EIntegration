package com.hypervisor.eintegrator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hypervisor.eintegrator.utils.PrefManager;
import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.services.DeviceService;
import com.hypervisor.eintegrator.utils.AppController;
import com.hypervisor.eintegrator.utils.Constants;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText _id;
    private Button _login;
    private ProgressDialog pDialog;
    private PrefManager prefManager;
    private Intent intentSound ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.message_wait));
        pDialog.setCancelable(false);
        prefManager = new PrefManager(this);
        intentSound = null;

        if(prefManager.getBhamashahId() != null){
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            playSound();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        _id = (EditText) findViewById(R.id.input_id);
        _login = (Button) findViewById(R.id.btn_login);
        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpDialog();
                if (_id.getText().toString().isEmpty()) {
                    _id.setError(getResources().getString(R.string.bhamashah_id_error));
                } else {
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            Constants.URL + _id.getText().toString() + Constants.CLIENT_ID_STR + Constants.CLIENT_ID, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            JSONObject jsonObject = response;
                            boolean result = jsonObject.has("result");
                            if (!result) {
                                // data found
                                // move to another activity
                                prefManager.setBhamashahId(_id.getText().toString());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                hidepDialog();
                                finish();
                            } else {
                                // show error no date found
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.bhamashah_wrong_details), Toast.LENGTH_SHORT).show();
                            }
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
                hidepDialog();
            }
        });
    }

    @Override
    protected void onStop() {
        stopAudio();
        super.onStop();
    }

    public void stopAudio() {
        Intent intent = new Intent(this, DeviceService.class);
        stopService(intent);
    }

    private void playSound(){
        String language = prefManager.getLanguage();
        intentSound = new Intent(this, DeviceService.class);
        intentSound.setAction(Constants.ACTION_PLAY_FILE);
        if(language.equals(Constants.LAN_ENG)){
            intentSound.putExtra(Constants.FILE_RAW, R.raw.please_enter_your_bhamashah_id_english);
        }else if(language.equals(Constants.LAN_HINDI)){
            intentSound.putExtra(Constants.FILE_RAW,R.raw.bhamashah_id_daalein_hindi);
        }else{
            intentSound.putExtra(Constants.FILE_RAW,R.raw.bhamashah_id_daalo_marwari);
        }
        startService(intentSound);
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
