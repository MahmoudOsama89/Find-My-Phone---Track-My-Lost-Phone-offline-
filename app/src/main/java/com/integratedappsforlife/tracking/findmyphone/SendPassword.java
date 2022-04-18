package com.integratedappsforlife.tracking.findmyphone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SendPassword extends AppCompatActivity {
    String countryCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_password);
        EditText phoneNumber = (EditText)findViewById(R.id.phone_et);
        EditText password = (EditText)findViewById(R.id.password);
        Button button = (Button) findViewById(R.id.verify_btn);
        String sPhoneNumber = phoneNumber.getText().toString();
        String sPassword = password.getText().toString();
        Context con = this;
        SettingSaved settingSaved = new SettingSaved(this);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (sPhoneNumber.matches("") || sPassword.matches("")) {
                    Toast.makeText(con, "You did not enter either phone number or password", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    if(settingSaved.loadPhoneNumber()!=null){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + sPhoneNumber));
                        intent.putExtra("sms_body", getString(R.string.sentLocationHead)+" "+sPassword+" ("+settingSaved.loadPhoneNumber()+") "+getString(R.string.sentLocationTail));
                        startActivity(intent);}

                }
            }
        });
    }
}
