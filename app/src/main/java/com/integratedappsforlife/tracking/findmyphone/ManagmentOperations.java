package com.integratedappsforlife.tracking.findmyphone;

import android.content.Context;
import android.database.Cursor;

import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

public class ManagmentOperations {


     public static String FormatPhoneNumber(String Oldnmber){
         /*MainActivity m = new MainActivity();
         Context con = m.getCon();*/
         try {

          String numberOnly= Oldnmber.replaceAll("[^0-9]", "");

              // phone must begin with '+'
     /*         PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
              Phonenumber.PhoneNumber numberProto = phoneUtil.parse(Oldnmber,);
              int countryCode = numberProto.getCountryCode();
              String na = String.valueOf(numberProto.getNationalNumber());
              Log.i("code", "code " + countryCode);
              Log.i("code", "national number " + na);*/
              return(numberOnly);
          } catch (Exception e) {
              Log.e("Hey","NumberParseException was thrown: " + e.toString());
              System.err.println("NumberParseException was thrown: " + e.toString());
              return ("");
          }


    }

    /*public static String formateCountryCode(String senderNum){
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(senderNum,"");
            int countryCode = numberProto.getCountryCode();
            String na = String.valueOf(numberProto.getNationalNumber());

            Log.i("code", "code " + countryCode);
            Log.i("code", "national number " + na);
            return na;
        }catch (NumberParseException e){
            Log.e("Hey","NumberParseException was thrown: " + e.toString());
            System.err.println("NumberParseException was thrown: " + e.toString());
            try {
                String numberOnly= senderNum.replaceAll("[^0-9]", "");
                return numberOnly;
            }catch (Exception e2){
                Log.e("Exception","Exception "+e.toString());
                return "";
            }
        }

    }*/



    public static String  IsPhoneIn(String PhoneNumber,String ListPhone){  // return index of phone if avaulbe true
        String IsFound=null;
        /*if(PhoneNumber.indexOf('+') >=0 ){
            PhoneNumber =  PhoneNumber.replace("+","00");
        }*/
        PhoneNumber =FormatPhoneNumber(PhoneNumber);
        Log.e("boot_broadcast_poc", "phones are: "+ListPhone);
        Log.e("boot_broadcast_poc", "edited sender "+PhoneNumber);
        // Log.e("boot_broadcast_poc", "phon: "+PhoneNumberUtils.compare(PhoneNumber, MyPhones[i]));
        String[] MyPhones = ListPhone.split(":");

        for(int i=0;i<MyPhones.length;i++ ) {

            Log.e("boot_broadcast_poc", "phon: "+PhoneNumberUtils.compare(PhoneNumber, MyPhones[i]));
            Log.e("boot_broadcast_poc", "i@: "+i);
            if(//(PhoneNumber.contains(MyPhones[i])  || MyPhones[i].contains(PhoneNumber))&& MyPhones[i].length()>0
                    PhoneNumberUtils.compare(PhoneNumber, MyPhones[i]) == true

                    )
            {
                //Toast.makeText(this, "Userphone="+MyPhones[i], Toast.LENGTH_SHORT).show();
                Log.e("boot_broadcast_poc", "hey: "+MyPhones[i]);
                //Log.e("boot_broadcast_poc", "isFound"+IsFound);
                IsFound=MyPhones[i];
                break;
            }
        }
        Log.e("boot_broadcast_poc", "isFound "+IsFound);
        return(IsFound);
    }






    public static boolean IsPhoneAut(String PhoneNumber,String ListPhone){  // return index of phone if avaulbe true
    boolean IsFound=false;
        /*if(PhoneNumber.indexOf('+') >=0 ){
            PhoneNumber =  PhoneNumber.replace("+","00");
        }*/
        PhoneNumber =FormatPhoneNumber(PhoneNumber);
        Log.e("boot_broadcast_poc", "phones are: "+ListPhone);
        Log.e("boot_broadcast_poc", "edited sender "+PhoneNumber);
       // Log.e("boot_broadcast_poc", "phon: "+PhoneNumberUtils.compare(PhoneNumber, MyPhones[i]));
        String[] MyPhones = ListPhone.split(":");

        for(int i=0;i<MyPhones.length;i++ ) {

            Log.e("boot_broadcast_poc", "phon: "+PhoneNumberUtils.compare(PhoneNumber, MyPhones[i]));
            Log.e("boot_broadcast_poc", "i@: "+i);
            if(//(PhoneNumber.contains(MyPhones[i])  || MyPhones[i].contains(PhoneNumber))&& MyPhones[i].length()>0
            PhoneNumberUtils.compare(PhoneNumber, MyPhones[i]) == true

            )
            {
                //Toast.makeText(this, "Userphone="+MyPhones[i], Toast.LENGTH_SHORT).show();
                Log.e("boot_broadcast_poc", "hey: "+MyPhones[i]);
                //Log.e("boot_broadcast_poc", "isFound"+IsFound);
                IsFound=true;
                break;
            }
        }
        Log.e("boot_broadcast_poc", "isFound "+IsFound);
        return(IsFound);
    }





    public static   String NumberToName(Context context,String number){

        String UsernName=number ;
        try {


        Cursor cursor = context.getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);

        while (cursor.moveToNext()) {
            String name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

           String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber= ManagmentOperations.FormatPhoneNumber(phoneNumber);
if ( phoneNumber.contains(number) || number.contains(phoneNumber) ){
    UsernName= name;
    break;
}
        }
        }catch (Exception ex){}

        return UsernName;
    }

}
