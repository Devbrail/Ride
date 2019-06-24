package com.example.myride.findride;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.example.myride.Home;
import com.example.myride.R;

public class Paymentacivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentacivity);

        try {
            ((TextView)findViewById(R.id.ack)).setOnClickListener(new View.OnClickListener() {
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

finish();    }
}
