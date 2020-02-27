package com.example.smarthomebudget;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.ads.mediation.AbstractAdViewAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class home1 extends Fragment {

    TextView tdate,tinc,texp,mcat,mitem,sav;
    SQLiteHelper helper;
    SQLiteDatabase db;
    String saving;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home1, container, false);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = v.findViewById(R.id.adView);
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

        helper = new SQLiteHelper(getContext());
        db = helper.getReadableDatabase();
        tdate=(TextView)v.findViewById(R.id.tdate);
        tinc=(TextView)v.findViewById(R.id.tinc);
        texp=(TextView)v.findViewById(R.id.texp);
        sav=(TextView)v.findViewById(R.id.sav);
        mcat=(TextView)v.findViewById(R.id.mcat);
        mitem=(TextView)v.findViewById(R.id.mitem);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        String date = dateFormat.format(calendar.getTime());

        try {
            tdate.setText("Date : " + date);
            tdate.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            tinc.setText("Total Income : " + getincome());
            tinc.setTextColor(getResources().getColor(R.color.green));

            texp.setText("Total Expense : " + getexpense());
            texp.setTextColor(getResources().getColor(R.color.red));

            sav.setText("Total Saving : " + saving);
            if (Integer.valueOf(saving) < 0)
                sav.setTextColor(getResources().getColor(R.color.red));
            else
                sav.setTextColor(getResources().getColor(R.color.green));

            mcat.setText("Maximum Spent Catagory : " + getcat());
            mitem.setText("Maximum Spent Item : " + getitem());

        }catch (Exception e){}

        return v;
    }

    private String getitem() {
        Cursor c = helper.getmitem(db);

        String expense="";
        while(c.moveToNext()) {
            expense=c.getString(0)+"\nAmount : "+c.getString(1);

        }


        return expense;
    }

    private String getcat() {
        Cursor c = helper.getmcat(db);

        String expense="";
        while(c.moveToNext()) {
            expense=c.getString(0)+"\nAmount : "+c.getString(1);

        }


        return expense;
    }

    private String getexpense() {
        Cursor c = helper.gettexpense(db);

        String expense="";
        while(c.moveToNext()) {
            expense=c.getString(0);

        }
        if(saving==null && expense==null){
            saving=null;
        }
        else if(saving==null)
            saving='-'+expense;
        else
        saving=String.valueOf(Integer.valueOf(saving)-Integer.valueOf(expense));


        return expense;
    }

    private String getincome() {
        Cursor c = helper.gettincome(db);

        String income="";
        while(c.moveToNext()) {
            income=c.getString(0);

        }
        saving=income;
        return income;
    }


}
