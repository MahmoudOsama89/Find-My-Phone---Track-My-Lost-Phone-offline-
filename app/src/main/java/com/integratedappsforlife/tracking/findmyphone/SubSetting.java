package com.integratedappsforlife.tracking.findmyphone;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class SubSetting extends AppCompatActivity {
 Button bustopNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_setting);
        bustopNext=(Button)findViewById(R.id.bustopNext);
       // bustopNext.setVisibility(View.GONE);

       try
       {
           LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
           if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
           {
               startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
               MessageSend("This App Use GPS please go Head and enable it");

           }
       }
       catch (Exception ex){}


/*Intent intent=new Intent(this,PhoneVerify.class);
      startActivity(intent);*/

    }

    public void MessageSend(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }



    // save setting
   /* public void WhoFindMe(View view){
        SettingSaved.WhoFindMeTag=0;
        Intent newpage=new Intent(  this,ContactList.class);
        startActivity(newpage);
        bustopNext.setVisibility(View.VISIBLE);
    }*/

    /*public void WhoIFind(View view){
        SettingSaved.WhoFindMeTag=1;
        Intent newpage=new Intent(  this,ContactList.class);
        startActivity(newpage);
        bustopNext.setVisibility(View.VISIBLE);

    }*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
            {
                SaveFindCode();
        }
        return super.onKeyDown(keyCode, event);
    }
// saving use find code
void  SaveFindCode(){
    EditText MyCode=(EditText) findViewById(R.id.EDTpassword);
    if(MyCode.getText().length()>0)
    {   SettingSaved.FindCode=MyCode.getText().toString();
        SettingSaved settingSaved=new SettingSaved(this);
        settingSaved.SaveData();}
}
    public void buNext(View view) {
        SaveFindCode();
        Toast.makeText(this,"Your password has been saved.",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SubSetting.this,MainActivity.class);
        startActivity(intent);
        //this.finish();
    }

    //access to permsions

    //get acces to location permsion



}
