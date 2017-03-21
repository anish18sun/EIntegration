package com.hypervisor.eintegrator.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hypervisor.eintegrator.R;

/**
 * Created by dexter on 19/3/17.
 */
public class ViewDialog {

    EditText e1 ,e2,e3,e4;
    TextView send , cancel;
    PrefManager pre;
    public void showDialog(final Activity activity, String id){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        e1 = (EditText)dialog.findViewById(R.id.key1);
        e2 = (EditText)dialog.findViewById(R.id.key1);
        e3 = (EditText)dialog.findViewById(R.id.key1);
        e4 = (EditText)dialog.findViewById(R.id.key1);
        send = (TextView)dialog.findViewById(R.id.bOK);
        cancel = (TextView)dialog.findViewById(R.id.bCancel);
        pre = new PrefManager(activity.getApplicationContext());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(e1.getText().toString().isEmpty() || e2.getText().toString().isEmpty() || e3.getText().toString().isEmpty() || e4.getText().toString().isEmpty()){
                   Toast.makeText(activity.getApplicationContext(),"Enter Correct OTP",Toast.LENGTH_LONG).show();
               }else{
                   if((e1.getText().toString() + e2.getText().toString() + e3.getText().toString() + e4.getText().toString()).equals("0000")){
                       // confirm
                       pre.setPaymentMade();
                       dialog.dismiss();
                       activity.finish();
                       Toast.makeText(activity.getApplicationContext(),"Payment is successful",Toast.LENGTH_LONG).show();
                   }else{
                       Toast.makeText(activity.getApplicationContext(),"Enter Correct OTP",Toast.LENGTH_LONG).show();
                   }
               }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();

    }
}