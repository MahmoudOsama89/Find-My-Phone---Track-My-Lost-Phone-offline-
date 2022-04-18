package com.cd.statussaver.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.cd.statussaver.R;
import com.cd.statussaver.api.CommonClassForAPI;
import com.cd.statussaver.databinding.ActivityRoposoBinding;
import com.cd.statussaver.util.AdsUtils;
import com.cd.statussaver.util.AppLangSessionManager;
import com.cd.statussaver.util.SharePrefs;
import com.cd.statussaver.util.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;
import static com.cd.statussaver.util.Utils.RootDirectoryRoposo;
import static com.cd.statussaver.util.Utils.createFileFolder;
import static com.cd.statussaver.util.Utils.startDownload;

public class RoposoActivity extends AppCompatActivity {
    private ActivityRoposoBinding binding;
    RoposoActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_roposo);
        activity = this;
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

        AdsUtils.showGoogleBannerAd(activity, binding.adView);
        InterstitialAdsINIT();

    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.imInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
            }
        });

        Glide.with(activity)
                .load(R.drawable.r1)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity)
                .load(R.drawable.r2)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity)
                .load(R.drawable.r1)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity)
                .load(R.drawable.r2)
                .into(binding.layoutHowTo.imHowto4);
        binding.layoutHowTo.tvHowToHeadOne.setVisibility(View.GONE);
        binding.layoutHowTo.LLHowToOne.setVisibility(View.GONE);
        binding.layoutHowTo.tvHowToHeadTwo.setText(getResources().getString(R.string.how_to_download));

        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_roposo));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.cop_link_from_roposo));




        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOROPOSO)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOROPOSO, true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }


        binding.loginBtn1.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                Utils.showProgressDialog(activity);
                GetRoposoData();
            }
        });

        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });

        binding.LLOpenTwitter.setOnClickListener(v -> {
            Utils.OpenApp(activity, "com.roposo.android");
        });
    }

    private void GetRoposoData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            if (host.contains("roposo")) {
                Utils.showProgressDialog(activity);
                new callGetRoposoData().execute(binding.etText.getText().toString());
                showInterstitial();
            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("roposo")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("roposo")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
                showInterstitial();
            } else {
                if (CopyIntent.contains("roposo")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class callGetRoposoData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            try {
                RoposoDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return RoposoDoc;
        }

        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog(activity);
            try {
                VideoUrl = result.select("meta[property=\"og:video\"]").last().attr("content");
                if (VideoUrl==null || VideoUrl.equals("")){
                    VideoUrl = result.select("meta[property=\"og:video:url\"]").last().attr("content");
                }
                Log.e("onPostExecute: ", VideoUrl);
                if (!VideoUrl.equals("")) {
                    try {
                        startDownload(VideoUrl, RootDirectoryRoposo, activity, "roposo_"+System.currentTimeMillis() + ".mp4");
                        VideoUrl = "";
                        binding.etText.setText("");

                        showInterstitial();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }


    //InterstitialAd : Start

    public void InterstitialAdsINIT(){

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });


        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_ad));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }


    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    //InterstitialAd : End



}