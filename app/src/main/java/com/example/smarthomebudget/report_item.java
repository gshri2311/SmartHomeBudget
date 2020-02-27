package com.example.smarthomebudget;


import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class report_item extends Fragment {

    ListView simpleList;
    ArrayList<String> arr_income, arr_expense, arr_income1, arr_expense1;
    SQLiteHelper helper;
    SQLiteDatabase db;
    private ListView simpleList1;
    EditText from, to;
    String max,max1;
    String totincome,totexpense;
    TextView maxitem;
    Button get;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_report_item, container, false);
        getActivity().setTitle("Item wise report");
        arr_income = new ArrayList<>();
        arr_income1 = new ArrayList<>();
        arr_expense = new ArrayList<>();
        arr_expense1 = new ArrayList<>();
        from = (EditText) v.findViewById(R.id.from11);
        to = (EditText) v.findViewById(R.id.to11);
        get = (Button) v.findViewById(R.id.get11);
        maxitem = (TextView) v.findViewById(R.id.maxitem);

        helper = new SQLiteHelper(getContext());
        db = helper.getReadableDatabase();

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fr = from.getText().toString(), too = to.getText().toString();
                if (!fr.equals("") && !too.equals(""))
                { getincome(fr, too);
                    maxitem.setText("Total Income : "+totincome+"\nTotal expense : "+totexpense+"\nMaximum Spent Item : " + max+"\nAmount : "+max1);
                 }
                else
                    Toast.makeText(getContext(), "Please enter from and to date", Toast.LENGTH_LONG).show();
            }
        });

        simpleList = (ListView) v.findViewById(R.id.simpleListView11);
        simpleList1 = (ListView) v.findViewById(R.id.simpleListView22);
        final ScrollView sv = (ScrollView) v.findViewById(R.id.sv);


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

            update_income(i);

                return true;
            }
        });


        simpleList1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            update_expense(i);

                return true;
            }
        });

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
                builder.setMessage(arr_expense.get(i));
                AlertDialog alert = builder.create();
                alert.setIcon(R.drawable.report);
                alert.getWindow().setLayout(600, 600);
                alert.show();
            }
        });

        return v;
    }



        void getincome (String fr, String too){

         try {

             arr_income.clear();
             arr_expense.clear();
             arr_income1.clear();
             arr_expense1.clear();

             Cursor c = helper.income_report_date(db, fr, too);
             Cursor e = helper.expense_report_date(db, fr, too);

             totincome = "0";
             totexpense = "0";
             while (c.moveToNext()) {

                 arr_income.add("\nAmount : " + c.getInt(0) + "\nSrc : " + c.getString(1) + "\nTime : " + c.getString(2));
                 arr_income1.add(c.getString(2));
                 totincome = String.valueOf(Integer.valueOf(c.getInt(0) + Integer.valueOf(totincome)));

             }
             max = "0";
             max1 = "0";

             while (e.moveToNext()) {

                 arr_expense.add("\nItem : " + e.getString(0) + "\nCatagory : " + e.getString(1) + "\nPrice : " + e.getString(2) + "\nTime : " + e.getString(3));
                 arr_expense1.add(e.getString(3));
                 totexpense = String.valueOf(Integer.valueOf(e.getInt(2) + Integer.valueOf(totexpense)));
                 if (Integer.valueOf(max1) < Integer.valueOf(e.getString(2))) {
                     max1 = e.getString(2);
                     max = e.getString(0);
                 }

             }

             ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.activity_list_view, R.id.textView, arr_income);
             simpleList.setAdapter(arrayAdapter);

             ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getContext(), R.layout.activity_list_view, R.id.textView, arr_expense);
             simpleList1.setAdapter(arrayAdapter1);

         }catch (Exception e){

         }
        }


    public void update_expense(final int i) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setMessage("Select the operation !");
        builder.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                final AlertDialog alert = builder1.create();

                LinearLayout lila1= new LinearLayout(getContext());
                lila1.setOrientation(LinearLayout.VERTICAL); //1 is for vertical orientation
                final EditText input = new EditText(getContext());
                final EditText input1 = new EditText(getContext());
                final EditText input2 = new EditText(getContext());
                final Button update=new Button(getContext());
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                input.setLayoutParams(p);
                p.setMargins(100, 100, 70, 0);

                input.setHint("Enter item name ...");
                lila1.addView(input);
                input1.setLayoutParams(p);
                input1.setHint("Enter item catagory...");

                lila1.addView(input1);
                input2.setLayoutParams(p);
                input2.setHint("Enter price...");

                lila1.addView(input2);
                p.setMargins(200, 100, 70, 0);
                update.setLayoutParams(p);
                update.setText("update");
                lila1.addView(update);
                alert.setView(lila1);

                alert.show();
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!input.getText().toString().equals("") && !input1.getText().toString().equals("") && !input2.getText().toString().equals("")){
                            helper.update_expense(db,input.getText().toString(),input1.getText().toString(),input2.getText().toString(),arr_expense1.get(i));
                            arr_expense.clear();
                            arr_expense1.clear();
                            arr_income.clear();
                            arr_income1.clear();
                            getincome(from.getText().toString(),to.getText().toString());
                            Toast.makeText(getContext(),"Successfully Updated",Toast.LENGTH_LONG).show();
                            alert.cancel();
                        }
                        else {
                            Toast.makeText(getContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
                        }
                    }
                });




            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                helper.delete_expense(db, arr_expense1.get(i));
                arr_expense.clear();
                arr_expense1.clear();
                arr_income.clear();
                arr_income1.clear();
                getincome(from.getText().toString(),to.getText().toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void update_income(final int i) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setMessage("Select the operation  ?");
        builder.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                final AlertDialog alert = builder1.create();

                LinearLayout lila1= new LinearLayout(getContext());
                lila1.setOrientation(LinearLayout.VERTICAL); //1 is for vertical orientation
                final EditText input = new EditText(getContext());
                final EditText input1 = new EditText(getContext());
                final Button update=new Button(getContext());

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                input.setLayoutParams(p);
                p.setMargins(100, 100, 70, 0);
                input.setHint("Enter income amount ...");
                lila1.addView(input);
                input1.setLayoutParams(p);
                input1.setHint("Enter income source...");
                lila1.addView(input1);
                p.setMargins(200, 100, 70, 0);
                update.setLayoutParams(p);
                update.setText("update");
                lila1.addView(update);
                alert.setView(lila1);
                alert.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!input.getText().toString().equals("") && !input1.getText().toString().equals("")){
                            helper.update_income(db,input.getText().toString(),input1.getText().toString(),arr_income1.get(i));
                            arr_expense.clear();
                            arr_expense1.clear();
                            arr_income.clear();
                            arr_income1.clear();
                            getincome(from.getText().toString(),to.getText().toString());
                            Toast.makeText(getContext(),"Successfully Updated",Toast.LENGTH_LONG).show();
                            alert.cancel();
                        }
                        else {
                            Toast.makeText(getContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
                        }
                    }
                });





            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.delete_income(db, arr_income1.get(i));
                arr_expense.clear();
                arr_expense1.clear();
                arr_income.clear();
                arr_income1.clear();
                getincome(from.getText().toString(),to.getText().toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}