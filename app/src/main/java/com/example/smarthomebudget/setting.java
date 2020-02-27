package com.example.smarthomebudget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class setting extends AppCompatActivity {

    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    CheckBox finger,pin,lupdate,remind;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);

        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        pin=(CheckBox)findViewById(R.id.pincheck);
        finger=(CheckBox)findViewById(R.id.fincheck);
        remind=(CheckBox)findViewById(R.id.remind);
        lupdate=(CheckBox)findViewById(R.id.lupdate);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        if(pref.getBoolean("remind",false)){
            remind.setChecked(true);
        }
        else {
            remind.setChecked(false);
        }
        if(pref.getBoolean("lupdate",false)){
            lupdate.setChecked(true);
        }
        else {
            lupdate.setChecked(false);
        }
        if(pref.getBoolean("pincheck",false)){
            pin.setChecked(true);
        }
        else {
            pin.setChecked(false);
        }
        if(pref.getBoolean("finger",false)){
            finger.setChecked(true);
        }
        else {
            finger.setChecked(false);
        }
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pin.isChecked()){
                    editor.putBoolean("pincheck",true);
                    editor.commit();
                }
                else {
                    editor.putBoolean("pincheck",false);
                    editor.commit();
                }

            }
        });

        finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finger.isChecked()){
                    editor.putBoolean("finger",true);
                    editor.commit();
                }
                else {
                    editor.putBoolean("finger",false);
                    editor.commit();
                }

            }
        });
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(remind.isChecked()){
                    editor.putBoolean("remind",true);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Please reopen the app to off notification",Toast.LENGTH_LONG).show();
                }
                else {
                    editor.putBoolean("remind",false);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Please reopen the app to off notification",Toast.LENGTH_LONG).show();
                }

            }
        });
        lupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lupdate.isChecked()){
                    editor.putBoolean("lupdate",true);
                    editor.commit();
                }
                else {
                    editor.putBoolean("lupdate",false);
                    editor.commit();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,home.class));
        finish();
    }
}
