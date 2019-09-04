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
import com.example.asimkhan.eating.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
private Button signin_btn;
private TextView signup_txt;
private EditText phone_txt,pass_txt;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mref;
    private ProgressDialog mdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
        //user login funtionality...
        login();
        //handle the text which send the user from sign in page to sign up page...
        signup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent = new Intent(SignIn.this,Signup.class);
                startActivity(signupintent);
            }
        });
    }

    private void login() {
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.setMessage("please wait!!!");
                mdialog.show();
                final String phone = phone_txt.getText().toString();
                final String password = pass_txt.getText().toString();
                if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)){
                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if user exists....
                            if (dataSnapshot.child(phone).exists()) {
                                //get user informations....
                                User user = dataSnapshot.child(phone).getValue(User.class);
                                user.setPhone(phone);
                                if (user.getPassword().equals(password)) {
                                    Intent home_intent = new Intent(SignIn.this,Home.class);
                                    Common.currentuser = user;
                                    startActivity(home_intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignIn.this, "wrong password!!!", Toast.LENGTH_SHORT)
                                            .show();
                                    mdialog.dismiss();
                                }
                            }else {
                                Toast.makeText(SignIn.this, "user not exists!!!", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void init() {
     phone_txt = (EditText)findViewById(R.id.login_phone);
        pass_txt = (EditText)findViewById(R.id.login_password);
        signin_btn = (Button)findViewById(R.id.login_button);
        signup_txt = (TextView)findViewById(R.id.singup_text);
        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference("users");
        mdialog = new ProgressDialog(SignIn.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mdialog.dismiss();
    }
}
