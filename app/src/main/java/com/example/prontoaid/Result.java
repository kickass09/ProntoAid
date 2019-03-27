package com.example.prontoaid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Result extends AppCompatActivity  {

    ArrayList activeEmployess = new ArrayList<Employee>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Jobs");

        myRef.child("Carpenter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                activeEmployess.clear();
                Log.d("here1",".............");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    Log.d("here2",".............");
                    Log.d("trial",postSnapshot.getValue().toString());
                    Employee e = postSnapshot.getValue(Employee.class);
                    activeEmployess.add(e);

                    Log.d("trial3",((Employee)activeEmployess.get(0)).getUsername());

                    Log.d("trial2",Integer.toString(activeEmployess.size()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
