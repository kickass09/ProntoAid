package com.example.prontoaid;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Map;

public class Notifications extends AppCompatActivity {
    TextView present,absent;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref;
    String uname,stringnoty,job,name,phone,date,time;
    SharedPreferences sp;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag=0;
        sp=getSharedPreferences("logindata",MODE_PRIVATE);
        //job=sp.getString("Job","null");
        //name=sp.getString("")
        //get codes here
        myref=database.getReference("Schedule_Assigned");
        setContentView(R.layout.activity_notifications);
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

                        stringnoty=job+" "+name+" has been assigned to you on "+date+" at "+time+"\nContact: "+phone;
                        Log.d("StringYes",stringnoty);
                        absent.setText("");
                        present.setText(stringnoty);
                        flag=1;
                    }

                }
                if (flag==0){
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
