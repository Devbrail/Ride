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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Driver extends AppCompatActivity {

    ImageView profile;
    EditText fName,lName,nin,lno,ldate,dob,email;

    String namestring,stringnin,stringgender,stringdob,stringemail,stringlno,stringldate;
    Boolean privacychecked=false;
    Button continu;
    RadioGroup gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        profile=findViewById(R.id.profile1);

        fName=findViewById(R.id.firstName);
        lName=findViewById(R.id.lastName);
        nin=findViewById(R.id.natid);
        lno=findViewById(R.id.liscence);
        ldate=findViewById(R.id.liscenceExpiry);
        email=findViewById(R.id.email);
        gender=findViewById(R.id.gender);

        dob=findViewById(R.id.dob);
        continu=findViewById(R.id.Continue);


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

                dob.setText (sdf.format(myCalendar.getTime()));


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





    }


    public void onCheckboxClicked(View view) {

        privacychecked=((CheckBox) view).isChecked();

    }
String fname,lname,liscenceNo,liscencedate;
    JSONObject profileObject;
    public void onContinueclicked(View view) {
        if(privacychecked){


            int selectedId=gender.getCheckedRadioButtonId();
             fname=fName.getText().toString();
            lname=lName.getText().toString();
            stringnin=nin.getText().toString();
            stringgender=(selectedId==1)?"Male":"Female";
            liscenceNo=lName.getText().toString();
            liscencedate=ldate.getText().toString();
            stringdob=dob.getText().toString();
            stringemail=email.getText().toString();


            if(driverimage!=null){

                try {


                    String profile64 = AppUtil.converttoBase64(profile);

                    SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

                    String userid=sharedpreferences.getString("userId",null);
//TODO  check all

                    profileObject=new JSONObject();

                    profileObject.put("userId",userid);
                    profileObject.put("firstName",fname);
                    profileObject.put("lastName",lname);
                    profileObject.put("nin",stringnin);
                    profileObject.put("gender",stringgender);
                    profileObject.put("town","");
                    profileObject.put("dob",stringdob);
                    profileObject.put("userPic",profile64);


//                    NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
//                    serviceCall.setOnServiceCallCompleteListener(new Profilecreate.onServiceCallCompleteListene());
//                    serviceCall.makeJSONObjectPostRequest( AppConstants.URL+AppConstants.PROFILE_CREATE,profileObject, Request.Priority.IMMEDIATE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                showSnackbar("please add a profile image",view);
            }


        }else {
            Snackbar.make(view, "You have to agree the terms and conditions", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
    private void showSnackbar(String s, View view) {

        Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
    public void uploadImageClicked(View view) {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 0);

    }
Bitmap driverimage;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){


                    try {
                        Uri selectedImage = null;
                        selectedImage = data.getData();

                        Bitmap bitmap =Bitmap.createScaledBitmap( MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage), 200,200, false);

                        driverimage=bitmap;
                        profile.setImageBitmap(bitmap );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
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
