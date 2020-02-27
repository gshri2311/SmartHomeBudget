package com.example.smarthomebudget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class profile extends Fragment {
    private SharedPreferences pref;
    SharedPreferences.Editor editor;

    private Button edit;
    private EditText name,phone,email,pin;
    private ImageView pimg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        getActivity().setTitle("Profile");
        name=v.findViewById(R.id.name2);
        phone=v.findViewById(R.id.phone2);
        pin=v.findViewById(R.id.pin2);
        email=v.findViewById(R.id.email2);
        pimg=v.findViewById(R.id.pimg2);
        edit=v.findViewById(R.id.edit);

        editor.putInt("profile",1);
        editor.commit();
        name.setText(pref.getString("uname",""));
        name.setEnabled(false);
        phone.setText(pref.getString("phone",""));
        phone.setEnabled(false);
        email.setText(pref.getString("email",""));
        email.setEnabled(false);
        pin.setText(pref.getString("PIN",""));
        pin.setEnabled(false);
        pimg.setImageBitmap(StringToBitMap(pref.getString("proimg","0101010101")));
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home=new Intent(getActivity(),register.class);
                startActivity(home);
                getActivity().finish();

            }
        });

        return v;
    }


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
