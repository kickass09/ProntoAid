package com.example.prontoaid;

import android.content.SharedPreferences;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Notifications extends AppCompatActivity {
    TextView present,absent;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref;
    String uname,stringnoty,job,name,phone,date,time;
    Date daterequest,datecurrent,timerequest,timecurrent,reqdate;
    String datescurrent,timescurrent;
    SharedPreferences sp;
    SimpleDateFormat sdf,stf;
    Button finish;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag=0;
        sdf=new SimpleDateFormat("dd-MM-yyyy");
        stf=new SimpleDateFormat("HH:mm");
        sp=getSharedPreferences("logindata",MODE_PRIVATE);
        //job=sp.getString("Job","null");
        //name=sp.getString("")
        //get codes here
        myref=database.getReference("Schedule_Assigned");
        setContentView(R.layout.activity_notifications);
        finish=findViewById(R.id.finish);
        present=findViewById(R.id.textViewPresent);
        absent=findViewById(R.id.textViewAbsent);
        uname=sp.getString("username","null");

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Map noty=(Map)postSnapshot.getValue();
                    if ((noty.get("CustomerUser").toString()).equals(uname)){
                        job=noty.get("Job").toString();
                        name=noty.get("WorkerName").toString();
                        phone=noty.get("WorkerContact").toString();
                        date=noty.get("DateBook").toString();
                        time=noty.get("TimeBook").toString();

                        try {
                            daterequest=sdf.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            timerequest=stf.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date dates=new Date();
                        datescurrent=sdf.format(dates);
                        timescurrent=stf.format(dates);
                        try {
                            datecurrent=sdf.parse(datescurrent);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            timecurrent=stf.parse(timescurrent);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int resulttime=timecurrent.compareTo(timerequest);
                        final int resultdate=datecurrent.compareTo(daterequest);
                        if (resultdate>0){
                            finish.setVisibility(View.GONE);
                            present.setText("");
                            absent.setText("You have no scheduled jobs");
                            //Toast.makeText(Notifications.this, "Test Yaar", Toast.LENGTH_SHORT).show();
                            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postnapshot:dataSnapshot.getChildren()){
                                        try {
                                            reqdate=sdf.parse(postnapshot.child("DateBook").getValue().toString());

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        int compresult=reqdate.compareTo(datecurrent);
                                        if (compresult<0){
                                            myref.child(postnapshot.getKey()).removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else if (resultdate==0){
                            Log.d("Theres"," time left");
                            if (resulttime>=0){
                                finish.setVisibility(View.VISIBLE);
                                stringnoty=job+" "+name+" has been assigned to you on "+date+" at "+time+"\nContact: "+phone;
                                Log.d("StringYes",stringnoty);
                                absent.setText("");
                                present.setText(stringnoty);
                                flag=1;

                            }

                        }

                        else{
                            finish.setVisibility(View.GONE);
                            stringnoty=job+" "+name+" has been assigned to you on "+date+" at "+time+"\nContact: "+phone;
                            Log.d("StringYes",stringnoty);
                            absent.setText("");
                            present.setText(stringnoty);
                            flag=1;
                        }


                    }

                }
                if (flag==0){
                    finish.setVisibility(View.GONE);
                    present.setText("");
                    absent.setText("You have no scheduled jobs");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
