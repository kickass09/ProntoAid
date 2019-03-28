package com.example.prontoaid;

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

import java.util.ArrayList;

public class Result extends AppCompatActivity  {
    //Bundle extras = getIntent().getExtras();
    ArrayList activeEmployess = new ArrayList<Employee>();
    TextView worklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        String job = getIntent().getStringExtra("for_job");
        //Log.i("Job Kitti",job);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Jobs");
        //if (myRef.child(job)==null)
            //Log.d("Acive Workers: ","None");

        myRef.child(job).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    Log.d("Active Workers", "None");
                    worklist=findViewById(R.id.textView6);
                    worklist.setText("Active Workers: None");
                  }
                int no=0;
                activeEmployess.clear();
                //Log.d("here1",".............");
                String details="";
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    no++;
                    //Log.d("here2",".............");
                    Log.d("Active Worker "+no,postSnapshot.getValue().toString());
                    details+="Location: "+postSnapshot.child("Loc").getValue().toString()+" Username"+postSnapshot.child("User").getValue().toString()+"\n";
                    //details+=postSnapshot.child("Loc").getValue().toString();
                    Employee e = postSnapshot.getValue(Employee.class);
                    activeEmployess.add(e);

                    //Log.d("trial3",((Employee)activeEmployess.get(0)).getUsername());

                    //Log.d("trial2",Integer.toString(activeEmployess.size()));
                }
                worklist=findViewById(R.id.textView7);
                worklist.setText(details);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}