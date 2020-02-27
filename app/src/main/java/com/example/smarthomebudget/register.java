package com.example.smarthomebudget;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class register extends AppCompatActivity {


    private static final int GALLERY_REQUEST_CODE = 1;
    private EditText name,phone,email,pin;
    private ImageView pimg;
    private Button reg;
    private TextView tv0;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    int profile;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);



        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        profile=pref.getInt("profile",0);
        name=(EditText)findViewById(R.id.name);
        tv0=(TextView)findViewById(R.id.tvv);
        phone=(EditText)findViewById(R.id.phone);
        email=(EditText)findViewById(R.id.email);
        reg=(Button)findViewById(R.id.reg);
        pin=(EditText)findViewById(R.id.pin);
        pimg=(ImageView)findViewById(R.id.pimg);
        name.requestFocus();
    profile=pref.getInt("profile",0);

        if(profile==1){
            tv0.setText("Edit Profile");
        }
    }


  public void uploadimg(View view) {
      //Create an Intent with action as ACTION_PICK
      Intent intent=new Intent(Intent.ACTION_PICK);
      // Sets the type as image/*. This ensures only components of type image are selected
      intent.setType("image/*");
      //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
      String[] mimeTypes = {"image/jpeg", "image/png"};
      intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
      // Launching the Intent
      startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                 try{
                     Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                     editor.putString("proimg",BitMapToString(bitmap));
                     editor.commit();
                     pimg.setImageBitmap(bitmap);
                     flag=1;
                     break;
                 }
                 catch (Exception e){
                     break;
                 }

            }
    }


    public void register(View view) {
        if(pin.getText().toString()=="" || name.getText().toString()=="" || phone.getText().toString().isEmpty()||email.getText().toString().isEmpty()||pin.getText().toString().isEmpty()|| flag==0)
        {
            Toast.makeText(this,"Please fill all the fields ...",Toast.LENGTH_LONG).show();
        }
        else {
            editor.putBoolean("registered", true);
            editor.putString("uname",name.getText().toString());
            editor.putString("phone",phone.getText().toString());
            editor.putString("email",email.getText().toString());
            editor.putString("PIN",pin.getText().toString());
            editor.commit();
            if(profile==1){
                editor.putInt("profile",0);
                editor.commit();
                Toast.makeText(this,"Profile Updated ...",Toast.LENGTH_LONG).show();

                Intent i = new Intent(this,home.class);
                startActivity(i);
            }
            else
            {Toast.makeText(this,"Successfully Registered ...",Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, fingerprint_auth.class);
                startActivity(i);
            }

            finish();
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
