package com.example.prontoaid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class login extends AppCompatActivity {
    Button r,l;
    EditText iemail,ipassword;
    FirebaseAuth Auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        Auth = FirebaseAuth.getInstance();
        setContentView(R.layout.login);
        iemail = (EditText) findViewById(R.id.editText);
        ipassword = (EditText) findViewById(R.id.editText2);
        Auth = FirebaseAuth.getInstance();

        addListenerOnButton();
    }
    public void addListenerOnButton() {
        final Context context= this;
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
                String email = iemail.getText().toString();
                final String password = ipassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>(){
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful())
                        {
                            if (password.length() < 6) {
                                ipassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login.this, Subject.class);
                            startActivity(intent);
                            finish();
                        }
                    };

                });
            }
        });
    }}


