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
    double distance,best_distance;
    int customer_loc,r_no;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef1,myRef,myRef2,refloc;
    String loc,name,job,phone,uname,cusname,cusnum,select_pay;
    TextView worklist;
    Button taskover;
    int empno,amount;
    //String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user",select_pay;

    public void task_complete(View view){


        myRef1 = database.getReference("Assigned");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myRef1.child(uid).removeValue();
                myRef2.child(job).child(tid).setValue(activeEmployess.get(empno));
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
                refloc=database.getReference("UpdateLocation");
                refloc.setValue("0");
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

        taskover = findViewById(R.id.taskover);
        SharedPreferences sp = getSharedPreferences("logindata" , MODE_PRIVATE);
        job = sp.getString("for_job","null");

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
                    int no = 0;
                    activeEmployess.clear();
                    //Log.d("here1",".............");
                    String details = "";
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        no++;
                        //Log.d("here2",".............");
                        Log.d("Active Worker " + no, postSnapshot.getValue().toString());
                        //details += "Location: " + postSnapshot.child("Loc").getValue().toString() + " Username" + postSnapshot.child("User").getValue().toString() + "\n";
                        Employee e = postSnapshot.getValue(Employee.class);
                        activeEmployess.add(e);
                        //Log.d("trial3",((Employee)activeEmployess.get(0)).getUsername());
                        //Log.d("trial2",Integer.toString(activeEmployess.size()));
                    }
                    Log.d("Loc Kitti", loc);
                    for (int i = 0; i < places_loc.length; i++) {
                        if (loc.equals(places_loc[i][0])) {
                            customer_loc = i;
                            Log.d("place", " found it " + i);
                        }
                    }
                    //user=((Employee) activeEmployess.get(0)).getUsername();
                    best_distance=100;
                    for (int i = 0; i < activeEmployess.size(); i++) {
                        loc = ((Employee) activeEmployess.get(i)).getLocation();
                        Log.d("Current Emp Loc:  ", places_loc[customer_loc][1] + " " + places_loc[customer_loc][2]);
                        for (int j = 0; j < places_loc.length; j++) {
                            if (loc.equals(places_loc[j][0])) {
                                Log.d("Active Empls: ", places_loc[j][1] + " " + places_loc[j][2]);
                                distance = Math.sqrt(Math.pow((Integer.parseInt(places_loc[j][1]) - Integer.parseInt(places_loc[customer_loc][1])), 2) + Math.pow((Integer.parseInt(places_loc[j][2]) - Integer.parseInt(places_loc[customer_loc][2])), 2));
                                Log.d("Active Distance: ", distance + "");
                                if (best_distance>distance){
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
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            //myRef=myRef.getParent();
                                            SharedPreferences sp = getSharedPreferences("logindata" , MODE_PRIVATE);
                                            cusname = sp.getString("name","null");
                                            cusnum =  sp.getString("phone","null");

                                            uid = myRef1.push().getKey();
                                            Map data = new HashMap();
                                            data.put("Worker_User", uname);
                                            data.put("Customer_Name", cusname);
                                            data.put("Customer_Contact", cusnum);
                                            data.put("Customer_Location", places_loc[customer_loc][0]);
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
                                                    //
                                                }
                                            });

                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });


                                    worklist = findViewById(R.id.textView7);
                                    worklist.setText(details);
                                }
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

}