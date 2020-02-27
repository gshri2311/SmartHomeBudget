package com.example.smarthomebudget;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class report_catagory extends Fragment {


    ListView simpleList;
    ArrayList<String> arr_income,arr_expense,arr_income1,arr_expense1;
    SQLiteHelper helper;
    SQLiteDatabase db;
    private ListView simpleList1;
    EditText from,to;
    Button get;
    String max,max1;
    TextView maxcat;
    private String totincome,totexpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_report_catagory, container, false);
        getActivity().setTitle("Catagory wise report");
        arr_income = new ArrayList<>();
        arr_income1 = new ArrayList<>();
        arr_expense = new ArrayList<>();
        arr_expense1 = new ArrayList<>();
        maxcat=(TextView)v.findViewById(R.id.maxcat);
        from=(EditText)v.findViewById(R.id.from);
        to=(EditText)v.findViewById(R.id.to);
        get=(Button)v.findViewById(R.id.get);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fr=from.getText().toString(),too=to.getText().toString();
                if(!fr.equals("")&&!too.equals(""))
                {getincome(fr,too);
                maxcat.setText("Total Income : "+totincome+"\nTotal expense : "+totexpense+"\nMaximum spent catagory : "+ max+"\nAmount : "+max1);
                }
                else
                    Toast.makeText(getContext(),"Please enter from and to date",Toast.LENGTH_LONG).show();
            }
        });

        final ScrollView sv=(ScrollView)v.findViewById(R.id.sv1);

        simpleList = (ListView) v.findViewById(R.id.simpleListView);
        simpleList1 = (ListView) v.findViewById(R.id.simpleListView1);


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

        helper = new SQLiteHelper(getContext());
        db = helper.getReadableDatabase();


        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Income");
                builder.setMessage(arr_income.get(i));
                AlertDialog alert = builder.create();
                alert.setIcon(R.drawable.report);
                alert.getWindow().setLayout(600, 600);
                alert.show();

            }
        });

        simpleList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Expense");
                builder.setMessage(arr_expense1.get(i));
                AlertDialog alert = builder.create();
                alert.setIcon(R.drawable.report);
                alert.getWindow().setLayout(600, 600);
                alert.show();
            }
        });


        return v;
    }

    void getincome(String fr, String too) {


        arr_income.clear();
        arr_expense.clear();
        arr_income1.clear();
        arr_expense1.clear();

        Cursor c = helper.income_report_date_cat(db, fr, too);
        Cursor e = helper.expense_report_date_cat(db, fr, too);

        totexpense = "0";
        totincome = "0";
        while (c.moveToNext()) {

            arr_income.add("\n\nSource : " + c.getString(0) + "\nAmount : " + c.getString(1));

            totincome = String.valueOf(Integer.valueOf(c.getString(1)) + Integer.valueOf(totincome));
        }

        max1 = "0";
        while (e.moveToNext()) {

            totexpense = String.valueOf(Integer.valueOf(e.getInt(1) + Integer.valueOf(totexpense)));
            arr_expense.add("\n\nCatogory : " + e.getString(0) + "\nTotal Spent : " + e.getString(1));
            if (Integer.valueOf(max1) < Integer.valueOf(e.getString(1))) {
                max1 = e.getString(1);
                max = e.getString(0);
            }

            Cursor e1 = helper.getitems(db, e.getString(0), fr, too);
            String items = "";
            while (e1.moveToNext()) {

                items = items + "\n\nItem : " + e1.getString(0) + "\nTotal Spent : " + e1.getString(1);
            }

            arr_expense1.add(items);

        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.activity_list_view, R.id.textView, arr_income);
        simpleList.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getContext(), R.layout.activity_list_view, R.id.textView, arr_expense);
        simpleList1.setAdapter(arrayAdapter1);
    }


}
