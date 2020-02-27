package com.example.smarthomebudget;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.ByteArrayOutputStream;


public class addbill extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private ImageView img;
    Button add,take;
    SQLiteHelper helper;
    SQLiteDatabase db;
    private Bundle b;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v=inflater.inflate(R.layout.fragment_addbill, container, false);
        img=(ImageView)v.findViewById(R.id.bill);
        take=(Button)v.findViewById(R.id.takeimge);
        add=(Button)v.findViewById(R.id.add);
        helper=new SQLiteHelper(getContext());
        db=helper.getReadableDatabase();
        mAdView = v.findViewById(R.id.adView2);
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

        int p4= ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA);;
        if (p4 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);

        }
        getActivity().setTitle("Add Bills");

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(i,REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(img.getDrawable()!=null){
                    try{
                        Bitmap bitmap=(Bitmap)b.get("data");
                       String image=BitMapToString(bitmap);

                       helper.addimage(db,image);
                       img.setImageResource(0);
                       Toast.makeText(getContext(),"Bill Added Successfully !",Toast.LENGTH_LONG).show();


                    }
                    catch (Exception e){

                    }

                }
                else Toast.makeText(getContext(),"Please add a image .",Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }


   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        b=data.getExtras();
        Bitmap bitmap=(Bitmap)b.get("data");
        img.setImageBitmap(bitmap);


    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }



}
