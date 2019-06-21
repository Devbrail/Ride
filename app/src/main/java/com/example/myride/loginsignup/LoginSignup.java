package com.example.myride.loginsignup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myride.R;

public class LoginSignup extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE =101 ;
    Button sigin,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        sigin=findViewById(R.id.sigin1);

        signup=findViewById(R.id.signup1);

        checkPermission();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    startActivity(new Intent(getApplicationContext(),Signup.class));
                finish();}
                else
                    Snackbar.make(v, "Make sure permission are allowed\nif not do it manually in app setting  ", Snackbar.LENGTH_SHORT).show();


            }
        });
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()) {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
                else
                    Snackbar.make(v, "Make sure permission are allowed\nif not do it manually in app setting  ", Snackbar.LENGTH_SHORT).show();




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
    /**
     *    Case 1: User doesn't have permission
     *    Case 2: User has permission
     *
     *    Case 3: User has never seen the permission Dialog
     *    Case 4: User has denied permission once but he din't clicked on "Never Show again" check box
     *    Case 5: User denied the permission and also clicked on the "Never Show again" check box.
     *    Case 6: User has allowed the permission
     *
     */

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(LoginSignup.this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE    }, CAMERA_PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
