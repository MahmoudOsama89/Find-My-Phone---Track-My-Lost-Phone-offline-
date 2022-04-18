package com.integratedappsforlife.tracking.findmyphone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;

import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.messages.TextMessage;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;

import static com.integratedappsforlife.tracking.findmyphone.AppSignatureHashHelper.TAG;


public class SettingSaved extends Activity {
    public static String UserPhoneNumber="";
    public static String UserTrackedNumber="";
    // this varible find user location
    public static String Userlocationinthemap="77:43";
    public static final String MyPREFERENCES = "MyPrefsFindPhone" ;
    /// this varible for loc for my locations
    public static boolean ISLocForLocation=false ;

    public static Map<String,String> WhoFindMeIN=new HashMap<String,String>();
    public static    Map<String,String> WhoIFindIN=new HashMap<String,String>();
    public static int WhoFindMeTag=0;  // 0 for who find me // 1 for who i find
    public static String FindCode="No_code";
    public  static  int IsRated=0;
    Context context;
    SharedPreferences sharedpreferences;
    public String senderPhoneNumb;
    public FirebaseAuth auth;
    public NexmoClient client;
    public  SettingSaved(Context context) {
        //Location v = getLocation(context);
        this.context=context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         client = NexmoClient.builder().apiKey(context.getString(R.string.nexmoApiKey)).apiSecret(context.getString(R.string.nexmoApiSecret)).build();

        //getLocation(context);
       // Toast.makeText(context,"function return "+v,Toast.LENGTH_LONG);
    }
    public  void sendm(String senderNum){
        senderPhoneNumb = senderNum;
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED){



                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        5

                );return;}
        }
        Location lastloc=SettingSaved.getLocation(context);
        if (lastloc != null && loadPhoneNumber() !=null) {
            TextMessage message = new TextMessage("Find My phone",
                    formatE164Number(senderNum),
                    context.getString(R.string.sentLocationHead)+" *" + lastloc.getLatitude() + "," + lastloc.getLongitude() +"%  ("+loadPhoneNumber()+") "+ context.getString(R.string.sentLocationTail)
            );

            SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                System.out.println("Message sent successfully.");
            } else {
                System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
            }


        }else{
            Toast.makeText(this,"There is something wrong Please check your permissions and GPS",Toast.LENGTH_LONG);

        }
    }


    public String formatE164Number(String phNum) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = manager.getNetworkCountryIso();
        String e164Number;
        if (TextUtils.isEmpty(countryCode)) {
            e164Number = phNum;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                e164Number = PhoneNumberUtils.formatNumberToE164(phNum, countryCode);
            } else {
                try {
                    PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber phoneNumber = instance.parse(phNum, countryCode);
                    e164Number = instance.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

                } catch (NumberParseException e) {
                    Log.e(TAG, "Caught: " + e.getMessage(), e);
                    e164Number = phNum;
                }
            }
        }

        return e164Number;
    }
   /* public  void sendForVer(String senderNum , String text ){

            SmsManager smsManagersend = SmsManager.getDefault();
            smsManagersend.sendTextMessage(senderNum, null, text, null, null);
        }*/
         //   Toast.makeText(this,"There is something wrong Please your phone number is wrong",Toast.LENGTH_LONG);




    public  void SaveData( )  {

        //get list of user authorize to find me
        String StringWhoFindMeIN="";
        for(Map.Entry m:WhoFindMeIN.entrySet())
           {
                StringWhoFindMeIN = StringWhoFindMeIN  + m.getKey() + "%" + m.getValue()+ "%";

           }
        //==================

        /// save list of who i could find.
        String StringWhoColudIfind="";
        for(Map.Entry m:WhoIFindIN.entrySet())
        {
                StringWhoColudIfind = StringWhoColudIfind + m.getKey()+"%"+m.getValue() + "%" ;
        }

        //save data
        try

        {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("StringWhoFindMeIN","");
            //PUT PHONE NUMBER OF USER
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("WhoFindMeIN",StringWhoFindMeIN);
            editor.putString("WhoCouldIFind",StringWhoColudIfind);
            editor.putString("FindCode",FindCode);
            editor.putInt("IsRated",IsRated);
            //Map<String,Object> map = new HashMap<String, Object>();
           // map.put(who,"");
            //root.updateChildren(map);
            editor.commit();
            LoadData( );
        }

        catch( Exception e)

        {

        }
        // Show Alert
          Toast.makeText(context, context.getResources().getString(R.string.UpdateMsg) , Toast.LENGTH_LONG).show();


}


    public void savePhoneNumber(String phoneNumber){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("OwnerPhoneNumber",phoneNumber);
        editor.commit();
    }

    public String loadPhoneNumber(){
        String OwnerPhoneNumber= sharedpreferences.getString("OwnerPhoneNumber", "empty");
        if(!OwnerPhoneNumber.equals("empty")) {
            return OwnerPhoneNumber;
        }else{
            return null;
        }
    }
    public  void LoadData(  ) {


        try {
// who could find me
           String StringWhoFindMeIN= sharedpreferences.getString("WhoFindMeIN", "empty");

            String AlowsTrackPhone="";
            String TrackedNum = "";
            //Toast.makeText(context,"long berfore",Toast.LENGTH_LONG).show();

            if(!StringWhoFindMeIN.equals("empty")) {
                //Toast.makeText(context,"!StringWhoFindMeIN.equals(\"empty\")",Toast.LENGTH_LONG).show();
                String[] WhoFindMe = StringWhoFindMeIN.split("%");

                WhoFindMeIN.clear();
                //Toast.makeText(context,"UserPhoneNumber"+AlowsTrackPhone,Toast.LENGTH_LONG).show();
                if (WhoFindMe.length>1) //avoid error
                for (int i = 0; i < WhoFindMe.length; i = i + 2) {
                    WhoFindMeIN.put(WhoFindMe[i], WhoFindMe[i + 1]);

                    AlowsTrackPhone = AlowsTrackPhone  + WhoFindMe[i]+ ":"; // load list find my phone

                   // Toast.makeText(context,"UserPhoneNumber"+AlowsTrackPhone,Toast.LENGTH_LONG).show();
                }

                UserPhoneNumber = AlowsTrackPhone;
                //Toast.makeText(context,"UserPhoneNumber"+UserPhoneNumber,Toast.LENGTH_LONG).show();
               // sendM("Phone numbers from SettingSaved="+UserPhoneNumber);

            }else{

            }

            //who i find
            String StringWhoIFind= sharedpreferences.getString("WhoCouldIFind", "empty");
            if(!StringWhoIFind.equals("empty")) {
                //Toast.makeText(context,"!StringWhoIFind.equals(\"empty\")",Toast.LENGTH_LONG).show();
                String[] WhoIFind = StringWhoIFind.split("%");
                WhoIFindIN.clear();
                if (WhoIFind.length>1) //avoid error
                for (int i = 0; i < WhoIFind.length; i = i + 2){
                    WhoIFindIN.put(WhoIFind[i], WhoIFind[i + 1]);
                    TrackedNum = TrackedNum  + WhoIFind[i]+ ":";}

                UserTrackedNumber = TrackedNum;
            }

            String FindCodeMe=sharedpreferences.getString("FindCode", "empty");
            if(!FindCodeMe.equals("empty")){
                //Toast.makeText(context,"FindCodeMe IS NOT EMPTY",Toast.LENGTH_LONG).show();
                FindCode=sharedpreferences.getString("FindCode", "empty");}

            // load ofr first time
            if(FindCodeMe.equals("empty")&&StringWhoIFind.equals("empty")&& StringWhoFindMeIN.equals("empty"))
            {
                //Toast.makeText(context,"another berfore",Toast.LENGTH_LONG).show();
                /*Intent imap = new Intent(context,SubSetting.class);
                imap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(imap);*/
                //Toast.makeText(context,"another after",Toast.LENGTH_LONG).show();
            }

            IsRated=sharedpreferences.getInt("IsRated", 0);
        } catch (Exception e) {

        }
    }
    final private static int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static Location getLocation(final Context conetxt) {
        MyLocationListener loctionLis = new MyLocationListener(conetxt);


       try{

           LocationManager locationManager = (LocationManager) conetxt.getSystemService(Context.LOCATION_SERVICE);
           if (locationManager != null) {
               Criteria  criteria = new Criteria();
               criteria.setAccuracy(Criteria.ACCURACY_FINE);
               criteria.setCostAllowed(false);
               String  provider = String.valueOf(locationManager.getBestProvider(criteria, false)).toString();
               Location lastKnownLocationGPS = locationManager.getLastKnownLocation(provider);
               //(LocationManager.GPS_PROVIDER);
               Log.e("boot_broadcast_poc", "Provider is "+provider);
               if (lastKnownLocationGPS != null) {
                   //locationManager.removeLocationUpdates();
                   Log.e("boot_broadcast_poc", "lastKnownLocationGPS");
             //      locationManager.removeUpdates(loctionLis);
                   return lastKnownLocationGPS;

               } else {
                   Log.e("boot_broadcast_poc", "lastKnownLocationGPS is null");
                   Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                 //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, loctionLis);
                   Log.e("boot_broadcast_poc", "location is "+loc);

               //    locationManager.removeUpdates(loctionLis);
                   return loc;
               }


           } else {
               //locationManager.removeUpdates(loctionLis);
               //locationManager.removeUpdates(loctionLis);
               return null;
           }


       }
       catch (Exception ex){return null;}
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    sendm(senderPhoneNumb);

                } else {
                    Toast.makeText(context, "You denied applicatin to send your location. application will do it's function", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 5: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //getLocation(context);
                    sendm(senderPhoneNumb);

                } else {
                            Toast.makeText(context,"You denied applicatin to access your location. application will do it's function",Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
