package com.example.myride.basic;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.example.myride.Utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class Insurance extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS =123 ;
    ImageView choosePdf;

    TextView fileName;
    EditText company,expiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        choosePdf=findViewById(R.id.choosepdf);
        fileName=findViewById(R.id.fileName);
        company=findViewById(R.id.company);
        expiry=findViewById(R.id.expiry);

        final Calendar myCalendar = Calendar.getInstance();

        final  DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                expiry.setText (sdf.format(myCalendar.getTime()));


            }

        };


        expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Insurance.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                    Log.wtf("Insurance",ex.getMessage());


                }
            }
        });
    }
File fileToUpload;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_PERMISSIONS){
            assert data != null;
            Uri uri = data.getData();
            File myFile = new File(Objects.requireNonNull(uri).toString());

            fileToUpload=myFile;

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

    public void uploadFle(View view) {
        String compan=company.getText().toString();
        String expi=expiry.getText().toString();

        if(fileToUpload!=null){


            NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
           serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());

            HashMap<String,String> params=new HashMap<>();
            params.put("InsuranceId","0");
            params.put("CarId","4");
            params.put("InsuranceCompany","dfds");
            params.put("ExpiryDate","10-10-2015");
             serviceCall.makeJSONObejctPostRequestMultipart(params,fileToUpload,fileToUpload.getName(),Request.Priority.IMMEDIATE);

             v=view;
        }

    }
View v;
    private static final String TAG = "Insurance";


    private class onServiceCallCompleteListene implements ServicesCallListener {

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                Log.d(TAG, "onJSONObjectResponse: ");



                if(jsonObject.has("insuranceId")) {
                    String insurenceid = jsonObject.getString("insuranceId");


                    SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("insuranceId", insurenceid);


                    startActivity(new Intent(getApplicationContext(), Home.class));
                }else
                    showSnackbar("Something went occured! please try again",v);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private void showSnackbar(String s, View view) {

            Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {

            Log.e(TAG, "onErrorResponse: ");
        }

        @Override
        public void onStringResponse(String string) {
            Log.d(TAG, "onStringResponse: ");
        }
    }

}
