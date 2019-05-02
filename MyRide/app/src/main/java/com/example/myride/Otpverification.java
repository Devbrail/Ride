package com.example.myride;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.nikartm.support.StripedProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

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



    private FirebaseAuth mAuth;
    private StripedProcessButton register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        register=findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.start();
            }
        });

        FirebaseApp.initializeApp(Otpverification.this);
        mAuth = FirebaseAuth.getInstance();


phoneNuber="+917012957601";
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNuber,
//                1,
//                TimeUnit.MINUTES,
//                Otpverification.this,
//                mCallBack
//        );





    }
    @Override
    public boolean onSupportNavigateUp() {
        // startActivity(new Intent(getApplicationContext(),LoginSignup.class));
        finish();
        return true;
    }
    private void resend_otp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNuber,
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

                            Toast.makeText(Otpverification.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
