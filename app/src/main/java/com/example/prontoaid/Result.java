package com.example.prontoaid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
    double distance,final_distance;
    int customer_loc;
    String loc;
    TextView worklist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String job = getIntent().getStringExtra("for_job");
        loc = getIntent().getStringExtra("for_loc");
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
                Log.d("Loc Kitti",loc );
                for(int i=0;i<places_loc.length;i++){
                    if (loc.equals(places_loc[i][0])) {
                        customer_loc = i;
                        Log.d("place", " found it " + i);
                    }
                }
                for(int i=0;i<activeEmployess.size();i++){
                    loc=((Employee)activeEmployess.get(i)).getLocation();
                    Log.d("Current Emp Loc:  " ,places_loc[customer_loc][1]+" "+places_loc[customer_loc][2]);
                    for(int j=0;j<places_loc.length;j++){
                        if (loc.equals(places_loc[j][0])) {
                            Log.d("Active Empls: " ,places_loc[j][1]+" "+places_loc[j][2]);
                            distance = Math.sqrt(Math.pow((Integer.parseInt(places_loc[j][1]) - Integer.parseInt(places_loc[customer_loc][1])), 2) + Math.pow((Integer.parseInt(places_loc[j][2]) - Integer.parseInt(places_loc[customer_loc][2])), 2));
                            Log.d("Active Distance: " , distance + "");
                        }
                    }
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