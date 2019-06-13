package com.example.myride.basic;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.VolleyError;

import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.ApiCall;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.adpter.AutoSuggestAdapter;
import com.example.myride.loginsignup.Forgot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Profilecreate extends AppCompatActivity {

    EditText fName,lName,nin,town,dob,email;

    String namestring,stringnin,stringgender,stringdob,stringemail,stringtown;
    Boolean privacychecked=false;
    Button continu;
    ImageView profile;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoSuggestAdapter autoSuggestAdapter;
    private Handler handler;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    RadioGroup gender;
    private RadioButton radioSexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilecreate);

         fName=findViewById(R.id.firstName);
        lName=findViewById(R.id.lastName);
        nin=findViewById(R.id.natid);
        town=findViewById(R.id.town);
        dob=findViewById(R.id.dob);

        final Calendar myCalendar = Calendar.getInstance();

        final  DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat     sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob.setText (sdf.format(myCalendar.getTime()));


            }

        };




        email=findViewById(R.id.email);
        profile=findViewById(R.id.profile1);
        autoCompleteTextView =
                findViewById(R.id.town);
        gender=findViewById(R.id.gender);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new DatePickerDialog(Profilecreate.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        autoCompleteTextView.setThreshold(3);

        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        


    profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 0);//one can be replaced with any action code
        }
    });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString(),true);
                    }

                }
                return false;
            }
        });

    }

    private static final String TAG = "Profilecreate";

    private void makeApiCall(String text,Boolean b) {
        ApiCall.make(Profilecreate.this, text,b, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                List<String> paceid=new ArrayList<>();
                List<String> result = new ArrayList<>();
                String value;
                String[] val;
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("predictions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        //String list = row.getString("description")+","+row.getString("place_id");
                        // val = list.split(",");
                        //result.add(val[1]);
                        stringList.add(row.getString("description"));

                        Log.d(TAG, "onResponse: "+row.getString("description"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
            }
        });
    }

    public void onCheckboxClicked(View view) {


        privacychecked=((CheckBox) view).isChecked();


    }
String fname,lname;
    public void onContinueclicked(View view) {

        int selectedId=gender.getCheckedRadioButtonId();
        radioSexButton=(RadioButton)findViewById(selectedId);
        fname=fName.getText().toString();
        lname=lName.getText().toString();
        stringnin=nin.getText().toString();
       stringgender=(selectedId==1)?"Male":"Female";
        stringtown=town.getText().toString();
        stringdob=dob.getText().toString();
        stringemail=email.getText().toString();


        if(privacychecked){



            if(namestring!=null||stringnin!=null||stringgender!=null||stringdob!=null||stringtown!=null){


                if(profileBitmap!=null)
                {


                    try {


                        String profile64 = AppUtil.converttoBase64(profile);

                        SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


                        String userid=AppUtil.getuserid(getApplicationContext());

                        profileObject=new JSONObject();

                        profileObject.put("userId",userid);
                        profileObject.put("firstName",fname);
                        profileObject.put("lastName",lname);
                        profileObject.put("nin",stringnin);
                        profileObject.put("gender",stringgender);
                        profileObject.put("town",stringtown);
                        profileObject.put("dob",stringdob);
                        profileObject.put("userPic",profile64);


                        NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                        serviceCall.setOnServiceCallCompleteListener(new  onServiceCallCompleteListene());
                        serviceCall.makeJSONObjectPostRequest( AppConstants.URL+AppConstants.PROFILE_CREATE,profileObject, Request.Priority.IMMEDIATE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {

                    showSnackbar("Please select a profile picture",view);
                }


            }else {



                showSnackbar("Fields cannot be empty",view);
                v=view;
            }

        }else {

            showSnackbar("You must agree the privacy policy",view);
        }

    }

    JSONObject profileObject;

    View v;

    private void showSnackbar(String s, View view) {

        Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
    private class onServiceCallCompleteListene implements ServicesCallListener {

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                Log.d(TAG, "onJSONObjectResponse: ");



               if(jsonObject.getBoolean("saveStatus")) {

                   SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

                   SharedPreferences.Editor editor=sharedpreferences.edit();
                   editor.putString("profile",profileObject.toString());


                   startActivity(new Intent(getApplicationContext(), Home.class));
               }

               else
                   showSnackbar("Something went occured! please try again",v);

            } catch (JSONException e) {
                e.printStackTrace();
            }
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

Bitmap profileBitmap;
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

                        profileBitmap=bitmap;
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

    public void dobclicked(View view) {
    }
}
