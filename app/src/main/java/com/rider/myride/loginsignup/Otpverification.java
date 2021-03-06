package com.rider.myride.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.github.nikartm.support.StripedProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rider.myride.R;
import com.rider.myride.Services.NetworkServiceCall;
import com.rider.myride.Services.ServicesCallListener;
import com.rider.myride.Utils.AppConstants;
import com.rider.myride.basic.Profilecreate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

public class Otpverification extends AppCompatActivity {
    private static final String TAG = "Otpverification";
    String verificationId;
    String phoneNuber;
    TextView resent, shownumber;
    EditText otp, password1, password2;
    String phone, language, otpcode, pass1, pass2, countrycode;
    boolean verified = false;
    String username;
    boolean buttonpressed = false;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private StripedProcessButton register;
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
            if (code != null) {
                verifyCode(code);
            }


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Crashlytics.logException(e);

            Log.e(TAG, "onVerificationFailed: "+e.getMessage() );
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            Log.e(TAG, "verifyCode: "+e.getMessage() );
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fabric.with(this, new Crashlytics());
        register = findViewById(R.id.register);
        otp = findViewById(R.id.otp);
        password1 = findViewById(R.id.pass1);
        password2 = findViewById(R.id.pass2);
        resent = findViewById(R.id.resent);
        shownumber = findViewById(R.id.shownumber);
        resent.setEnabled(false);
        resent.setFocusable(false);
        callcounterforresend();
        resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resent.isFocusable()) {
                    resend_otp();
                    Snackbar.make(v, "Verification code has sented", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otpcode = otp.getText().toString();
                pass1 = password1.getText().toString();
                pass2 = password2.getText().toString();
                if (otpcode.length() != 6) {
                    Snackbar.make(v, "Please enter a valid Verification Code", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    register.stop();
                } else {
                    if (!verified && !otpcode.isEmpty()) {
                        verifyCode(otpcode);
                        buttonpressed = true;
                    } else
                        gowithapi();

                }

            }
        });

        FirebaseApp.initializeApp(Otpverification.this);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            String[] splitText = {};
            String fulltext = intent.getStringExtra("data");
            splitText = fulltext.split("-");
            phone = splitText[1];
            countrycode = splitText[2];
            username = splitText[3];

            if (phone.length() > 9)
                shownumber.setText(shownumber.getText().toString() + phone);

            language = splitText[0];

        } else {
            language = "English";
            // phone="+919645529012";

        }

        try {


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,
                    1,
                    TimeUnit.MINUTES,
                    Otpverification.this,
                    mCallBack
            );


        } catch (Exception e) {
            Log.e(TAG, "onCreate: " + e.getMessage());

            Toast.makeText(Otpverification.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


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

                            verified = true;
                            if (buttonpressed) {
                                gowithapi();
                            }

                            register.stop();
                        } else {

                            Log.e(TAG, "onComplete: " + task.getException());
                            Toast.makeText(Otpverification.this, "Verification Failed! please try again", Toast.LENGTH_SHORT).show();
                            register.stop();

                        }
                    }
                });
    }

    private void gowithapi() {
        if (pass1.equals(pass2)) {


            try {
                register.start();
                JSONObject usercreation = new JSONObject();
                usercreation.put("userName", username);
                usercreation.put("phone", phone);
                usercreation.put("password", pass1);
                usercreation.put("lookUpUserStatusId", 1);
                usercreation.put("lookUpUserTypeId", 1);
                usercreation.put("saveStatus", true);



                NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                serviceCall.makeJSONObjectPostRequest(AppConstants.URL, usercreation, Request.Priority.IMMEDIATE);
            } catch (JSONException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
                register.stop();
            }


        } else {

            Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
            register.stop();
        }
    }

    private class onServiceCallCompleteListene implements ServicesCallListener {

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {

            Log.d(TAG, "onJSONObjectResponse: ");

            try {
                boolean savestatus;
                savestatus = jsonObject.getBoolean("saveStatus");
                if (savestatus) {

                    register.stop();
                    SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    String userId = jsonObject.getString("userId");
                    editor.putString("userid", userId);
                    editor.putString("phone", phone);
                    editor.putString("password", pass1);
                    editor.putString("countrycode", countrycode);

                    editor.apply();
                    Crashlytics.setUserIdentifier(userId);

                    Crashlytics.setUserName(phone);
                    startActivity(new Intent(getApplicationContext(), Profilecreate.class));


                } else {
                    register.stop();

                    Toast.makeText(Otpverification.this, "User Already Registerd with this number. please login to continue", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginSignup.class));
                }


            } catch (JSONException e) {
                Crashlytics.logException(e);
                register.stop();

                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "onErrorResponse: "+ error.getMessage() );
            Crashlytics.logException(error);
            register.stop();
            Toast.makeText(Otpverification.this, "Something went error occured", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStringResponse(String string) {

        }
    }


}
