package com.example.myride.loginsignup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
 import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myride.R;
import com.example.myride.countrypicker.Country;
import com.example.myride.countrypicker.CountryPickerCallbacks;
import com.example.myride.countrypicker.CountryPickerDialog;
import com.example.myride.countrypicker.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public class Signup extends AppCompatActivity {

     Country country;
     ImageView countryImage;
     TextView countryView;
     LinearLayout countrySelectLayout;
     CountryPickerDialog countryPickerDialog;
    private static final String TAG = "Signup";
    String countryName,countryCode,languageChoosed;
    EditText chooseLanguage,phone;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         countryImage=findViewById(R.id.countryImage);
        countryView=findViewById(R.id.countryName);
        chooseLanguage=findViewById(R.id.chooseLanguage);
        phone=findViewById(R.id.phone);

        countrySelectLayout=findViewById(R.id.countrySelectLayout);
        register=findViewById(R.id.register);
        chooseLanguage.setKeyListener(null);

        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        String locale =tm.getNetworkCountryIso();

        if(locale.length()>0)
        setCountrylabel(locale);
        else
            setCountrylabel("ke");


        Log.wtf(TAG, "onCreate: "+ Arrays.toString(Resources.getSystem().getAssets().getLocales()));



        final SortedSet<String> allLanguages = new TreeSet<>();
        String[] languages = Locale.getISOLanguages();
        for (int i = 0; i < languages.length; i++){
            Locale loc = new Locale(languages[i]);
            allLanguages.add(loc.getDisplayLanguage());
        }




chooseLanguage.setFocusable(false);
         chooseLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(chooseLanguage.getWindowToken(), 0);

                //final String[]  languageCodes=allLanguages.toArray(new String[allLanguages.size()]) ;
                final  String[] languageCodes={"English","Kiswahili","French","Arab","Mandarin"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                builder.setItems(languageCodes, new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //languageChoosed=allLanguages
                        languageChoosed=languageCodes[which];
                        chooseLanguage.setText(languageChoosed);
                        dialog.dismiss();


                    }
                }).show();

            }
        });

        countrySelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPickerDialog=new CountryPickerDialog(Signup.this, new CountryPickerCallbacks()    {
                    @Override
                    public void onCountrySelected(Country country, int flagResId) {
                        Log.wtf(TAG, "onCountrySelected: "+country.toString());
                        countryName=country.getIsoCode();
                         countryCode=country.getDialingCode();

                        setCountrylabel(countryName);


                     }
                });
                countryPickerDialog.show();

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworked()) {

                    if(phone.length()>8) {
                        String finalText = languageChoosed + "-" + countryCode.toLowerCase()+ phone.getText().toString()+"-"+countryName+"-"+phone.getText().toString();
                        Intent intent = new Intent(Signup.this, Otpverification.class);
                        intent.putExtra("data", finalText);
                        startActivity(intent);
                    }else {
                        Snackbar.make(v, "Please enter a valid Mobile number", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }else {

                    Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });


//        String drawableName = country.getIsoCode().toLowerCase(Locale.ENGLISH) + "_flag";
/*
        CountryPickerDialog countryPicker =
                new CountryPickerDialog(getApplicationContext(), new CountryPickerCallbacks() {
                    @Override
                    public void onCountrySelected(Country country, int flagResId) {


                    }
                });
        countryPicker.show();*/
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

    private boolean isNetworked() {
        try {
            final String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;

        } catch (Exception e) {
            return false;
        }
    }

    private void setCountrylabel(String locale) {

        countryName=locale;
        String drawableName = locale + "_flag";
        countryImage.setImageResource(Utils.getMipmapResId(getApplicationContext(), drawableName));
        countryView.setText(new Locale(getApplicationContext().getResources().getConfiguration().locale.getLanguage(),
                locale).getDisplayCountry());
        countryCode="+"+Utils.getCountrycode(this,locale);
    }

    @Override
    public boolean onSupportNavigateUp() {
       // startActivity(new Intent(getApplicationContext(),LoginSignup.class));
        finish();
        return true;
    }




    private List<Country> countries;
    private CountryPickerCallbacks callbacks;
    private ListView listview;
    private String headingCountryCode;
    private boolean showDialingCode;



}
