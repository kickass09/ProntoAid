package com.example.prontoaid;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//date
import java.util.Date;


public class Subject extends AppCompatActivity implements View.OnClickListener {
    Button btnDatePicker, btnTimePicker, btnsearch;
    EditText txtDate, txtTime;
    String job,loc,uid,datein,address,email,phone,name;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Intent intent ;
    double lat1,lon1;
    int amount;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Assigned");
    TextView job_amount;
    RadioGroup check_pay,bookmethod;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user",select_pay;
    int GOOGLE_PAY_REQUEST_CODE = 123;
    //ArrayList activeEmployess = new ArrayList<Employee>();
    DatabaseReference myRef1 = database.getReference("Jobs");
    SharedPreferences sp;
    //Map emp;

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButton:
                Button btn_date = this.findViewById(R.id.btn_date);
                Button btn_time = this.findViewById(R.id.btn_time);
                TextView in_date = this.findViewById(R.id.in_date);
                TextView in_time = this.findViewById(R.id.in_time);
                if (checked) {
                    btnsearch.setText("Book Now");
                    btn_date.setVisibility(View.GONE);

                    btn_time.setVisibility(View.GONE);

                    in_date.setVisibility(View.GONE);

                    in_time.setVisibility(View.GONE);
                    break;
                }
            case R.id.radioButton2:

