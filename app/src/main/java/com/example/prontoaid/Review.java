package com.example.prontoaid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Review extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref;
    RatingBar ratingBar;
    double rating,workerrating,finalrating;
    SharedPreferences sp;
    String uname;
    int noreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        sp = getSharedPreferences("logindata" , Context.MODE_PRIVATE);
        uname=sp.getString("Worker_User","null");
        ratingBar=findViewById(R.id.ratingBar);
        //myref=database.getReference("Workers");
        Log.d("Reviewss name",uname);


    }

    public void reviewsubmit(View view){
        Toast.makeText(this, "Thank you for your review", Toast.LENGTH_SHORT).show();
        Log.d("Reviewss name",uname);
        myref=database.getReference("Workers");
        rating=ratingBar.getRating();
        Log.d("Rating value",rating+"");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    if ((postSnapshot.child("Username").getValue().toString()).equals(uname)){
                        workerrating=Double.parseDouble(postSnapshot.child("AvgReview").getValue().toString());
                        noreview=Integer.parseInt(postSnapshot.child("NoReviews").getValue().toString());
                        if (noreview==0){
                            myref.child(postSnapshot.getKey()).child("AvgReview").setValue(rating);
                            myref.child(postSnapshot.getKey()).child("NoReviews").setValue("1");
                        }
                        else{
                            finalrating=(workerrating*noreview+rating)/(noreview+1);
                            myref.child(postSnapshot.getKey()).child("AvgReview").setValue(finalrating+"");
                            noreview+=1;
                            myref.child(postSnapshot.getKey()).child("NoReviews").setValue(noreview+"");

                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        finish();
        Intent intent = new Intent(Review.this, WelcomeActivity.class);
        startActivity(intent);
    }
}
