package com.example.smarthomebudget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    SharedPreferences.Editor editor;

    private static int SPLASH_TIME_OUT=8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                editor = pref.edit();

                if(pref.getBoolean("remind",false)){
                    startAlarm(true);
                }
                else
                    startAlarm(false);

                if(pref.getBoolean("lupdate",false))
                {
                    notificationHelper notice=new notificationHelper(getApplicationContext());
                    notice=new notificationHelper(getApplicationContext());
                    notice.createNotification("SmartHomeBudget","Last Updated : "+pref.getString("lastsync","Welcome"));
                }

                if(!pref.getBoolean("pincheck",false) && !pref.getBoolean("finger",false)){
                    Intent i=new Intent(getApplicationContext(),home.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent home = new Intent(getApplicationContext(), fingerprint_auth.class);
                    startActivity(home);
                    finish();
                }
            }





            private void startAlarm(boolean set) {

                notificationHelper notice = new notificationHelper(getApplicationContext());

                AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent myIntent;
                PendingIntent pendingIntent = null;

                // SET TIME HERE
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                myIntent = new Intent(MainActivity.this,AlarmNotificationReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);


                if(set){

                    manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() +
                                    60*60*24 * 1000, pendingIntent);

                }
                else
                if (manager!= null) {
                    manager.cancel(pendingIntent);
                }
            }
        },SPLASH_TIME_OUT);








    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           finish();
        }
    }

}