                if (checked) {
                    btnsearch.setText("Book Now");
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
        check_pay = findViewById(R.id.payCheck);
        bookmethod = findViewById(R.id.bookMethod);
        sp = getSharedPreferences("logindata" , MODE_PRIVATE);
        name=sp.getString("name","null");
        phone=sp.getString("phone","null");
        email=sp.getString("username","null");
        // For reference
        // checkedbuttonid = bookmethod.getCheckedRadioButtonId();
        // scheduleforlatebutton = (RadioButton) findViewById(checkedbuttonid);
        // String buttontext = scheduleforlatebutton.getText()
        //
        Spinner spinner = findViewById(R.id.spinner);
        final List<String> categories = new ArrayList<String>();
        categories.add("Plumber");
        categories.add("Electrician");
        categories.add("House Cleaner");
        categories.add("Carpenter");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job=categories.get(position);
                Log.i("Selected Job ",job);
                if (job.equals("Carpenter")) {
                    job_amount = findViewById(R.id.job_Amount);
                    job_amount.setText("Rs 250");
                    }
                else if (job.equals("Plumber")) {
                    job_amount = findViewById(R.id.job_Amount);
                    job_amount.setText("Rs 300");
                }
                else if (job.equals("Electrician")) {
                    job_amount = findViewById(R.id.job_Amount);
                    job_amount.setText("Rs 350");
                }
                else {
                    job_amount = findViewById(R.id.job_Amount);
                    job_amount.setText("Rs 300");
                }

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
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnsearch.setOnClickListener(this);

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

            // Check for scheduler

            final String checkviewpending = ((RadioButton)findViewById(bookmethod.getCheckedRadioButtonId())).getText().toString();
            /*
            if (checkviewpending.equalsIgnoreCase("View Pending Requests")){
                Intent intent = new Intent(this, scheduler.class);
                startActivity(intent);
            }
            */
            select_pay= ((RadioButton)findViewById(check_pay.getCheckedRadioButtonId())).getText().toString();

            sp.edit().putString("for_loc",loc).commit();
            sp.edit().putString("for_job",job).commit();
            sp.edit().putString("for_pay",select_pay).commit();
            if (checkviewpending.equalsIgnoreCase("Hire now")) {
                myRef1.child(job).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //DatabaseReference refloc=database.getReference("UpdateLocation");
                            //refloc.setValue("1");
                            if (select_pay.equals("Google Pay")) {
                                Toast.makeText(Subject.this, "Pay now", Toast.LENGTH_SHORT).show();
                                //Toast paynow = Toast.makeText(getApplicationContext(),"Pay now-",Toast.LENGTH_SHORT);
                                //paynow.show();
                                switch (job) {
                                    case "Carpenter":
                                        amount = 250;
                                        break;
                                    case "Plumber":
                                        amount = 300;
                                        break;
                                    case "Electrician":
                                        amount = 350;
                                        break;
                                    case "House Cleaner":
                                        amount = 300;
                                        break;
                                    default:
                                        break;

                                }

                                Uri uri = new Uri.Builder()
                                        .scheme("upi")
                                        .authority("pay")
                                        .appendQueryParameter("pa", "shaunritty-1@okicici")
                                        .appendQueryParameter("pn", "Shaun Ritty")
                                        .appendQueryParameter("mc", "1234")
                                        .appendQueryParameter("tr", "983638Pronto")
                                        .appendQueryParameter("tn", "Service Payment")
                                        .appendQueryParameter("am", amount + "")
                                        .appendQueryParameter("cu", "INR")
                                        .appendQueryParameter("url", "www.google.com")
                                        .build();
                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(uri);
                                intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
                                startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
                            } else {
                                finish();
                                intent = new Intent(Subject.this, Result.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(Subject.this, "No available workers, try again later", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
         else{
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                datein=txtDate.getText().toString()+" "+txtTime.getText().toString();
               try{
                   // https://www.mkyong.com/java/how-to-convert-string-to-date-java/
                   // first convert date to date objecct
                   // then send to validate function
                   // if true send to database
                    Date date=formatter.parse(datein);

                   //Date date = null;
                   if (validateData(date)){
                       //Toast.makeText(Subject.this, "Hello Testing...", Toast.LENGTH_SHORT).show();
                       myRef=database.getReference("Requesting");
                       lat1=Double.parseDouble(sp.getString("latitude","null"));
                       lon1=Double.parseDouble(sp.getString("longitude","null"));
                       address=getAddress(lat1,lon1);
                       uid = myRef.push().getKey();
                       Map data = new HashMap();
                       data.put("DateBook", txtDate.getText().toString());
                       data.put("TimeBook", txtTime.getText().toString());
                       data.put("Job",job);
                       data.put("LocBook", address);
                       data.put("CustomerUser",email);
                       myRef.child(uid).setValue(data);
                       Toast.makeText(Subject.this, "Your request is being processed...", Toast.LENGTH_SHORT).show();
                       finish();
                       Intent intent = new Intent(Subject.this,WelcomeActivity.class);
                       startActivity(intent);
                       //Convert String to date object using:

                       // Avaiable format
                       //myRef.child(uid).child("Date").setValue(txtDate.getText().toString());
                       //myRef.child(uid).child("Time").setValue(txtTime.getText().toString());
                   } else{
                       Toast.makeText(Subject.this, "Date is invalid", Toast.LENGTH_SHORT).show();
                   }
                   // else
               }catch (Exception e){
                   // Show toast that date is wrong
                   Toast.makeText(Subject.this, "Date is wrong", Toast.LENGTH_SHORT).show();

               }

            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(Subject.this,WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String status=data.getStringExtra("Status");
        Log.d("Result Code of payment:",Integer.toString(requestCode));
        Log.d("Google Code of payment:",Integer.toString(GOOGLE_PAY_REQUEST_CODE));
        Toast.makeText(Subject.this, status, Toast.LENGTH_SHORT).show();
        if (status.equals("SUCCESS")){
            finish();
            Intent intent = new Intent(Subject.this, Result.class);
            startActivity(intent);
            }
        }

        private boolean validateData(Date date) {

         // write the logic here to validate for example less than 2 days
           //if date.
            Boolean retval = false;
            Date currentdate= new Date();
            if (date.compareTo(currentdate)>0){
                retval = true;
            }

         return retval;
        }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(Subject.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            /*add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();*/

            //Log.v("IGA", "Address" + add);
            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    }





