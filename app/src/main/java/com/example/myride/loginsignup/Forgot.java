package com.example.myride.loginsignup;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;


import com.android.volley.VolleyError;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
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

public class Forgot extends AppCompatActivity {

    EditText otp,password1,password2,phone;
    String phoneText,language,otpcode,pass1,pass2;
    Button register;
    TextView resent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        register=findViewById(R.id.register);
        otp=findViewById(R.id.otp);
        password1=findViewById(R.id.pass1);
        password2=findViewById(R.id.pass2);
        resent=findViewById(R.id.resent);
        phone=findViewById(R.id.phone);

        FirebaseApp.initializeApp(Forgot.this);
        mAuth = FirebaseAuth.getInstance();
        resent.setEnabled(false);
        resent.setFocusable(false);
        callcounterforresend();
        FirebaseApp.initializeApp(Forgot.this);
        mAuth = FirebaseAuth.getInstance();

        phone.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                        if (actionId == EditorInfo.IME_ACTION_DONE) {

                            phoneText=phone.getText().toString();


                            sentVerificationcode(phoneText);
                            InputMethodManager    imm = (InputMethodManager) getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(phone.getApplicationWindowToken(), 0);
                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });


        otp.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                        if (actionId == EditorInfo.IME_ACTION_DONE) {



                            verifyCode(otp.getText().toString().trim());
                            phoneText=phone.getText().toString();



                            InputMethodManager    imm = (InputMethodManager) getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(otp.getApplicationWindowToken(), 0);

                            //sentVerificationcode(phoneText);
                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });



        resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resent.isFocusable()){
                    resend_otp();
                    Snackbar.make(v, "Verification code has sented", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else {
                    Snackbar.make(v, "You should wait some more time", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(verified){

                    try {
                        JSONObject usercreation=new JSONObject();
                        usercreation.put("password",phoneText);
                        usercreation.put("lookUpUserStatusId",1);
                        usercreation.put("lookUpUserTypeId",1);
                        usercreation.put("saveStatus",true);


                        NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                        serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                        serviceCall.makeJSONObjectPostRequest( AppConstants.URL+AppConstants.FORGOT_PASSWORD,usercreation, Request.Priority.IMMEDIATE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    String otpText=otp.getText().toString();
                    if(!otpText.isEmpty()){
                        verifyCode(otpText);
                    }else {
                        Toast.makeText(Forgot.this, "Otp verification failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
    private void resend_otp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneText,
                1,
                TimeUnit.MINUTES,
                Forgot.this,
                mCallBack,
                mResendToken
        );
    }

    private class onServiceCallCompleteListene implements ServicesCallListener {



        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            Log.d(TAG, "onJSONObjectResponse: ");

            //TODO manage when geting response
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse: ");

        }

        @Override
        public void onStringResponse(String string) {
            Log.d(TAG, "onStringResponse: ");

        }
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

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

    private void sentVerificationcode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                1,
                TimeUnit.MINUTES,
                Forgot.this,
                mCallBack
        );
    }
    private FirebaseAuth mAuth;
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
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            verified=true;
                            Toast.makeText(Forgot.this, "OTP Verified", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(Forgot.this, "Verification Failed! please try again", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }
    boolean verified=false;
}
