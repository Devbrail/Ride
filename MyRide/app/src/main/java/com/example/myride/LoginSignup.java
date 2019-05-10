package com.example.myride;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginSignup extends AppCompatActivity {
Button sigin,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        sigin=findViewById(R.id.sigin1);

        signup=findViewById(R.id.signup1);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),Signup.class));

            }
        });
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Login.class));


            }
        });

    }
    boolean doubleBackToExitPressedOnce=false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }


        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
