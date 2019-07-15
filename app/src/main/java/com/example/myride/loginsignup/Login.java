package com.example.myride.loginsignup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.ConnectivityHelper;
import com.example.myride.countrypicker.Country;
import com.example.myride.countrypicker.CountryPickerCallbacks;
import com.example.myride.countrypicker.CountryPickerDialog;
import com.example.myride.countrypicker.Utils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private static final String DEVICE_OFFLINE_MESSAGE = "Please check your internet connection";
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
    String counryiso;
    boolean countryselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Fabric.with(this, new Crashlytics());
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
                    if (!ConnectivityHelper.isConnectedToNetwork(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), DEVICE_OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();

                        return;
                    }


                    name = username.getText().toString();
                    pass = password.getText().toString();

                    if (countryselected) {
                        if (name.isEmpty() && pass.isEmpty()) {
                            Snackbar.make(v, "Phone and password cannot be null", Snackbar.LENGTH_SHORT).show();


                        } else {
                            login.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);

                            JSONObject logindetails = new JSONObject();
                            logindetails.put("userName", countryCode + name);
                            logindetails.put("password", pass);
                            logindetails.put("rememberMe", true);

                            Log.wtf(TAG, "login json: " + logindetails.toString());

                            NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                            serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());

                            serviceCall.makeJSONObjectPostRequest(AppConstants.URL + AppConstants.USERLOGIN, logindetails, Request.Priority.IMMEDIATE);
                        }
                    } else {
                        Snackbar.make(v, "Please select Country", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    login.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

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
//            setCountrylabel("ke");


            countrySelectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countryPickerDialog = new CountryPickerDialog(Login.this, new CountryPickerCallbacks() {
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


    }

    private void setCountrylabel(String locale) {

        countryselected = true;
        counryiso = locale.toLowerCase();
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
                String savestatus = jsonObject.getString("lookUpUserStatusId");
                if (jsonObject != null && !savestatus.equals("null")) {


                    SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    String userId = jsonObject.getString("userId");
                    String phone = jsonObject.getString("userName");
                    editor.putString("userid", userId);
                    editor.putString("phone", phone);
                    editor.putString("password", pass);
                    editor.putString("countrycode", counryiso);


                    Crashlytics.setUserIdentifier(userId);

                    Crashlytics.setUserName(phone);

                    editor.apply();
                    Toast.makeText(Login.this, "Succes", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();

                } else {
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                Crashlytics.logException(e);
                login.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }


        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Crashlytics.logException(error);
            Log.wtf(TAG, "onErrorResponse: " + error.getMessage());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Login.this, "Something went wrong, Make sure\n you have proper internet connection", Toast.LENGTH_SHORT).show();

                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            });


        }

        @Override
        public void onStringResponse(String string) {
            Log.wtf(TAG, "onStringResponse: ");

        }
    }
}
