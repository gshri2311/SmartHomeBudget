package com.example.smarthomebudget;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class today extends Fragment {

    ListView simpleList;
    ArrayList<String> arr_income,arr_expense,arr_income1,arr_expense1;
    SQLiteHelper helper;
    SQLiteDatabase db;
    TextView tv;
    int tote,toti;
    private ListView simpleList1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_today, container, false);

        arr_income=new ArrayList<>();
        arr_income1=new ArrayList<>();
        arr_expense=new ArrayList<>();
        arr_expense1=new ArrayList<>();


        getActivity().setTitle("Today");
     tv=(TextView)v.findViewById(R.id.ttttv);
        simpleList = (ListView) v.findViewById(R.id.simpleListView2);
        simpleList1=(ListView)v.findViewById(R.id.simpleListView3);
        final ScrollView sv = (ScrollView) v.findViewById(R.id.sv);
        helper=new SQLiteHelper(getContext());
        db=helper.getReadableDatabase();
        getincome();
        tv.setText("Total Income : "+toti+"\nTotal expense : "+tote);


        simpleList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sv.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        sv.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        simpleList1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sv.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        sv.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
      simpleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
              delete_income(i);
                     return true;
            }
        });

        simpleList1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                delete_expense(i);
                               return true;
            }
        });





        return v;
    }

    void getincome(){
        try{

            Calendar calendar = Calendar.getInstance();

            final String DATE_FORMAT_2 = "yyyy-mm-dd";



            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String today=timestamp.toString().substring(0,11);

            Cursor c = helper.income_report(db);
            Cursor e = helper.expense_report(db);
toti=0;
tote=0;
            while(c.moveToNext()) {
                String date1=c.getString(2).substring(0,11);
                if(today.equals(date1)){
                arr_income.add("Amount : " + c.getInt(0) + "\nSrc : " + c.getString(1) + "\nTime : " + c.getString(2));
                arr_income1.add(c.getString(2));
                toti=toti+Integer.valueOf(c.getInt(0));
                }

            }

            while(e.moveToNext()) {

                String date1=e.getString(3).substring(0,11);
                if(today.equals(date1)) {
                    arr_expense.add("Item : " + e.getString(0) + "\nCatagory : " + e.getString(1) + "\nPrice : " + e.getString(2) + "\nTime : " + e.getString(3));
                    arr_expense1.add(e.getString(3));
                    tote = tote + Integer.valueOf(e.getString(2));
                }

            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.activity_list_view, R.id.textView, arr_income);
            simpleList.setAdapter(arrayAdapter);

            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getContext(), R.layout.activity_list_view, R.id.textView, arr_expense);
            simpleList1.setAdapter(arrayAdapter1);

        }catch (Exception e){

        }
    }

    public void delete_income(final int i) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("Do you want to delete the item ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                helper.delete_income(db, arr_income1.get(i));
                arr_income.clear();
                arr_income1.clear();
                arr_expense.clear();
                arr_expense1.clear();
                getincome();

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

    public void delete_expense(final int i) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("Do you want to delete this ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                helper.delete_expense(db, arr_expense1.get(i));
                arr_expense.clear();
                arr_expense1.clear();
                arr_income.clear();
                arr_income1.clear();
                getincome();

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
