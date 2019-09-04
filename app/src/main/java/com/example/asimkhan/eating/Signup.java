package com.example.asimkhan.eating;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asimkhan.eating.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    private EditText phone,name,pass,conf_pass;
    private TextView sign_txt;
    private Button signup_btn;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mref;
    private ProgressDialog mdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        //handle the intent going from sign up page to login page...
        sign_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this,SignIn.class);
                startActivity(intent);
            }
        });
        //handle the signup button...
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        final String user_phone = phone.getText().toString();
        final String user_name  =name.getText().toString();
        final String user_pass  = pass.getText().toString();
        String user_conf_pass = conf_pass.getText().toString();
        if(!TextUtils.isEmpty(user_phone)&&!TextUtils.isEmpty(user_name)&&!TextUtils.isEmpty(user_pass)
                &&!TextUtils.isEmpty(user_conf_pass)) {
            if (user_pass.length() >=8) {
                if (user_pass.equals(user_conf_pass)) {
                    mdialog.setMessage("Please wait...");
                    mdialog.show();
                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(user_phone).exists()) {
                                mdialog.dismiss();
                                Toast.makeText(Signup.this, "Phone Number Already registered",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                            else{
                                mdialog.dismiss();
                                User user = new User(user_name, user_pass);
                                mref.child(user_phone).setValue(user);
                                Toast.makeText(Signup.this, "Sign Up successfully",
                                Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Signup.this,MainActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }else {
                Toast.makeText(Signup.this,"password length should" +
                        " be eight character or maximum...",Toast.LENGTH_LONG).show();
            }
        }
            }
        });
    }

    private void init() {
        phone = (EditText)findViewById(R.id.signup_phone);
        name = (EditText)findViewById(R.id.signup_user_name);
        pass = (EditText)findViewById(R.id.signup_password);
        conf_pass = (EditText)findViewById(R.id.signup_conf_pass);
        sign_txt  =(TextView)findViewById(R.id.singup_text);
        signup_btn = (Button)findViewById(R.id.signup_button);
        //mauth = FirebaseAuth.getInstance();
        //current_user = mauth.getCurrentUser().getUid();
        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference("users");
        mdialog = new ProgressDialog(Signup.this);

    }
}
