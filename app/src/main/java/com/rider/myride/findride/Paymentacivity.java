package com.rider.myride.findride;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.rider.myride.R;

public class Paymentacivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentacivity);

        try {
            findViewById(R.id.ack).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
