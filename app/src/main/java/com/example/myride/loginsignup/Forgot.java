package com.example.myride.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;


import com.android.volley.VolleyError;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.countrypicker.Country;
import com.example.myride.countrypicker.CountryPickerCallbacks;
import com.example.myride.countrypicker.CountryPickerDialog;
import com.example.myride.countrypicker.Utils;
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

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Forgot extends AppCompatActivity {

    private static final String TAG = "Otpverification";
    EditText otp, password1, password2, phone;
    String phoneText, language, otpcode, pass1, pass2;
    StripedProcessButton register;
    TextView resent;
    LinearLayout countrySelectLayout;
    String verificationId;
    String phoneNuber;
    boolean verified = false;
    String countryName;
    CountryPickerDialog countryPickerDialog;
    String countryCode;
    ImageView countryImage;
    TextView countryView;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
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
            Log.wtf(TAG, "onVerificationCompleted phonecred" + phoneAuthCredential + "getSmsCode " + code);
            if (code != null) {
                verifyCode(code);
            }


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Crashlytics.logException(e);

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        register = findViewById(R.id.register);
        otp = findViewById(R.id.otp);
        password1 = findViewById(R.id.pass1);
        password2 = findViewById(R.id.pass2);
        resent = findViewById(R.id.resent);
        phone = findViewById(R.id.phone);
        countryImage = findViewById(R.id.countryImage);
        countryView = findViewById(R.id.countryName);
        countrySelectLayout = findViewById(R.id.countrySelectLayout);
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        String locale = tm.getNetworkCountryIso();

        if (locale.length() > 0)
            setCountrylabel(locale);
        else
            setCountrylabel("ke");


        countrySelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPickerDialog = new CountryPickerDialog(Forgot.this, new CountryPickerCallbacks() {
                    @Override
                    public void onCountrySelected(Country country, int flagResId) {
                        Log.wtf(TAG, "onCountrySelected: " + country.toString());
                        countryName = country.getIsoCode();
                        countryCode = country.getDialingCode();

                        setCountrylabel(countryName);


                    }
                });
                countryPickerDialog.show();

            }
        });


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

                            phoneText = countryCode + phone.getText().toString();


                            sentVerificationcode(phoneText);
                            InputMethodManager imm = (InputMethodManager) getSystemService(
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


                            try {
                                verifyCode(otp.getText().toString().trim());
                                phoneText = phone.getText().toString();


                                InputMethodManager imm = (InputMethodManager) getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(otp.getApplicationWindowToken(), 0);
                            } catch (Exception e) {
           Crashlytics.logException(e);e.printStackTrace();
                            }

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
                if (resent.isFocusable()) {
                    resend_otp();
                    Snackbar.make(v, "Verification code has sented", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(v, "You should wait some more time", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verified) {

                    try {

                        pass1=password1.getText().toString();
                        pass2=password2.getText().toString();
                        if(pass1.equals(pass2)) {
                            register.start();
                            JSONObject usercreation = new JSONObject();
                            usercreation.put("phone", phoneText);
                            usercreation.put("password", pass1);


                            Log.wtf(TAG, "onClick: " + usercreation.toString());
                            Log.wtf(TAG, "onClick: " + usercreation.toString());
                            NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                            serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                            serviceCall.makeJSONObjectPostRequest(AppConstants.URL + AppConstants.FORGOT_PASSWORD, usercreation, Request.Priority.IMMEDIATE);
                        }else
                            Toast.makeText(Forgot.this, "Password must be same", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
           Crashlytics.logException(e);e.printStackTrace();
                    }


                } else {
                    String otpText = otp.getText().toString();
                    if (!otpText.isEmpty()) {
                        verifyCode(otpText);
                    } else {
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

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e) {
           Crashlytics.logException(e);e.printStackTrace();
        }
    }

    private void sentVerificationcode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                1,
                TimeUnit.MINUTES,
                Forgot.this,
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

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            verified = true;
                            Toast.makeText(Forgot.this, "OTP Verified", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Forgot.this, "Verification Failed! please try again", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    private void setCountrylabel(String locale) {

        String drawableName = locale + "_flag";
        countryImage.setImageResource(Utils.getMipmapResId(getApplicationContext(), drawableName));
        countryView.setText(new Locale(getApplicationContext().getResources().getConfiguration().locale.getLanguage(),
                locale).getDisplayCountry());
        countryCode = "+" + Utils.getCountrycode(this, locale);
    }

    private class onServiceCallCompleteListene implements ServicesCallListener {


        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            Log.wtf(TAG, "onJSONObjectResponse: ");


            try {
                if (jsonObject.has("saveStatus")&&jsonObject.getBoolean("saveStatus")){
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Password updated succesfully", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    register.stop();

                    startActivity(new Intent(Forgot.this,Login.class));

                }else {
                    register.stop();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Something went occured. please try again", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            } catch (JSONException e) {
           Crashlytics.logException(e);register.stop();
                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse (VolleyError error) {
    Crashlytics.logException(error);;;Log.wtf(TAG, "onErrorResponse: ");
            register.stop();

        }

        @Override
        public void onStringResponse(String string) {
            Log.wtf(TAG, "onStringResponse: ");

        }
    }
}
