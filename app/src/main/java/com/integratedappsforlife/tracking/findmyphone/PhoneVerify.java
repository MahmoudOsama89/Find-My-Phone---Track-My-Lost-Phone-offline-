package com.integratedappsforlife.tracking.findmyphone;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;



public class PhoneVerify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
    }



    public void BuDismiss(View view) {
        finish();
    }

    public void buVideo(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=hG_Mi43pfOk")));
    }
}
