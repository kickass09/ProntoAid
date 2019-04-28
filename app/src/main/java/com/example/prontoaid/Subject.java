package com.example.prontoaid;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Subject extends AppCompatActivity implements View.OnClickListener {
    Button btnDatePicker, btnTimePicker, btnsearch, btnPayNow;
    EditText txtDate, txtTime;
    String job,loc,name,phone;
    private int mYear, mMonth, mDay, mHour, mMinute;
    int number;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Assigned");

    //    Button radioButton = findViewById(R.id.radioButton);
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();


        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButton:
                Button btn_date = (Button) this.findViewById(R.id.btn_date);
                Button btn_time = (Button) this.findViewById(R.id.btn_time);
                TextView in_date = (TextView) this.findViewById(R.id.in_date);
                TextView in_time = (TextView) this.findViewById(R.id.in_time);
                if (checked) {

                    btn_date.setVisibility(View.GONE);

                    btn_time.setVisibility(View.GONE);

                    in_date.setVisibility(View.GONE);

                    in_time.setVisibility(View.GONE);
                    break;
                }
            case R.id.radioButton2:

                if (checked) {
                    Button btn_date1 = (Button) this.findViewById(R.id.btn_date);
                    btn_date1.setVisibility(View.VISIBLE);
                    Button btn_time1 = (Button) this.findViewById(R.id.btn_time);
                    btn_time1.setVisibility(View.VISIBLE);
                    TextView in_date1 = (TextView) this.findViewById(R.id.in_date);
                    in_date1.setVisibility(View.VISIBLE);
                    TextView in_time1 = (TextView) this.findViewById(R.id.in_time);
                    in_time1.setVisibility(View.VISIBLE);
                    break;
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener(this);
        final List<String> categories = new ArrayList<String>();
        categories.add("Plumber");
        categories.add("Electrician");
        categories.add("House Cleaner");
        categories.add("Carpenter");
        //categories.add("Call Girl");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job=categories.get(position);
                Log.i("Selected Job ",job);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        //spinner.setOnItemSelectedListener(this);
        final List<String> categories2 = new ArrayList<String>();
        categories2.add("Kakkanad");
        categories2.add("Vytila");
        categories2.add("Thripunithura");
        categories2.add("Palarivattom");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
        spinner2.setAdapter(dataAdapter2);
        //final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loc=categories2.get(position);
                //Log.i("Selected Location ",loc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        btnsearch = (Button) findViewById(R.id.search);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnsearch.setOnClickListener(this);

        //Pay now button
        btnPayNow = (Button)findViewById(R.id.paynow);

        btnPayNow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            long now = System.currentTimeMillis() - 1000;
            datePickerDialog.getDatePicker().setMinDate(now);
            datePickerDialog.getDatePicker().setMaxDate(now + (1000 * 60 * 60 * 24 * 3));

            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);

            timePickerDialog.show();
        }
        if (v == btnsearch){

            Intent intent = new Intent(Subject.this, Result.class);
            intent.putExtra("for_job",job);
            intent.putExtra("for_loc",loc);
            startActivity(intent);
            finish();
        }
        if (v == btnPayNow) {
            // code for showing payment
            Toast paynow = Toast.makeText(getApplicationContext(),"Pay now-",Toast.LENGTH_SHORT);
            paynow.show();
            String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
            int GOOGLE_PAY_REQUEST_CODE = 123;

            Uri uri =
                    new Uri.Builder()
                            .scheme("upi")
                            .authority("pay")
                            .appendQueryParameter("pa", "your-merchant-vpa@xxx")
                            .appendQueryParameter("pn", "your-merchant-name")
                            .appendQueryParameter("mc", "your-merchant-code")
                            .appendQueryParameter("tr", "your-transaction-ref-id")
                            .appendQueryParameter("tn", "your-transaction-note")
                            .appendQueryParameter("am", "your-order-amount")
                            .appendQueryParameter("cu", "INR")
                            .appendQueryParameter("url", "your-transaction-url")
                            .build();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent intent = new Intent(Subject.this, login.class);
        startActivity(intent);
    }


}



