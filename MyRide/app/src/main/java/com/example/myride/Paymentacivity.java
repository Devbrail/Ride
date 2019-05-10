package com.example.myride;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Paymentacivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentacivity);

        try {
            ((TextView)findViewById(R.id.ack)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),Home.class));
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(getApplicationContext(),Home.class));
    }
}
