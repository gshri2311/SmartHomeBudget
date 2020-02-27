package com.example.smarthomebudget;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Date;


public class addincome extends Fragment {

    private SQLiteHelper helper;
    private SQLiteDatabase db;
    private ArrayList<Object> arr_income,arr_income1;
    AutoCompleteTextView inc_src,inc;
    Date date;
    private Button incbutton;
    ArrayAdapter adapter;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_addincome, container, false);

        mAdView = v.findViewById(R.id.adView4);
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
        date=new Date();
       incbutton=(Button)v.findViewById(R.id.inc_button);
        final TextView inc_res=(TextView)v.findViewById(R.id.inc_res);
        helper=new SQLiteHelper(getContext());
        db=helper.getReadableDatabase();

        inc=(AutoCompleteTextView)v.findViewById(R.id.inc);
        inc_src=(AutoCompleteTextView)v.findViewById(R.id.inc_src);





        arr_income=new ArrayList<>();
        arr_income1=new ArrayList<>();


        addincome();

        getActivity().setTitle("Add Income");

        inc.requestFocus();


        incbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amt;
               String src=inc_src.getText().toString();
                if(!inc.getText().toString().equals(""))
                {
                    amt=Integer.parseInt(inc.getText().toString());}
                else
                    amt=0;

                if(amt==0 || src.equals("")){

                    Toast.makeText(getContext(),"Please fill all the fields .",Toast.LENGTH_LONG).show();

                }
                else
                {
                    helper.addincome(db,amt,src);
                    inc_res.setText("Income Added Successfully !");
                    inc.setText("");
                    inc_src.setText("");

                    inc.requestFocus();
                    addincome();
                }
            }
        });
        return v;
    }


    void addincome(){

        Cursor e = helper.income_report(db);

        arr_income.clear();
        arr_income1.clear();
        e.moveToFirst();
        while(e.moveToNext()) {
            if(!arr_income.contains(e.getString(0))){
                arr_income.add(e.getString(0).toString());
                arr_income1.add(e.getString(1).toString());}

        }
        e.close();

        adapter = new
                ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arr_income1);
        inc_src.setAdapter(adapter);
        inc_src.setThreshold(1);


    }


}
