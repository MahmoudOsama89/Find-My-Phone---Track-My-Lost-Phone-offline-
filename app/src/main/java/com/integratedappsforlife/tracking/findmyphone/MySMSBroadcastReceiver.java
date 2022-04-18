package com.integratedappsforlife.tracking.findmyphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;

public class MySMSBroadcastReceiver extends BroadcastReceiver {
    static int NotifyID=1;
    private String senderNum;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NewTRY","first thing");
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Log.d("NewTRY","2 thing");
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            Log.d("NewTRY","3 thing");

            switch(status.getStatusCode()) {
               // Log.d("NewTRY",status.getStatusCode()+" "+ status.getStatusMessage());
                case CommonStatusCodes.SUCCESS:
                    //Toast.makeText(context,"Yes",Toast.LENGTH_LONG).show();
                    // Get SMS message contents
                    Log.d("NewTRY","Only success");
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    senderNum = message.substring(message.indexOf('(') + 1, message.indexOf(')'));
                    if (   (( ManagmentOperations.IsPhoneAut(senderNum,SettingSaved.UserPhoneNumber)==true) && (message.contains(context.getString(R.string.requestLocationCode)))) || ((message.contains(SettingSaved.FindCode)) &&(!SettingSaved.FindCode.equals("No_code")) ) )
                    {
                        SettingSaved settingSaved=new SettingSaved(context);
                        settingSaved.sendm( senderNum );
                    } else if ( message.contains("%") ) {
                            String Location = message.substring(message.indexOf('*') + 1, message.indexOf('%'));
                            Log.e("boot_broadcast_poc", "Iam another deep still working...");
                            String Myinfo=message.substring(1,message.length()-1);
                            if (Myinfo != null) {
                                SettingSaved.Userlocationinthemap = context.getString(R.string.sentLocationHeadText)+Location+context.getString(R.string.sentLocationTailText) +"%"+ senderNum ;
                                try {

                                    NewMessageNotification tn=new NewMessageNotification();
                                    tn.notify(context, ManagmentOperations.NumberToName(context,senderNum),message ,NotifyID,context.getString(R.string.notificationMessage),null,null);
                                    NotifyID++;

                                } catch (Exception e) {

                                    //e.printStackTrace();
                                }

                            }else{

                            }

                    }else {Log.e("boot_broadcast_poc", "new wrong1 ...");}



        Log.d("NewTRY",message);
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...

                    Log.d("NewTRY","4 thing");
                    try {
                    SmsRetrieverClient client = SmsRetriever.getClient(context);
                    Task<Void> task = client.startSmsRetriever();
                    task.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // API successfully started
                            Log.d("NewTRY","inside Prehere success");
                        }
                    });
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Fail to start API
                            Log.d("NewTRY","inside Prehere fail");

                        }
                    });
                    Log.d("NewTRY","5 thing");}catch (Exception e) {
                        e.printStackTrace();
                        SmsRetrieverClient client = SmsRetriever.getClient(context);
                        Task<Void> task = client.startSmsRetriever();
                    }
                    break;
            }
        }
    }
}