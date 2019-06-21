package com.example.myride.basic;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.github.nikartm.support.StripedProcessButton;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class Insurance extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 123;
    ImageView choosePdf;

    TextView fileName;
    EditText company, expiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        choosePdf = findViewById(R.id.choosepdf);
        fileName = findViewById(R.id.fileName);
        company = findViewById(R.id.company);
        expiry = findViewById(R.id.expiry);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM-dd-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                expiry.setText(sdf.format(myCalendar.getTime()));


            }

        };
        final com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener onDateSetListener=new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                expiry.setText(simpleDateFormat.format(calendar.getTime()));

            }

        };

        expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  new DatePickerDialog(Insurance.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/


                Date date1=new Date();
                Calendar cal = Calendar.getInstance();

                new SpinnerDatePickerDialogBuilder()
                        .context(Insurance.this)
                        .callback(onDateSetListener)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate( cal.get( Calendar.YEAR)-1,cal.get( Calendar.MONTH)-1, cal.get( Calendar.DAY_OF_MONTH))
                        .maxDate(cal.get( Calendar.YEAR)+15,cal.get( Calendar.DAY_OF_MONTH), cal.get( Calendar.DAY_OF_WEEK))
                        .minDate(cal.get( Calendar.YEAR)-1,cal.get( Calendar.MONTH), cal.get( Calendar.DAY_OF_MONTH))
                        .build()
                        .show();


            }
        });
        choosePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select PDF file"),
                            REQUEST_PERMISSIONS);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Log.wtf("Insurance", ex.getMessage());


                }
            }
        });
    }

    File fileToUpload;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSIONS) {
            assert data != null;
            Uri uri = data.getData();
            File myFile = new File(Objects.requireNonNull(uri).toString());

            fileToUpload = myFile;

            fileName.setText(String.format("%s.pdf", myFile.getName()));
        }
    }

    String expirydat;

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        return true;
    }

    StripedProcessButton button;
    public void uploadFle(View view) {

        button=findViewById(R.id.stripedbutton);

        String compan = company.getText().toString();
        String expi = expiry.getText().toString();
        expi=expi+" 00:00:00";
        if (compan.length() > 2 && expi.length() > 2) {
            if (fileToUpload != null) {


                button.start();
                NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());

                HashMap<String, String> params = new HashMap<>();

                params.put("CarId", getCarid());
                params.put("InsuranceCompany", compan);
                params.put("ExpiryDate", expi);
                serviceCall.makeJSONObejctPostRequestMultipart(params, fileToUpload, fileToUpload.getName(), Request.Priority.IMMEDIATE);

                v = view;
            } else
                Toast.makeText(this, "make sure file uploaded", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
    }

    private String getCarid() {


        SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        return sharedpreferences.getString("carId", "0");

    }

    View v;
    private static final String TAG = "Insurance";



    private void showSnackbar(String s, View view) {

        Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
    private class onServiceCallCompleteListene implements ServicesCallListener {

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                Log.wtf(TAG, "onJSONObjectResponse: ");

                button.stop();

                if (jsonObject.has("insuranceId")) {
                    String insurenceid = jsonObject.getString("insuranceId");

                    SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("insuranceId", insurenceid);
                    editor.apply();


                    startActivity(new Intent(getApplicationContext(), Driver.class));
                } else
                    showSnackbar("Something went occured! please try again", v);


            } catch (Exception e) {
                button.stop();

                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            button.stop();
                showSnackbar("Something went occured! please try again", v);
            Toast.makeText(Insurance.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.wtf(TAG, "onErrorResponse: "+error.getMessage());
        }

        @Override
        public void onStringResponse(String string) {
            Log.wtf(TAG, "onStringResponse: ");
        }
    }

}
