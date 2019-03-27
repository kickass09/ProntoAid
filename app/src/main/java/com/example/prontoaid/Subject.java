package com.example.prontoaid;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Subject extends AppCompatActivity implements View.OnClickListener {
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

//    Button radioButton = findViewById(R.id.radioButton);
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();



        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                Button btn_date = (Button) this.findViewById(R.id.btn_date);
                Button btn_time = (Button) this.findViewById(R.id.btn_time);
                TextView in_date = (TextView) this.findViewById(R.id.in_date);
                TextView in_time = (TextView) this.findViewById(R.id.in_time);
                if (checked)
                {

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
                   break;}
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Plumber");
        categories.add("Electrician");
        categories.add("House Cleaner");
        categories.add("Carpenter");
        //categories.add("Call Girl");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinner.setAdapter(dataAdapter);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        //spinner.setOnItemSelectedListener(this);
        List<String> categories2 = new ArrayList<String>();
        categories2.add("Kakkanad");
        categories2.add("Vytila");
        categories2.add("Thripunithura");
        categories2.add("Palarivattom");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
        spinner2.setAdapter(dataAdapter2);
        //final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

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
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            long now = System.currentTimeMillis() - 1000;
            datePickerDialog.getDatePicker().setMinDate(now);
            datePickerDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*3));

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
    }
}



