package com.example.smarthomebudget;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class viewbill extends Fragment{

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private ImageView img;
    Button add,take;
    SQLiteHelper helper;
    SQLiteDatabase db;
    private Bundle b;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v=inflater.inflate(R.layout.fragment_viewbill, container, false);

        getActivity().setTitle("View Bills");
        helper=new SQLiteHelper(getContext());
        db=helper.getReadableDatabase();
     setimage();

        return v;
    }

    void setimage(){

        int tot;
        Cursor c=helper.getimage(db);
        tot=c.getCount();
        Bitmap image[]=new Bitmap[c.getCount()];
        final String imagedat[]=new String[c.getCount()];
        ImageView imgview[]=new ImageView[c.getCount()];
        int co=0;
        while(c.moveToNext()) {
            image[co]=StringToBitMap(c.getString(0));
            image[co]=Bitmap.createScaledBitmap(image[co], 1000,1500, true);
            imagedat[co++]=c.getString(1);

        }


        LinearLayout ll = (LinearLayout) v.findViewById(R.id.layout);
        ll.removeAllViews();
        int ab=0;
        while (ab<tot) {
            imgview[ab]=new ImageView(getContext());
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imgview[ab].setLayoutParams(p);
            imgview[ab].setImageBitmap(image[ab]);
            p.setMargins(10, 50, 10, 0);
            imgview[ab].setId(Integer.valueOf(ab));
            ll.addView(imgview[ab]);
            ab++;
        }


        for(int i=0;i<tot;i++)
        {
            final int j=i;
            imgview[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    delete_image(imagedat[j]);
                    return true;
                }
            });
        }

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

    public void delete_image(final String s) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("Do you want to delete the item ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                helper.delete_image(db,s);
                setimage();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
