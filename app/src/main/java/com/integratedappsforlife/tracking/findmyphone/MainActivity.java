package com.integratedappsforlife.tracking.findmyphone;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
//import androidx.core.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity  {
    String phoneNumber;
    //private  LinearLayout  adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        runSmSRetriver();
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);

        // This code requires one time to get Hash keys do comment and share key
        Log.d("NewTRY", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));
        setContentView(R.layout.activity_main);

        AdView adView =(AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
        ListView list = (ListView) findViewById(R.id.listView);


        list.setAdapter(new VivzAdapter(this));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newpage;
                switch (position) {
                    case 0:
                        dispalyText();
                        LoadAdmob();
                        newpage = new Intent(MainActivity.this, FindMyphone.class);
                        startActivity(newpage);

                        break;
                    case 1:


                        newpage = new Intent(MainActivity.this, TrustedList.class);
                        startActivity(newpage);
                        break;

                    case 2:

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(getResources().getString(R.string.sharemessage) + " "+getString(R.string.appShortLink)));
                        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.ShareUse)));
                        break;

                    case 3:
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(myAppLinkToMarket);
                        } catch (ActivityNotFoundException e) {
                            MessageSend(" unable to find market app");
                        }
                        break;

                    case 4:
                        dispalyText();
                        LoadAdmob();
                        newpage = new Intent(MainActivity.this, SendPassword.class);
                        startActivity(newpage);
                        break;
                    case 5:
                        dispalyText();
                        LoadAdmob();
                        newpage = new Intent(MainActivity.this, SubSetting.class);
                        startActivity(newpage);

                }



            }
        });



    }

    private void runSmSRetriver() {
        try {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // API successfully started
                Log.d("NewTRY","Prehere success");
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Fail to start API
                Log.d("NewTRY","Prehere fail");

            }
        });


    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    void dispalyText(){

        Toast.makeText(this,"Ads will be loaded",Toast.LENGTH_LONG).show();
    }






    public  String getContactName(Context context, String phoneNumber) {
        Log.e("hey","Hey");
        Log.e("context","context "+context);
       // readContactPer();
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED ){
                Log.e("hey3","Hey3");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS}
                ,225);
                Log.e("hey4","Hey4");
                return null;

            }
        }
        Log.e("hey1","hey2");
        Log.e("phone","phoneNum "+phoneNumber);
        String contactName = ManagmentOperations.NumberToName(context,phoneNumber);
        Log.e("yse","yse");
        Log.e("return","return "+contactName);
        return contactName;
    }


public void MessageSend(String msg){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
}


@Override
    protected void onResume() {
         super.onResume();


     }
    InterstitialAd mInterstitialAd;
    private  void    LoadAdmob(){
        Context con = this;
        try{
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getResources().getString(R.string.Pop_ad_unit_id)
                     );
            AdRequest adRequest = new AdRequest.Builder()
                    //  .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {

                }

                @Override
                public void onAdLoaded() {
                    DisplayAdmob();
                }
            });
        } catch (Exception ex) {
        }
    }
    private void DisplayAdmob() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }else{
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }

    }

    // @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
           // onQuitPressed();


        }

        return super.onKeyDown(keyCode, event);
    }






    /// liste item

    class VivzAdapter extends BaseAdapter
    {


        ArrayList<ListItem> list ;
        Context context;
        VivzAdapter (Context c)
        {
            context=c;


            list=new ArrayList<ListItem>();
            list.add(new ListItem(getResources().getString(R.string.FindTitle2), getResources().getString(R.string.FindDesc) ,R.drawable.dmapsmall));
            list.add(new ListItem(getResources().getString(R.string.HelpTitle), getResources().getString(R.string.HelpDesc2) ,R.drawable.helpmep ));
            list.add(new ListItem(getResources().getString(R.string.ShareTitle),  getResources().getString(R.string.ShareDesc) ,R.drawable.newaccount ));
            list.add(new ListItem(getResources().getString(R.string.RateTitle),getResources().getString(R.string.RateDesc),R.drawable.rsz_download));
            //list.add(new ListItem(getResources().getString(R.string.RateTitle),getResources().getString(R.string.RateDesc),R.drawable.sl));
            list.add(new ListItem(getResources().getString(R.string.sendSmsPassword),  getResources().getString(R.string.sendSmsPasswordDetail) ,R.drawable.sms));
            list.add(new ListItem(getResources().getString(R.string.SettingTitle),  getResources().getString(R.string.SettingDesc2) ,R.drawable.settingp));
            //list.add(new ListItem("ads",  "ads display" ,R.drawable.settingmap));


        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater =( LayoutInflater ) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ListItem temp=list.get(i);
           // if(!temp.Title.equals("ads")) { // isnot ads
                View row= inflater.inflate( R.layout.singlerow,viewGroup,false );


                TextView title=(TextView) row.findViewById(R.id.textView1);
                TextView detals=(TextView) row.findViewById(R.id.textView2);
                ImageView image =(ImageView) row.findViewById(R.id.imageView);


                //runTutorial(row,i);
                title.setText(temp.Title);
                detals.setText(temp.Detals);// it updated
                image.setImageResource(temp.ImageURL);

                return row;





        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 7:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED

                        ) {

                } else {
                    // Permission Denied

                    Toast.makeText( this, getResources().getString(R.string.DenailMessage), Toast.LENGTH_SHORT)
                            .show();

                }
                return;
            case 225:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContactName(this, phoneNumber);
                } else {
                    // Permission Denied

                    Toast.makeText( this, getResources().getString(R.string.DenailMessage), Toast.LENGTH_SHORT)
                            .show();

                }
                return;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }








}
