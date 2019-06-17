package com.example.myride.basic;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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


        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob.setText(sdf.format(myCalendar.getTime()));


            }

        };
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                ldate.setText(sdf.format(myCalendar.getTime()));


            }

        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Driver.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        ldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Driver.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

                        SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
                        Date newDate = spf.parse(stringdob);
                        Date newDate1 = spf.parse(liscencedate);
                        spf = new SimpleDateFormat("MM-dd-yyyy");
                        stringdob = spf.format(newDate);

                        liscencedate = spf.format(newDate1);


                        String profile64 = AppUtil.converttoBase64(profile);


                        String jsonString = AppUtil.getvehicledetails(getApplicationContext());

                        profileObject = new JSONObject();

                        profileObject.put("carId", getcarID());
                        profileObject.put("firstName", fname);
                        profileObject.put("lastName", lname);
                        profileObject.put("nin", stringnin);
                        profileObject.put("gender", stringgender);
                        profileObject.put("email", stringemail);

                        profileObject.put("dob", "2019-06-15T05:36:28.536Z");
                        profileObject.put("userPic", profile64);
                        profileObject.put("drivngLicence", liscenceNo);
                        profileObject.put("drivngLicenceExpiry", "2019-06-15T05:36:28.536Z");

                        System.out.println("driverdetails" + profileObject);

                        NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                        serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                        serviceCall.makeJSONObjectPostRequest(AppConstants.URL + AppConstants.SAVE_DRIVER, profileObject, Request.Priority.IMMEDIATE);

                        button.start();
                    } catch (JSONException e) {
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
                button.stop();
                Toast.makeText(Driver.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Log.wtf(TAG, "onJSONObjectResponse: ");
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            button.stop();
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
