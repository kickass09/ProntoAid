package com.example.prontoaid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class login extends AppCompatActivity {
    Button r,l;
    EditText iemail,ipassword;
    FirebaseAuth Auth;
    private TextView ForgotPassword;
    int flag=0,number;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Customer");




        FirebaseApp.initializeApp(this);
        Auth = FirebaseAuth.getInstance();
        setContentView(R.layout.login);
        iemail = (EditText) findViewById(R.id.editText);
        ipassword = (EditText) findViewById(R.id.editText2);
        Auth = FirebaseAuth.getInstance();
        ForgotPassword = (TextView)findViewById(R.id.tvForgotPassword);

        addListenerOnButton();
    }
    public void addListenerOnButton() {
        flag=0;
        final Context context= this;
        progressDialog = new ProgressDialog(this);
        r= (Button) findViewById(R.id.button);
        l= (Button) findViewById(R.id.button2);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, SignUp.class);
                startActivity(intent);
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = iemail.getText().toString();
                final String password = ipassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Loading");
                progressDialog.show();
                Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>(){
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference("Customer");

                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Log.i("Test2",email);
                                number=(int)dataSnapshot.getChildrenCount();
                                //Log.i("Number of Children",number+"");
                                //number=Integer.parseInt(dataSnapshot.child("number_customer").getValue().toString());
                                int n=1;
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                                    String uname = postSnapshot.child("Username").getValue(String.class);
                                    //Log.i("uname",uname);
                                    if (uname.equals(email)) {
                                        //Log.i("Test4",flag+"");
                                        if (task.isSuccessful()) {
                                            flag = 1;
                                            progressDialog.dismiss();
                                            //Disconnection
                                            //myRef.child(n+"").child("Available").setValue("Online");
                                            //myRef.child(n+"").child("Available").onDisconnect().setValue("Disconnected");
                                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();


                                            Intent intent = new Intent(login.this, Subject.class);
                                            startActivity(intent);
                                            finish();
                                            }
                                        }
                                    if (n==number)
                                        break;
                                    n++;

                                    }
                                if (flag==0){
                                    progressDialog.dismiss();
                                    Toast.makeText(login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                });


            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(login.this, PasswordActivity.class));
            }
        });


    }}


