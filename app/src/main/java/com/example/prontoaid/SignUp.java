package com.example.prontoaid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUp extends AppCompatActivity {
    Button r;
    FirebaseAuth auth;
    SharedPreferences sharedpreferences;
    ProgressDialog progressDialog;

    EditText iemail,ipassword,iphno,icpassword,iname;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        iemail = (EditText)findViewById(R.id.editText5);
        ipassword = (EditText)findViewById(R.id.editText10);
        iphno = (EditText)findViewById(R.id.editText8);
        icpassword = (EditText)findViewById(R.id.editText11) ;
        iname = (EditText)findViewById(R.id.editText3);

        /*Spinner spinner = (Spinner) findViewById(R.id.std);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        //spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("LKG");
        categories.add("UKG");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinner.setAdapter(dataAdapter);*/
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        final Context context= this;
        progressDialog = new ProgressDialog(this);

        r= (Button) findViewById(R.id.button3);
        //l= (Button) findViewById(R.id.r2lbtn);
          /* r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, login.class);
                startActivity(intent);
            }
        });*/
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=iemail.getText().toString().trim();
                final String password=ipassword.getText().toString().trim();
                String cpassword=icpassword.getText().toString().trim();
                final String name=iname.getText().toString().trim();
                final String number=iphno.getText().toString().trim();

                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(cpassword))
                {
                    Toast.makeText(getApplicationContext(), "Enter confirm password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(number))
                {
                    Toast.makeText(getApplicationContext(), "Enter phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6)
                {
                    Toast.makeText(getApplicationContext(), "Password too short!Enter minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(cpassword))
                {
                    Toast.makeText(getApplicationContext(), "passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Registering User Statement..");
                progressDialog.show();
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference("Customer");
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int number1 = (int)dataSnapshot.getChildrenCount();
                                    number1++;
                                    //myRef=myRef.getParent();
                                    myRef.child(number1+"").child("Name").setValue(name);
                                    myRef.child(number1+"").child("Phone_Number").setValue(number);
                                    myRef.child(number1+"").child("Username").setValue(email);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                            startActivity(new Intent(SignUp.this, login.class));
                            finish();
                        }
                    }
                });
                //progressDialog.dismiss();


            }
        });
        progressDialog.dismiss();

    }

}

