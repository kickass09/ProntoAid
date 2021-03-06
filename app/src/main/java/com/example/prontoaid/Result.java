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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Result extends AppCompatActivity  {
    //Bundle extras = getIntent().getExtras();
    ArrayList activeEmployess = new ArrayList<Employee>();
    String places_loc[][]={{"Kakkanad","1","2"},{"Vytila","5","2"},{"Thripunithura","7","4"},{"Palarivattom","6","1"}};
    String uid,tid;
    double distance,best_distance,lat1,lon1,lat2,lon2;
    int customer_loc,r_no;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef1,myRef,myRef2;
    String loc,name,job,phone,uname,cusname,cusnum,select_pay;
    TextView worklist;
    Button taskover;
    int empno,amount;
    Map emp;
    //Employee e;
    //String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user",select_pay;

    public void task_complete(View view){


        myRef1 = database.getReference("Assigned");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myRef1.child(uid).removeValue();
                Employee ideal=(Employee)activeEmployess.get(empno);
                //Log.d("Test List Username",d.getUsername());
                Map data = new HashMap();
                data.put("User", ideal.getUsername());
                data.put("Emp_Name", ideal.getName());
                data.put("Phone_Number", ideal.getContact());
                data.put("Loclatitude",ideal.getLoclatitude());
                data.put("Loclongitude",ideal.getLoclongitude());

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
        final SharedPreferences sp = getSharedPreferences("logindata" , MODE_PRIVATE);
        taskover = findViewById(R.id.taskover);
        //double d1,d2;
        //d1=calculateDistance(9.9932711,76.3582368,9.9931892,76.3581765);
        //d2=calculateDistance(9.9931892,76.3581765,9.9923511 ,76.3583902);
        //Log.d("Distance   Ashish ",d1+"");
        //Log.d("Distance  Nathaniel ",d2+"");
        job = sp.getString("for_job","null");
        lat1=Double.parseDouble(sp.getString("latitude","null"));
        lon1=Double.parseDouble(sp.getString("longitude","null"));

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
                        Employee e=new Employee(emp.get("User").toString(),emp.get("Phone_Number").toString(),emp.get("Emp_Name").toString(),emp.get("Loclatitude").toString(),emp.get("Loclongitude").toString());
                        activeEmployess.add(e);

                        }

                    lat2=Double.parseDouble(((Employee) activeEmployess.get(0)).getLoclatitude());
                    lon2=Double.parseDouble(((Employee) activeEmployess.get(0)).getLoclongitude());
                    best_distance=calculateDistance(lat1,lon1,lat2,lon2);
                    //Log.d("Assigning","2");

                    for (int i = 0; i < activeEmployess.size(); i++) {
                        //loc = ((Employee) activeEmployess.get(i)).getLocation();
                        //lat2=76.3707827;
                        //lon2=10.0037787;
                        lat2=Double.parseDouble(((Employee) activeEmployess.get(i)).getLoclatitude());
                        lon2=Double.parseDouble(((Employee) activeEmployess.get(i)).getLoclongitude());

                        distance=calculateDistance(lat1,lon1,lat2,lon2);

                        if (best_distance>=distance){
                            //Log.d("Assigning","3");
                            best_distance=distance;
                            uname=((Employee) activeEmployess.get(i)).getUsername();
                            name=((Employee) activeEmployess.get(i)).getName();
                            phone=((Employee) activeEmployess.get(i)).getContact();
                            details=job+" "+name+" has been assigned to you\nContact: "+phone;
                            empno=i;
                            taskover.setVisibility(View.VISIBLE);

                            myRef1 = database.getReference("Assigned");
                            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    cusname = sp.getString("name","null");
                                    cusnum =  sp.getString("phone","null");
                                    uid = myRef1.push().getKey();
                                    Map data = new HashMap();
                                    data.put("Worker_User", uname);
                                    data.put("Customer_Name", cusname);
                                    data.put("Customer_Contact", cusnum);
                                    data.put("Customer_Latitude",lat1+"");
                                    data.put("Customer_Longitude",lon1+"");
                                    myRef1.child(uid).setValue(data);
                                    Log.d("User Worker again", uname);
                                    myRef=myRef.child(job);
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