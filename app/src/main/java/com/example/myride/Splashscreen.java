package com.example.myride;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.myride.loginsignup.LoginSignup;

import java.util.Set;

public class Splashscreen extends AppCompatActivity {
ImageView splashView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        Set<String> set = prefs.getStringSet("profile", null);

        if(set!=null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(getApplicationContext(),Home.class));
                    finish();

                }
            }, 3000);
            }
        else {


            //  setContentView(layout);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(getApplicationContext(), LoginSignup.class));
                    finish();

                }
            }, 3000);
        }
    }
}
