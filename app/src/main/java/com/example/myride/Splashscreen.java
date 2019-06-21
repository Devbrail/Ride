package com.example.myride;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.myride.loginsignup.LoginSignup;

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

                String userid = sharedpreferences.getString("userid", "0");
                if (userid.equals("0")) {


                    startActivity(new Intent(getApplicationContext(), LoginSignup.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }
            }
        }, 3000);
//        SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
//
//        Set<String> set = prefs.getStringSet("profile", null);
//
//      ////  if(set!=null){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
////
////                    startActivity(new Intent(getApplicationContext(),Home.class));
////                    finish();
//
//                }
//            }, 3000);
//            }
//        else {
//
//
//            //  setContentView(layout);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    startActivity(new Intent(getApplicationContext(), LoginSignup.class));
//                    finish();
//
//                }
//            }, 5000);
//        }/
    }
}
