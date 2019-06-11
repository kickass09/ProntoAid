package com.example.prontoaid;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class login extends AppCompatActivity {
    Button r,l;
    EditText iemail,ipassword;
    FirebaseAuth Auth;
    private TextView ForgotPassword;
    int flag=0,number;
    ProgressDialog progressDialog;
    Double latitude,longitude;

    public LocationManager mLocationManager = null;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askPermission();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Customer");
        FirebaseApp.initializeApp(this);


        Auth = FirebaseAuth.getInstance();
        setContentView(R.layout.login);
        iemail = (EditText) findViewById(R.id.editText);

        ipassword = (EditText) findViewById(R.id.editText2);
        Auth = FirebaseAuth.getInstance();
        ForgotPassword = (TextView)findViewById(R.id.tvForgotPassword);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        flag=0;
        final Context context= this;

        r= (Button) findViewById(R.id.button);
        l= (Button) findViewById(R.id.button2);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, SignUp.class);
                startActivity(intent);
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading");
                progressDialog.show();
                final String email = iemail.getText().toString();
                final String password = ipassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>(){
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference("Customer");

                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Log.i("Test2",email);
                                number=(int)dataSnapshot.getChildrenCount();
                                //Log.i("Number of Children",number+"");
                                //number=Integer.parseInt(dataSnapshot.child("number_customer").getValue().toString());
                                int n=1;
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                                    String uname = postSnapshot.child("Username").getValue(String.class);
                                    String name = postSnapshot.child("Name").getValue(String.class);
                                    String phone = postSnapshot.child("Phone_Number").getValue(String.class);


                                    //Log.i("uname",uname);
                                    if (uname.equals(email)) {
                                        //Log.i("Test4",flag+"");
                                        if (task.isSuccessful()) {
                                            flag = 1;
                                            progressDialog.dismiss();

                                            //Disconnection
                                            //myRef.child(n+"").child("Available").setValue("Online");
                                            //myRef.child(n+"").child("Available").onDisconnect().setValue("Disconnected");

                                            SharedPreferences sp = getSharedPreferences("logindata" , Context.MODE_PRIVATE);
                                            sp.edit().putString("name",name).commit();
                                            sp.edit().putString("phone",phone).commit();
                                            sp.edit().putString("latitude",latitude+"").commit();
                                            sp.edit().putString("longitude",longitude+"").commit();
                                            //Log.d("Post Key",postSnapshot.getKey());
                                            //locationListenSet();
                                            myRef.child(postSnapshot.getKey()).child("Loc_Latitude").setValue(latitude+"");
                                            myRef.child(postSnapshot.getKey()).child("Loc_Longitude").setValue(longitude+"");
                                            //float dist=calculateDistance(latitude,longitude,10.001944, 76.350272);
                                            //Log.i("Distance",dist+"");
                                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                            //Intent intent = new Intent(login.this, Subject.class);
                                            Intent intent = new Intent(login.this,WelcomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    if (n==number)
                                        break;
                                    n++;

                                }
                                if (flag==0){
                                    progressDialog.dismiss();
                                    Toast.makeText(login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                });
            }
        });
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, PasswordActivity.class));
            }
        });


    }


    void locationListenSet()
    {
        initializeLocationManager();
        LocationListener[] mLocationListeners = new login.LocationListener[]{

                new login.LocationListener(LocationManager.GPS_PROVIDER),
                new LocationListener(LocationManager.NETWORK_PROVIDER)
        };
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 100, 10f,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,100, 10f,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
    }


    public class LocationListener implements android.location.LocationListener {
        public Location mLastLocation;
        int i = 0;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location)
        {


            latitude=location.getLatitude();
            longitude=location.getLongitude();
            progressDialog.dismiss();
            //Log.i("Latitude",latitude+"");
            //Log.i("Longitude",longitude+"");
            //progressDialog.dismiss();
        }



        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }



    }
    void askPermission()
    {
        try {
            if (ActivityCompat.checkSelfPermission(login.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {//Checking permission
                progressDialog = new ProgressDialog(this);
                locationListenSet();
                progressDialog.setMessage("Setting your location");
                progressDialog.show();


            } else {

                ActivityCompat.requestPermissions(login.this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},99);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        progressDialog = new ProgressDialog(this);
        locationListenSet();
        progressDialog.setMessage("Setting your location");
        progressDialog.show();
        Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();

    }

}

