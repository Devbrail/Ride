package com.example.myride.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;


import com.android.volley.VolleyError;
import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.countrypicker.Country;
import com.example.myride.countrypicker.CountryPickerCallbacks;
import com.example.myride.countrypicker.CountryPickerDialog;
import com.example.myride.countrypicker.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    EditText username, password;
    String name, pass;
    TextView singup, forgot;
    Button login;
    ProgressBar progressBar;


    LinearLayout countrySelectLayout;
    String countryName;
    CountryPickerDialog countryPickerDialog;
    String countryCode;
    ImageView countryImage;
    TextView countryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        singup = findViewById(R.id.singup);
        login = findViewById(R.id.signin);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressdialog);
        forgot = findViewById(R.id.forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Forgot.class));
            }
        });


        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    login.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    name = username.getText().toString();
                    pass = password.getText().toString();

                    JSONObject logindetails = new JSONObject();
                    logindetails.put("userName", name);
                    logindetails.put("password", pass);
                    logindetails.put("rememberMe", true);


                    NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                    serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());

                    serviceCall.makeJSONObjectPostRequest(AppConstants.URL + AppConstants.USERLOGIN, logindetails, Request.Priority.IMMEDIATE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

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
                countryPickerDialog = new CountryPickerDialog(Login.this, new CountryPickerCallbacks() {
                    @Override
                    public void onCountrySelected(Country country, int flagResId) {
                        Log.i(TAG, "onCountrySelected: " + country.toString());
                        countryName = country.getIsoCode();
                        countryCode = country.getDialingCode();

                        setCountrylabel(countryName);


                    }
                });
                countryPickerDialog.show();

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

            Log.d(TAG, "onJSONObjectResponse: ");


            try {
                String savestatus = jsonObject.getString("lookUpUserStatusId");
                if (jsonObject != null && !savestatus.equals("null")) {


                    SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userid", jsonObject.getString("userId"));
                    editor.putString("phone", jsonObject.getString("userName"));
                    editor.putString("password", pass);

                    editor.apply();
                    Toast.makeText(Login.this, "Succes", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), Home.class));

                } else {
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                login.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }


        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse: ");
            Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();

            login.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);


        }

        @Override
        public void onStringResponse(String string) {
            Log.d(TAG, "onStringResponse: ");

        }
    }
}
