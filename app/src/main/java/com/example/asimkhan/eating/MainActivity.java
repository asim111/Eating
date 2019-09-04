package com.example.asimkhan.eating;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
private Button signin_btn,signup_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin_btn = (Button)findViewById(R.id.signinbtn);
        signup_btn = (Button)findViewById(R.id.signupbtn);
        //sign in button handling...
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinintent = new Intent(MainActivity.this,SignIn.class);
                startActivity(signinintent);
            }
        });
        //sing up button handling...
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent = new Intent(MainActivity.this,Signup.class);
                startActivity(signupintent);
            }
        });
    }
}
