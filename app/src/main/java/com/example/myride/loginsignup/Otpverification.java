package com.example.myride.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;


import com.android.volley.VolleyError;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.basic.Profilecreate;
import com.github.nikartm.support.StripedProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Otpverification extends AppCompatActivity {
    private PhoneAuthProvider.ForceResendingToken mResendToken;
String verificationId;
    private static final String TAG = "Otpverification";
    String phoneNuber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            mResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            otp.setText(code);
            Log.d(TAG, "onVerificationCompleted phonecred" + phoneAuthCredential + "getSmsCode " + code);
            if (code != null)
            {
                verifyCode(code);
            }


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }



        TextView resent,shownumber;
    private FirebaseAuth mAuth;
    private StripedProcessButton register;
 EditText otp,password1,password2;
    String phone,language,otpcode,pass1,pass2,countrycode;
    boolean verified=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        register=findViewById(R.id.register);
        otp=findViewById(R.id.otp);
        password1=findViewById(R.id.pass1);
        password2=findViewById(R.id.pass2);
        resent=findViewById(R.id.resent);
        shownumber=findViewById(R.id.shownumber);
        resent.setEnabled(false);
        resent.setFocusable(false);
        callcounterforresend();
        resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resent.isFocusable()){
                    resend_otp();
                    Snackbar.make(v, "Verification code has sented", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.start();
                otpcode=otp.getText().toString();
                pass1=password1.getText().toString();
                pass2=password2.getText().toString();
                if(otpcode.length()!=6){
                    Snackbar.make(v, "Please enter a valid Verification Code", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    register.stop();
                }else {
                    if(!verified) {
                        verifyCode(otpcode);
                        buttonpressed = true;
                    }
                    else
                        gowithapi();

                }

            }
        });

        FirebaseApp.initializeApp(Otpverification.this);
        mAuth = FirebaseAuth.getInstance();

        Intent intent=getIntent();
        if(intent!=null){
            String[] splitText={};
            String fulltext=intent.getStringExtra("data");
            splitText=fulltext.split("-");
            phone=splitText[1];
            countrycode=splitText[2];

            if(phone.length()>9)
                shownumber.setText(shownumber.getText().toString()+phone);

            language=splitText[0];

        }else {
            language="English";
           // phone="+919645529012";

        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                1,
                TimeUnit.MINUTES,
                Otpverification.this,
                mCallBack
        );





    }

    private void callcounterforresend() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
               resent.setEnabled(true);
               resent.setFocusable(true);
            }

        }.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // startActivity(new Intent(getApplicationContext(),LoginSignup.class));
        finish();
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void resend_otp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                1,
                TimeUnit.MINUTES,
                Otpverification.this,
                mCallBack,
                mResendToken
        );
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            verified=true;
                             if(buttonpressed){
                                gowithapi();
                            }

                            register.stop();
                        }else {
                            Toast.makeText(Otpverification.this, "Verification Failed! please try again", Toast.LENGTH_SHORT).show();
                            register.stop();

                        }
                    }
                });
    }
    boolean buttonpressed=false;
    private void gowithapi() {
        if(pass1.equals(pass2)){

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    SharedPreferences sharedpreferences = getSharedPreferences("ride", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString("countrycode", countrycode);
//
//                    editor.commit();
//
//
//                    startActivity(new Intent(getApplicationContext(), Profilecreate.class) );
//
//                }
//            },3000);


            try {
                JSONObject usercreation=new JSONObject();
                usercreation.put("userName","9048337605");
                usercreation.put("phone","9048337605");
                usercreation.put("password",pass1);
                usercreation.put("lookUpUserStatusId",1);
                usercreation.put("lookUpUserTypeId",1);
                usercreation.put("saveStatus",true);


                NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                serviceCall.makeJSONObjectPostRequest( AppConstants.URL,usercreation, Request.Priority.IMMEDIATE);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else {
            Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
            register.stop();
        }
    }
    private class onServiceCallCompleteListene implements ServicesCallListener {

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {

            Log.d(TAG, "onJSONObjectResponse: ");

            try {
                boolean savestatus;
                savestatus=jsonObject.getBoolean("saveStatus");
                if(savestatus){


                    SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userid", jsonObject.getString("userId"));
                    editor.putString("phone", phone);
                    editor.putString("password", pass1);

                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), Profilecreate.class));





                }else {
                    Toast.makeText(Otpverification.this, "User Already Registerd with this number. please login to continue", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),LoginSignup.class));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void onStringResponse(String string) {

        }
    }


}
