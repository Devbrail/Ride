package com.example.myride;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//
//import com.heetch.countrypicker.Country;
//import com.heetch.countrypicker.CountryPickerCallbacks;
//import com.heetch.countrypicker.CountryPickerDialog;
import com.rilixtech.CountryCodePicker;

public class Signup extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        countryCodePicker=findViewById(R.id.ccp);

//        CountryPickerDialog countryPicker =
//                new CountryPickerDialog(getApplicationContext(), new CountryPickerCallbacks() {
//                    @Override
//                    public void onCountrySelected(Country country, int flagResId) {
//
//
//                    }
//                });
//        countryPicker.show();



    }

    @Override
    public boolean onSupportNavigateUp() {
       // startActivity(new Intent(getApplicationContext(),LoginSignup.class));
        finish();
        return true;
    }
}
