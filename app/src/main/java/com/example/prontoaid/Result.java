package com.example.prontoaid;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Result extends AppCompatActivity  {
    //Bundle extras = getIntent().getExtras();
    ArrayList activeEmployess = new ArrayList<Employee>();
    String uid,tid,vid,startdate,enddate;
    double distance,lat1,lon1,lat2,lon2,weight,normaldistance,rating,normalrating,bestweight;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef1,myRef,myRef2,myRef3;
    String loc,name,job,phone,uname,cusname,cusnum,select_pay,email;
    TextView worklist;
    Button taskover;
    int empno,amount;
    Map emp;
    SimpleDateFormat formatter;


    public void task_complete(View view){


        myRef1 = database.getReference("Assigned");
        myRef3=database.getReference("History");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myRef1.child(uid).removeValue();
                Employee ideal=(Employee)activeEmployess.get(empno);
                //Log.d("Test List Username",d.getUsername());
                Map data = new HashMap();
                Map hist= new HashMap();
                data.put("User", ideal.getUsername());
                data.put("Emp_Name", ideal.getName());
                data.put("Phone_Number", ideal.getContact());
                data.put("Loclatitude",ideal.getLoclatitude());
                data.put("Loclongitude",ideal.getLoclongitude());
                data.put("Rating",ideal.getRating());

                hist.put("Job",job);
                hist.put("Customer_Username",email);
                hist.put("Worker_Username",ideal.getUsername());
                hist.put("Customer_lat",lat1);
                hist.put("Customer_lon",lon1);
                hist.put("Worker_lat",((Employee) activeEmployess.get(empno)).getLoclatitude());
                hist.put("Worker_lon",((Employee) activeEmployess.get(empno)).getLoclatitude());
                hist.put("Start",startdate);
                hist.put("PaymentMode",select_pay);
                Date date=new Date();
                enddate=formatter.format(date);
                hist.put("End",enddate);


                myRef2.child(job).child(tid).setValue(data);
                if (select_pay.equals("Cash on completion")) {
                    switch (job) {
                        case "Carpenter":
                            amount=250;
                            break;
                        case "Plumber":
                            amount=300;
                            break;
                        case "Electrician":
                            amount=350;
                            break;
                        case "House Cleaner":
                            amount=300;
                            break;
                        default:
                            break;

                        }
                    Toast.makeText(Result.this, "Please pay Rs"+amount, Toast.LENGTH_SHORT).show();
                    }
                hist.put("Amount",amount+"");
                vid=myRef3.push().getKey();
                myRef3.child(vid).setValue(hist);

                finish();
                Intent intent = new Intent(Result.this, Review.class);
                startActivity(intent);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        final SharedPreferences sp = getSharedPreferences("logindata" , MODE_PRIVATE);
        taskover = findViewById(R.id.taskover);
        //paymode=sp.getString("paymode","null");
        job = sp.getString("for_job","null");
        //customer loc
        lat1=Double.parseDouble(sp.getString("latitude","null"));
        lon1=Double.parseDouble(sp.getString("longitude","null"));
        email=sp.getString("username","null");
        loc =  sp.getString("for_loc","null");
        //job = getIntent().getStringExtra("for_job");
        //loc = getIntent().getStringExtra("for_loc");
        select_pay = sp.getString("for_pay","null");
        //Log.d("Payment",select_pay);
        myRef = database.getReference("Jobs");
        myRef2=database.getReference("Jobs");

        myRef.child(job).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    //Log.d("Assigning","1");
                    activeEmployess.clear();

                    String details = "";
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        emp=(Map)postSnapshot.getValue();
                        Log.d("Post Test",emp.toString());
                        Employee e=new Employee(emp.get("User").toString(),emp.get("Phone_Number").toString(),emp.get("Emp_Name").toString(),emp.get("Loclatitude").toString(),emp.get("Loclongitude").toString(),emp.get("Rating").toString());
                        activeEmployess.add(e);

                        }
                    //worker loc
                    lat2=Double.parseDouble(((Employee) activeEmployess.get(0)).getLoclatitude());
                    lon2=Double.parseDouble(((Employee) activeEmployess.get(0)).getLoclongitude());
                    distance=calculateDistance(lat1,lon1,lat2,lon2);
                    normaldistance=((5000-distance)/5000)*10;
                    rating=Double.parseDouble(((Employee) activeEmployess.get(0)).getRating());
                    normalrating=2*rating;
                    bestweight=0.6*normaldistance+0.4*normalrating;
                    //Log.d("Assigning","2");

                    for (int i = 0; i < activeEmployess.size(); i++) {
                        //loc = ((Employee) activeEmployess.get(i)).getLocation();
                        //lat2=76.3707827;
                        //lon2=10.0037787;
                        lat2 = Double.parseDouble(((Employee) activeEmployess.get(i)).getLoclatitude());
                        lon2 = Double.parseDouble(((Employee) activeEmployess.get(i)).getLoclongitude());

                        distance = calculateDistance(lat1, lon1, lat2, lon2);

                        if (distance <= 5000){
                            normaldistance=((5000-distance)/5000)*10;
                            rating=Double.parseDouble(((Employee) activeEmployess.get(i)).getRating());
                            normalrating=2*rating;
                            weight=0.6*normaldistance+0.4*normalrating;
                            if (bestweight <= weight) {
                                //Log.d("Assigning","3");
                                bestweight=weight;
                                uname = ((Employee) activeEmployess.get(i)).getUsername();
                                name = ((Employee) activeEmployess.get(i)).getName();
                                phone = ((Employee) activeEmployess.get(i)).getContact();
                                details = job + " " + name + " has been assigned to you\nContact: " + phone;
                                empno = i;
                                taskover.setVisibility(View.VISIBLE);
                                //myRef3=database.getReference("History");
                                myRef1 = database.getReference("Assigned");
                                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        cusname = sp.getString("name", "null");
                                        cusnum = sp.getString("phone", "null");
                                        uid = myRef1.push().getKey();
                                        Map data = new HashMap();
                                        data.put("Worker_User", uname);
                                        data.put("Customer_Name", cusname);
                                        data.put("Customer_Contact", cusnum);
                                        data.put("Customer_Latitude", lat1 + "");
                                        data.put("Customer_Longitude", lon1 + "");
                                        Date date = new Date();
                                        sp.edit().putString("Worker_User", uname).commit();
                                        startdate = formatter.format(date);
                                        myRef1.child(uid).setValue(data);

                                        Log.d("User Worker again", uname);
                                        myRef = myRef.child(job);
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                    //String tid = postSnapshot.getKey();
                                                    String username = postSnapshot.child("User").getValue(String.class);
                                                    Log.d("Username tid", username);
                                                    //activeEmployess.add(e);
                                                    //Log.d("Users Again", e.getUsername());
                                                    if (uname.equals(username)) {
                                                        tid = postSnapshot.getKey();
                                                        Log.d("Testing again", tid);
                                                        myRef2.child(job).child(tid).removeValue();
                                                    }
                                                    //myRef.child(uid).removeValue();
                                                    //Log.d("trial3",((Employee)activeEmployess.get(0)).getUsername());

                                                    //Log.d("trial2",Integer.toString(activeEmployess.size()));
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                worklist = findViewById(R.id.textView7);
                                worklist.setText(details);


                            }
                    }

                    }
                    //change here
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(Result.this, Subject.class);
        startActivity(intent);
    }

    public final static double AVERAGE_RADIUS_OF_EARTH = 6371000;
    public int calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
                (Math.cos(Math.toRadians(userLat))) *
                        (Math.cos(Math.toRadians(venueLat))) *
                        (Math.sin(lngDistance / 2)) *
                        (Math.sin(lngDistance / 2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));

    }
}