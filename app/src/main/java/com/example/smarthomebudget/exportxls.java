package com.example.smarthomebudget;


import android.app.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;


public class exportxls extends Fragment {

    private RewardedAd rewardedAd;
    TextView tv111;
    SQLiteHelper helper;
    SQLiteDatabase db;
    String from1,to1,today,totincome,totexpense,max,max1;
    EditText from,to;
    Button get;
    int COH;
    Timestamp timestamp;
    ArrayList<String> arr_item, arr_expense, arr_cat, arr_income,arr_amt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_exportxls, container, false);

        from=(EditText)v.findViewById(R.id.from111);
        to=(EditText)v.findViewById(R.id.to111);
        get=(Button)v.findViewById(R.id.get111) ;
        tv111=(TextView)v.findViewById(R.id.tv111);

        arr_item=new ArrayList<>();
        arr_expense=new ArrayList<>();
        arr_cat=new ArrayList<>();
        arr_amt=new ArrayList<>();
        arr_income=new ArrayList<>();

        helper = new SQLiteHelper(getContext());
        db = helper.getReadableDatabase();

        timestamp = new Timestamp(System.currentTimeMillis());
        today=timestamp.toString().substring(0,11);

        tv111.setText("Watch Ad to use Premium feature...");
        tv111.setTextSize(20);
        tv111.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from1=from.getText().toString();
                to1=to.getText().toString();
                if (!from1.equals("") && !to1.equals(""))
                {
                    getincome(from1,to1);
                    load();
                    create_spreadsheet();
                    Toast.makeText(getContext(),"Exported Successfully !", Toast.LENGTH_LONG).show();
                    exported();

                }
                else
                    Toast.makeText(getContext(), "Please enter from and to date", Toast.LENGTH_LONG).show();

            }
        });




        return v;
    }

    private void create_spreadsheet() {

        Workbook wb=new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Report");


        Row row = sheet.createRow(0);
        row.createCell(5).setCellValue("INCOME EXPENSE STATEMENT FOR THE PERIOD " + from1 +" to " + to1 );
        row = sheet.createRow(3);

        row.createCell(2).setCellValue("Total Income ");
        row.createCell(3).setCellValue(totincome);
        row.createCell(6).setCellValue("Total Expense ");
        row.createCell(7).setCellValue(totexpense);
        row.createCell(10).setCellValue("Cash On Hand ");
        row.createCell(11).setCellValue(String.valueOf(COH));


        row = sheet.createRow(7);
        row.createCell(0).setCellValue("Expenses :- ");


        row = sheet.createRow(9);
        row.createCell(1).setCellValue("ITEM");
        row.createCell(3).setCellValue("CATEGORY");
        row.createCell(5).setCellValue("PRICE");
        row.createCell(7).setCellValue("PERCENT");

        int r=11;

       for(String s:arr_item){
           row=sheet.createRow(r++);
           row.createCell(1).setCellValue(s);
       }

       r=11;
        for(String s:arr_cat){
            row.getSheet().getRow(r++);
            row.createCell(3).setCellValue(s);
        }

        r=11;
        for(String s:arr_expense){
            row.getSheet().getRow(r++);
            row.createCell(5).setCellValue(s);
        }

        r=11;
        for(String s:arr_expense){
            row.getSheet().getRow(r++);
            row.createCell(7).setCellValue(Double.valueOf(s) / Integer.valueOf(totincome)*100);
        }




        r+=5;
        row = sheet.createRow(r);
        row.createCell(0).setCellValue("Income :- ");

        row = sheet.createRow(r+2);
        row.createCell(1).setCellValue("Amount");
        row.createCell(3).setCellValue("Source");
        row.createCell(5).setCellValue("PERCENT");

        int r1=r+4;

        for(String s:arr_amt){
            row=sheet.createRow(r1++);
            row.createCell(1).setCellValue(s);
        }

        r=r+4;
        for(String s:arr_income){
            row.getSheet().getRow(r++);
            row.createCell(3).setCellValue(s);
        }

        r=r+4;
        for(String s:arr_amt){
            row.getSheet().getRow(r++);
            row.createCell(5).setCellValue(Double.valueOf(s) / Integer.valueOf(totincome)*100);
        }



        String fileName = "Report_"+timestamp.toString()+".xlsx"; //Name of the file

        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
        File folder1 = new File(extStorageDirectory, "SmartHome Budget");// Name of the folder you want to keep your file in the local storage.
        folder1.mkdir(); //creating the folder
        File folder2 = new File(folder1, "Report");// Name of the folder you want to keep your file in the local storage.
        folder2.mkdir(); //creating the folder
        File folder = new File(folder2, today);// Name of the folder you want to keep your file in the local storage.
        folder.mkdir(); //creating the folder

        File file = new File(folder, fileName);
        try {
            file.createNewFile(); // creating the file inside the folder
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(file); //Opening the file
            wb.write(fileOut); //Writing all your row column inside the file
            fileOut.close(); //closing the file and done

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    void getincome (String fr, String too){

        try {

            arr_item.clear();
            arr_expense.clear();
            arr_cat.clear();
            arr_income.clear();
            arr_amt.clear();

            Cursor c = helper.income_report_date(db, fr, too);
            Cursor e = helper.expense_report_date(db, fr, too);

            totincome = "0";
            totexpense = "0";
            COH=0;
            while (c.moveToNext()) {
                arr_amt.add(String.valueOf(c.getInt(0)));
                arr_income.add(String.valueOf(c.getString(1)));
                COH+=c.getInt(0);
                totincome = String.valueOf(Integer.valueOf(c.getInt(0) + Integer.valueOf(totincome)));

            }
            max = "0";
            max1 = "0";

            while (e.moveToNext()) {

                arr_item.add(e.getString(0));
                arr_cat.add(e.getString(1));
                arr_expense.add(e.getString(2));
                COH-=e.getInt(2);
                totexpense = String.valueOf(Integer.valueOf(e.getInt(2) + Integer.valueOf(totexpense)));
                if (Integer.valueOf(max1) < Integer.valueOf(e.getString(2))) {
                    max1 = e.getString(2);
                    max = e.getString(0);
                }

            }


        }catch (Exception e){

        }
    }

    public void exported() {
        tv111.setText("Location : \nSmartHome Budget/Report/"+today+"/"+timestamp.toString()+".xlsx");
        tv111.setTextColor(getResources().getColor(R.color.green));

    }

    void load()
    {

        rewardedAd = new RewardedAd(getContext(),"ca-app-pub-7618597715196623/1761548667");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {

                show();
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                Toast.makeText(getContext(),"Failed to load Ad,Check your Internet Connection!",Toast.LENGTH_LONG).show();            }

        };

        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    void show(){
        if (rewardedAd.isLoaded()) {
            Activity activityContext = getActivity();
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {

                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display.
                }
            };
            rewardedAd.show(activityContext, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
        }


    }


}
