package com.example.myride.basic;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.myride.Home;
import com.example.myride.OfferaRide.OfferaRide;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.github.nikartm.support.StripedProcessButton;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Driver extends AppCompatActivity {

    ImageView profile;
    EditText fName, lName, nin, lno, ldate, dob, email;

    String namestring, stringnin, stringgender, stringdob, stringemail, stringlno, stringldate;
    Boolean privacychecked = false;
    Button continu;
    RadioGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        profile = findViewById(R.id.profile1);




        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        nin = findViewById(R.id.natid);
        lno = findViewById(R.id.liscence);
        ldate = findViewById(R.id.liscenceExpiry);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);

        dob = findViewById(R.id.dob);
        dob.setFocusable(false);
        ldate.setFocusable(false);
        continu = findViewById(R.id.Continue);

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        Bitmap bmp = AppUtil.getbmpfromURL("https://cdn.pixabay.com/photo/2015/03/04/22/35/head-659651_960_720.png");
                        profile.setImageBitmap(bmp);
                    } catch (Exception e) {
            Crashlytics.logException(e);
e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            Crashlytics.logException(e);
e.printStackTrace();
        }
        final Calendar myCalendar = Calendar.getInstance();


        final com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener onDateSetListener=new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                dob.setText(simpleDateFormat.format(calendar.getTime()));

            }

        };
        final com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener onDateSetListener1=new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                ldate.setText(simpleDateFormat.format(calendar.getTime()));

            }

        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar cal = Calendar.getInstance();

                new SpinnerDatePickerDialogBuilder()
                        .context(Driver.this)
                        .callback(onDateSetListener)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(1990, 6, 1)
                        .maxDate(2008, 0, 1)
                        .minDate(1960, 0, 1)
                        .build()
                        .show();
            }
        });


        ldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new SpinnerDatePickerDialogBuilder()
                        .context(Driver.this)
                        .callback(onDateSetListener1)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate( cal.get( Calendar.YEAR)+1,cal.get( Calendar.MONTH)-1, cal.get( Calendar.DAY_OF_MONTH))
                        .maxDate(cal.get( Calendar.YEAR)+20,cal.get( Calendar.DAY_OF_MONTH), cal.get( Calendar.DAY_OF_WEEK))
                        .minDate(cal.get( Calendar.YEAR)-1,cal.get( Calendar.MONTH), cal.get( Calendar.DAY_OF_MONTH))
                        .build()
                        .show();
            }
        });


    }


    public void onCheckboxClicked(View view) {

        privacychecked = ((CheckBox) view).isChecked();

    }

    String fname, lname, liscenceNo, liscencedate;
    JSONObject profileObject;
    StripedProcessButton button;

    public void onContinueclicked(View view) throws ParseException {
        if (privacychecked) {

            button = findViewById(view.getId());
            int selectedId = gender.getCheckedRadioButtonId();
            fname = fName.getText().toString();
            lname = lName.getText().toString();
            stringnin = nin.getText().toString();
            stringgender = (selectedId == 1) ? "Male" : "Female";
            liscenceNo = lno.getText().toString();
            liscencedate = ldate.getText().toString();
            stringdob = dob.getText().toString();
            stringemail = email.getText().toString();


            if (fname.length() > 3 && stringnin.length() > 3 && generclicked && liscenceNo.length() > 3 && liscencedate.length() > 3 && stringdob.length() > 3) {
                if (driverimage != null) {

                    try {





                        String profile64 = AppUtil.converttoBase64(profile);


                        String jsonString = AppUtil.getvehicledetails(getApplicationContext());

                        profileObject = new JSONObject();

                        profileObject.put("carId", getcarID());
                        profileObject.put("firstName", fname);
                        profileObject.put("lastName", lname);
                        profileObject.put("nin", stringnin);
                        profileObject.put("gender", stringgender);
                        profileObject.put("email", stringemail);

                        profileObject.put("dob", stringdob);
                        profileObject.put("userPic", profile64);
                        profileObject.put("drivngLicence", liscenceNo);
                        profileObject.put("drivngLicenceExpiry", liscencedate);

                        System.out.println("driverdetails" + profileObject);

                        NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                        serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                        serviceCall.makeJSONObjectPostRequest(AppConstants.URL + AppConstants.SAVE_DRIVER, profileObject, Request.Priority.IMMEDIATE);

                        button.start();
                    } catch (JSONException e) {
            Crashlytics.logException(e);
e.printStackTrace();
                        System.out.println(e);
                    }

                } else {
                    showSnackbar("please add a profile image", view);
                }
            } else
                showSnackbar("Fields cannot be empty", view);


        } else {
            Snackbar.make(view, "You have to agree the terms and conditions", Snackbar.LENGTH_SHORT)
                    .show();
        }


    }

    private String getcarID() {

        SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        return sharedpreferences.getString("carId", "0");

    }

    private static final String TAG = "Driver";
    boolean generclicked = false;

    public void genderclicked(View view) {
        generclicked = true;

    }

    private class onServiceCallCompleteListene implements ServicesCallListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {

            try {
                button.stop();

                profileObject.put("driverId", jsonObject.getString("driverId"));

                SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("driver", profileObject.toString());


                startActivity(new Intent(getApplicationContext(), OfferaRide.class));

            } catch (Exception e) {
            Crashlytics.logException(e);
button.stop();
                Toast.makeText(Driver.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Log.wtf(TAG, "onJSONObjectResponse: ");
        }

        @Override
        public void onErrorResponse (VolleyError error) {
    Crashlytics.logException(error);;;button.stop();
            showSnackbar("Something happend, please try again",findViewById(android.R.id.content) );

            Toast.makeText(Driver.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            Log.wtf(TAG, "onErrorResponse: " + error);
        }

        @Override
        public void onStringResponse(String string) {

        }
    }


    private void showSnackbar(String s, View view) {

        Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void uploadImageClicked(View view) {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 0);

    }

    Bitmap driverimage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {


                    try {
                        Uri selectedImage = null;
                        selectedImage = data.getData();

                        Bitmap bitmap = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage), 200, 200, false);

                        driverimage = bitmap;
                        profile.setImageBitmap(bitmap);
                    } catch (Exception e) {
            Crashlytics.logException(e);
e.printStackTrace();
                    }

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    imageview.setImageURI(selectedImage);
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        return true;
    }
}
