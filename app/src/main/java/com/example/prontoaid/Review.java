package com.example.prontoaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Review extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


    }

    public void reviewsubmit(View view){
        Toast.makeText(this, "Thank you for your review", Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(Review.this, Subject.class);
        startActivity(intent);
    }
}
