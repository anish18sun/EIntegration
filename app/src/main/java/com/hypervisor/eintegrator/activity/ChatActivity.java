package com.hypervisor.eintegrator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.adapter.MessageItemAdapter;
import com.hypervisor.eintegrator.model.BillDetails;
import com.hypervisor.eintegrator.model.FamilyHeadType;
import com.hypervisor.eintegrator.model.Message;
import com.hypervisor.eintegrator.model.TransactionDetails;
import com.hypervisor.eintegrator.utils.AppController;
import com.hypervisor.eintegrator.utils.Chatbot;
import com.hypervisor.eintegrator.utils.ChecksumGenerator;
import com.hypervisor.eintegrator.utils.Constants;
import com.hypervisor.eintegrator.utils.Decryptor;
import com.hypervisor.eintegrator.utils.Encryptor;
import com.hypervisor.eintegrator.utils.PrefManager;
import com.hypervisor.eintegrator.utils.ResponseObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TYPE_SEND = 2;
    private static final int TYPE_RECEIVE = 1;
    private static final int FILE_SELECT_CODE = 0;
    public ArrayList<Message> mMessages;
    public MessageItemAdapter mMessageAdapter;
    Chatbot chatbot;
    int check = 0;
    PrefManager prefManager;
    String bhamashahId;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fSend;
    private EditText etMessage;
    private String memName, memAge, memGender;
    private int counter = -1;
    private StorageReference storageRef;
    private ProgressDialog progressDialog;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressDialog = new ProgressDialog(ChatActivity.this);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://eintegrator-2c6d9.appspot.com/");
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding File .... Please wait");
        progressDialog.setCancelable(false);
        ActionBar b = getSupportActionBar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        memName = null;
        memAge = null;
        memGender = null;
        prefManager = new PrefManager(this);
        bhamashahId = prefManager.getBhamashahId();
        if (bhamashahId == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        chatbot = new Chatbot();
        mRecyclerView = (RecyclerView) findViewById(R.id.messagelist_recycler_view);
        fSend = (FloatingActionButton) findViewById(R.id.fab);
        fSend.setOnClickListener(this);
        etMessage = (EditText) findViewById(R.id.messagetext);
        if (etMessage.getText().toString().isEmpty()) {
            fSend.setEnabled(false);
            fSend.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        }
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etMessage.getText().length() == 0) {
                    fSend.setEnabled(false);
                    fSend.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                } else {
                    fSend.setEnabled(true);
                    fSend.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mMessages = new ArrayList<Message>();
        Collections.reverse(mMessages);
        mMessageAdapter = new MessageItemAdapter(this, mMessages);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mMessageAdapter);
        ResponseObj obj = chatbot.initiateChat();
        output(obj.getResponse());
        if (obj != null)
            saveObj(obj);
    }

    private void saveObj(ResponseObj obj) {
        // if obj result is true then task is completed
        if (obj.isResult()) {
            chatbot = new Chatbot();
            chatbot.initiateChat();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Log.e("RESULT", "OK");
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    storageRef = storage.getReferenceFromUrl("gs://eintegrator-2c6d9.appspot.com/");
                    StorageReference reference = storageRef.child(bhamashahId).child(memName + memAge);
                    UploadTask uploadTask = reference.putFile(uri);
                    // Register observers to listen for when the download is done or if it fails
                    progressDialog.show();
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            progressDialog.dismiss();
                            Toast.makeText(ChatActivity.this, "File Uploading problem .. Try Again !", Toast.LENGTH_LONG).show();
                            System.out.println("Failed");
                            System.out.println("Exception : " + exception.getMessage());
                            output("We are facing some issue. Please try again later");
                            memGender = null;
                            memAge = null;
                            memGender = null;
                            counter = -1;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            progressDialog.dismiss();
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            System.out.println("SUCCESSFUL " + " " + downloadUrl);
                            output("Name : " + memName + "\n"
                                    + "Age : " + memAge + "\n"
                                    + "Gender : " + memGender + "\n"
                                    + "File Saved : " + downloadUrl.toString());
                            output("We'll get back to you asap");
                            output(correctRandomMessages());
                            memGender = null;
                            memAge = null;
                            memGender = null;
                            counter = -1;
                        }
                    });
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        System.out.println("Message");
        String text = etMessage.getText().toString();
        Message newMessageUser = new Message();
        newMessageUser.setMes(text);
        newMessageUser.setType(TYPE_SEND);
        mMessageAdapter.addItem(mMessages.size(), newMessageUser);
        mMessages.add(newMessageUser);
        mRecyclerView.scrollToPosition(mMessages.size() - 1);
        etMessage.setText("");
        process(text);
    }

    private void process(String text) {
        processStr(text, bhamashahId);
    }

    private void output(String str) {
        Message newMessageUser = new Message();
        newMessageUser.setMes(str);
        newMessageUser.setType(TYPE_RECEIVE);
        mMessageAdapter.addItem(mMessages.size(), newMessageUser);
        mMessages.add(newMessageUser);
        mRecyclerView.scrollToPosition(mMessages.size() - 1);
        etMessage.setText("");

    }

    public void processStr(String str, String bhamashahId) {
        if (str.contains("no") || str.contains("NO")) {
            counter = -1;
            check = 0;
            output(correctRandomMessages());
        } else if (counter != -1) {
            str.trim();

            System.out.println("COUNTER : " + counter);
            if (counter == 0) {
                //name]
                Pattern p = Pattern.compile("[A-Za-z]");
                Matcher m = p.matcher(str);
                boolean b = m.find();
                System.out.println("BOOLEAN " + b);
                if (b == true) {
                    // valid string
                    memName = str;
                    counter++;
                    output("Enter Age");
                } else {
                    output("Enter valid name");
                }
            } else if (counter == 1) {
                //age
                if (isNumeric(str)) {
                    memAge = str;
                    counter++;
                    output("Enter Gender");
                } else {
                    output("Enter valid age");
                }
            } else if (counter == 2) {
                Pattern p = Pattern.compile("[A-Za-z]");
                Matcher m = p.matcher(str);
                boolean b = m.find();
                if (b == true) {
                    // valid string
                    if (str.equalsIgnoreCase("female") || str.equalsIgnoreCase("male")) {
                        memGender = str;
                        counter++;
                        output("Select files to upload");
                        showFileChooser();
                    } else {
                        output("Enter valid gender");
                    }
                } else {
                    output("Enter valid gender");
                }
            } else {
                //upload doc
                showFileChooser();

            }
        } else if (str.equals("1")) {
            // get bhamashah Details and Send
            counter = -1;
            String url = Constants.URL + "WDYYYGG" + Constants.CLIENT_ID_STR + Constants.CLIENT_ID;
            System.out.println("URL " + url);
            LongOperation task = new LongOperation();
            task.execute(new String[]{url});
        } else if (str.equals("2")) {
            // get Payment Details
            counter = -1;
            String enc = Encryptor.encrypt(Constants.URL_FETCH_BILL_PARAM);
            System.out.println("ENC " + enc);
            String url = Constants.URL_BILL_DETAILS;
            BillOperation operation = new BillOperation();
            operation.execute(new String[]{url, enc});
        } else if (str.equals("3")) {
            // get entitlement details and send
            counter = -1;
            String strAccural =
                    "Entitlement Id : 1414 \n" +
                            "transactionId : 23232\n" +
                            "Due Date : 01/04/2015\n" +
                            "Due Amount : 800\n";
            output(strAccural);
            output(correctRandomMessages());
        } else if (str.equals("4")) {
            // register member of family
            // ask name first
            counter = 0;
            output("Enter the name of new Member");
        } else if (check == 1) {
            counter = -1;
            //now check for answer
            if (str.contains("yes") || str.contains("YES")) {
                // make payment
                Paymenttask task = new Paymenttask();
                String url = Constants.URL_MAKE_PAYMENT;
                task.execute(new String[]{url});
            } else {
                //do not make payment
                check = 0;
                output(correctRandomMessages());
            }
        } else {
            // check that the last thing was payment
            // didn't not understood
            if (str.trim().contains("gender")) {
                output("I have none");
            } else if (str.trim().contains("creator") || str.trim().contains("developer")
                    || str.trim().contains("father") || str.trim().contains("mother")) {
                output("You are !! :-p");
            } else if (str.trim().contains("name")) {
                output("I am no one");
            } else {
                output("I am still learning. Don't try me much");
            }
        }
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    private String correctRandomMessages() {
        return (Math.random() < 0.5) ? Constants.correctMsgs[0] : Constants.correctMsgs[1];
    }

    private String errRandomMessages() {
        return (Math.random() < 0.5) ? Constants.errorMsgs[0] : Constants.errorMsgs[1];
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("EXECUTING");
            String url = params[0];
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("RESPONSE", response.toString());
                    try {
                        JSONObject jsonObject = response;
                        String headOfFaamily = jsonObject.getString("hof_Details");
                        Gson gson = new Gson();
                        FamilyHeadType obj = gson.fromJson(headOfFaamily, FamilyHeadType.class);
                        output(obj.toString());
                        output(correctRandomMessages());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("RESPONSE", "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                    output(errRandomMessages());
                    // hide the progress dialog
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class BillOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("EXECUTING");
            String url = params[0];
            final String enc = params[1];
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
                                service.setLableValue(objTransaction.getServiceName());
                                String out = "";
                                out += service.getLabelName() + " : " + service.getLableValue() + "\n";
                                billDetailsArrayList.add(service);
                                for (int i = 0; i < billDetails.length(); i++) {
                                    JSONObject obj = billDetails.getJSONObject(i);
                                    BillDetails billDetail = gson.fromJson(obj.toString(), BillDetails.class);
                                    out += billDetail + "\n";
                                    billDetailsArrayList.add(billDetail);
                                }
                                output(out);
                                output("Would you like to pay?");
                                check = 1;

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
                    output(errRandomMessages());
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("encData", enc);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjRequest);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

    private class Paymenttask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("EXECUTING");
            String url = params[0];
            StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("RESPONSE " + response);
                            output("Payment is Successful");
                            check = 0;
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    output("Payment is Successful");
                    check = 0;
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Random r = new Random();
                    int n = 100000 + r.nextInt(900000);
                    System.out.println("N : " + n);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Constants.MERCHANT_CODE, Constants.MERCHANT_CODE_PAR);
                    params.put(Constants.AMOUNT, Constants.AMOUNT_PAR);
                    params.put(Constants.CANCELURL, Constants.CANCELURL_PAR);
                    params.put(Constants.FAILUREURL, Constants.FAILUREURL_PAR);
                    params.put(Constants.SUCCESSURL, Constants.SUCCESSURL_PAR);
                    params.put(Constants.USERNAME, Constants.USERNAME_PAR);
                    params.put(Constants.USEREMAIL, Constants.USEREMAIL_PAR);
                    params.put(Constants.USERMOBILE, Constants.USERMOBILE_PAR);
                    params.put(Constants.UDF1, Constants.UDF1_PAR);
                    params.put(Constants.UDF2, Constants.UDF2_PAR);
                    params.put(Constants.UDF3, Constants.UDF3_PAR);
                    params.put(Constants.PURPOSE, Constants.PURPOSE_PAR);
                    params.put(Constants.REQTIMESTAMP, Constants.REQTIMESTAMP_PAR);
                    params.put(Constants.PRN, String.valueOf(n));
                    params.put(Constants.CHECKSUM, ChecksumGenerator.checksum("HACKATHON2017|" + n + "|100.00|#2&[W<nJ*K\"xO_z"));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjRequest);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
