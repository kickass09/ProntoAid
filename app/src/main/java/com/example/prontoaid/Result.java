package com.example.prontoaid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Result extends AppCompatActivity  {
    //Bundle extras = getIntent().getExtras();
    ArrayList activeEmployess = new ArrayList<Employee>();
    String places_loc[][]={{"Kakkanad","1","2"},{"Vytila","5","2"},{"Thripunithura","7","4"},{"Palarivattom","6","1"}};
    double distance,best_distance;
    int customer_loc,r_no;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef1;
    String loc,name,job,phone,uname,cusname,cusnum;
    TextView worklist;
    Button taskover;


    public void task_complete(View view){

        myRef1 = database.getReference("Assigned");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myRef1.child(r_no+"").removeValue();
                Intent intent = new Intent(Result.this, Subject.class);
                startActivity(intent);
                finish();
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

        job = getIntent().getStringExtra("for_job");
        loc = getIntent().getStringExtra("for_loc");
        //Log.i("Job Kitti",job);


        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Jobs");
        //if (myRef.child(job)==null)
            //Log.d("Acive Workers: ","None");

        myRef.child(job).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    Log.d("Active Workers", "None");
                    worklist = findViewById(R.id.textView7);
                    worklist.setText("");
                    worklist = findViewById(R.id.textView6);
                    worklist.setText("Active Workers: None");
                }
                else{
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
                                    taskover.setVisibility(View.VISIBLE);

                                    myRef1 = database.getReference("Assigned");
                                    myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int number = (int)dataSnapshot.getChildrenCount();
                                            number++;
                                            //myRef=myRef.getParent();


                                            SharedPreferences sp = getSharedPreferences("logindata" , MODE_PRIVATE);
                                            cusname = sp.getString("name","null");
                                            cusnum =  sp.getString("phone","null");


                                            r_no=number;
                                            myRef1.child(number+"").child("Worker_User").setValue(uname);
                                            myRef1.child(number+"").child("Customer_Name").setValue(cusname);
                                            myRef1.child(number+"").child("Customer_Contact").setValue(cusnum);
                                            myRef1.child(number+"").child("Customer_Location").setValue(places_loc[customer_loc][0]);
                                            //myRef1.child(number+"").onDisconnect().removeValue();

                                            //if(myRef1.child())

                                            //int number2=(int)dataSnapshot.getChildrenCount();
                                            //int number2=(int)dataSnapshot.getParent().child("Jobs").getChildre
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
        myRef1 = database.getReference("Assigned");
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myRef1.child(r_no+"").removeValue();
                finish();
                Intent intent = new Intent(Result.this, Subject.class);
                startActivity(intent);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }
}
