<<<<<<< HEAD:app/src/main/java/com/example/myride/basic/Profilecreate.java
package com.example.myride.basic;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.ApiCall;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.adpter.AutoSuggestAdapter;
import com.github.nikartm.support.StripedProcessButton;
import com.google.android.material.snackbar.Snackbar;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Profilecreate extends AppCompatActivity {

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private static final String TAG = "Profilecreate";
    EditText fName, lName, nin, town, dob, email;
    String namestring, stringnin, stringgender, stringdob, stringemail, stringtown;
    Boolean privacychecked = false;
    Button continu;
    ImageView profile;
    RadioGroup gender;
    boolean viewstatus = false;
    ProgressDialog progressBar;
    Button register;
    LinearLayout privacy;
    String fname, lname;
    StripedProcessButton button;
    RadioButton male, female;
    JSONObject profileObject;
    View v;
    boolean generclicked = false;
    Bitmap profileBitmap;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoSuggestAdapter autoSuggestAdapter;
    private Handler handler;
    private RadioButton radioSexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilecreate);

        initView();

        progressBar = new ProgressDialog(Profilecreate.this, R.style.Theme_MaterialComponents_Dialog);
        progressBar.setCancelable(false);

        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//you can cancel it by pressing back button
//        progressBar.setMessage("Please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ;
        Intent intent = getIntent();
        if (intent.hasExtra("view")) {
            viewstatus = intent.getBooleanExtra("view", false);
            if (viewstatus) {


                changeuiState(false);


                progressBar.show();


                NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                serviceCall.makeGetrequest(AppConstants.URL + "/" + AppUtil.getuserid(this));


            }


        }


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


        autoCompleteTextView =
                findViewById(R.id.town);
        gender = findViewById(R.id.gender);
        final com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener onDateSetListener = new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                dob.setText(simpleDateFormat.format(calendar.getTime()));

            }

        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SpinnerDatePickerDialogBuilder()
                        .context(Profilecreate.this)
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


        autoCompleteTextView.setThreshold(3);

        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);

        autoCompleteTextView.setAdapter(autoSuggestAdapter);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 0);//one can be replaced with any action code
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
                        makeApiCall(autoCompleteTextView.getText().toString(), true);
                    }

                }
                return false;
            }
        });

    }

    private void changeuiState(boolean b) {

        fName.setFocusable(b);
        lName.setFocusable(b);
        nin.setFocusable(b);
        town.setFocusable(b);
        email.setFocusable(b);
        dob.setEnabled(b);


        if (b) {
            fName.setFocusableInTouchMode(b);
            lName.setFocusableInTouchMode(b);
            nin.setFocusableInTouchMode(b);
            town.setFocusableInTouchMode(b);
            email.setFocusableInTouchMode(b);

        }


        if (!b) {
            // profile.setVisibility(View.GONE);
            profile.setEnabled(b);
            register.setVisibility(View.GONE);
            privacy.setVisibility(View.GONE);
        } else {
            profile.setVisibility(View.VISIBLE);
            profile.setEnabled(true);
            profile.setFocusable(true);
            profile.setClickable(true);
            register.setVisibility(View.VISIBLE);
            privacy.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        nin = findViewById(R.id.natid);
        town = findViewById(R.id.town);
        dob = findViewById(R.id.dob);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        email = findViewById(R.id.email);
        profile = findViewById(R.id.profile1);
        register = findViewById(R.id.Continue);
        privacy = findViewById(R.id.privacy);
    }

    private void makeApiCall(String text, Boolean b) {
        ApiCall.make(Profilecreate.this, text, b, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                List<String> paceid = new ArrayList<>();
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

                        Log.wtf(TAG, "onResponse: " + row.getString("description"));
                    }
                } catch (Exception e) {
            Crashlytics.logException(e);
e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error) {
    Crashlytics.logException(error);;;Log.wtf(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
    }

    public void onCheckboxClicked(View view) {


        privacychecked = ((CheckBox) view).isChecked();


    }

    public void onContinueclicked(View view) throws ParseException {

        button = findViewById(view.getId());
        v = view;
        int selectedId = gender.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);

        fname = fName.getText().toString();
        lname = lName.getText().toString();
        namestring = fname + " " + lname;
        stringnin = nin.getText().toString();
        stringtown = town.getText().toString();
        stringdob = dob.getText().toString();
        stringemail = email.getText().toString();


        if (privacychecked) {


            if (namestring != null && stringnin != null && generclicked && stringdob != null && stringtown != null) {


                if (profileBitmap != null) {


                    try {


                        stringgender = (selectedId == 1) ? "Male" : "Female";

                        SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
                        Date newDate = spf.parse(stringdob);
                        spf = new SimpleDateFormat("MM-dd-yyyy");
                        stringdob = spf.format(newDate);


                        button.start();

                        String profile64 = AppUtil.converttoBase64(profile);


                        String userid = AppUtil.getuserid(getApplicationContext());

                        profileObject = new JSONObject();

                        profileObject.put("userId", userid);
                        profileObject.put("firstName", fname);
                        profileObject.put("lastName", lname);
                        profileObject.put("nin", stringnin);
                        profileObject.put("gender", stringgender);
                        profileObject.put("town", stringtown);
                        profileObject.put("dob", stringdob);
                        profileObject.put("userPic", profile64);

                        Log.wtf(TAG, "profileobject: " + profileObject.toString());

                        NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                        serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                        serviceCall.makeJSONObjectPostRequest(AppConstants.URL + AppConstants.PROFILE_CREATE, profileObject, Request.Priority.IMMEDIATE);

                    } catch (JSONException e) {
            Crashlytics.logException(e);
button.stop();
                        e.printStackTrace();
                    }


                } else {

                    showSnackbar("Please select a profile picture", view);
                }


            } else {


                showSnackbar("Fields cannot be empty", view);

            }

        } else {

            showSnackbar("You must agree the privacy policy", view);
            view.setVisibility(View.VISIBLE);

        }

    }

    private void showSnackbar(String s, View view) {

        Snackbar.make(findViewById(R.id.profilelayout), s, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void genderclicked(View view) {
        generclicked = true;

    }

    String convertDateformat(String date) {

        try {


            String s[] = date.split("T");
            date = s[0] + ":" + s[1];
            return s[0];
        } catch (Exception e) {
            Crashlytics.logException(e);
return date;
        }
    }

    private void showalertforaddprofile() {

        changeuiState(true);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);

        builder.setMessage("Please update your profile");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                changeuiState(true);


            }
        });
        builder.show();


    }

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

                        profileBitmap = bitmap;
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

    private class onServiceCallCompleteListene implements ServicesCallListener {

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                Log.wtf(TAG, "onJSONObjectResponse: ");

                if (viewstatus) {


                    if (jsonObject.has("userDetails") && !jsonObject.get("userDetails").equals(null)) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("userDetails");
                        if (jsonObject1 != null) {

                            fName.setText(jsonObject1.getString("firstName"));
                            lName.setText(jsonObject1.getString("lastName"));
                            String doo = convertDateformat(jsonObject1.getString("dob"));
                            dob.setText(doo);
                            nin.setText(jsonObject1.getString("nin"));
                            town.setText(jsonObject1.getString("town"));
                            String gender = jsonObject1.getString("gender");

                            String emal = jsonObject.getString("email");
                            if (emal != null && !emal.equals("null"))
                                email.setText(emal);
                            if (gender.equals("male"))
                                male.toggle();
                            else
                                female.toggle();

                            String userImage = jsonObject1.getString("userImage");


                            if (userImage != null && userImage.contains("jpg")) {
                                Glide.with(getApplicationContext())
                                        .load(AppConstants.host + AppConstants.UserImages + userImage)
                                        .placeholder(R.drawable.caricon)
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(profile);


                            }


                            progressBar.dismiss();


                        }
                    } else {


                        progressBar.dismiss();

                        showalertforaddprofile();
                        viewstatus = false;


                    }


                } else {

                    button.stop();

                    if (jsonObject.getBoolean("saveStatus")) {

                        SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


                        startActivity(new Intent(getApplicationContext(), Home.class));
                    } else
                        showSnackbar("Something went occured! please try again", v);
                }

            } catch (Exception e) {
            Crashlytics.logException(e);
if (!viewstatus)
                    button.stop();
                else
                    progressBar.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.wtf(TAG, "onJSONObjectResponse: " + e.getMessage());

                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse (VolleyError error) {
    Crashlytics.logException(error);;;if (!viewstatus)
                button.stop();
            else
                progressBar.dismiss();
            showSnackbar("Something went occured! please try again", v);
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            Log.wtf(TAG, "onErrorResponse: " + error.getMessage());
        }

        @Override
        public void onStringResponse(String string) {
            Log.wtf(TAG, "onStringResponse: ");
        }
    }


}
=======
package com.rider.myride.basic;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.github.nikartm.support.StripedProcessButton;
import com.google.android.material.snackbar.Snackbar;
import com.rider.myride.Home;
import com.rider.myride.R;
import com.rider.myride.Services.ApiCall;
import com.rider.myride.Services.NetworkServiceCall;
import com.rider.myride.Services.ServicesCallListener;
import com.rider.myride.Utils.AppConstants;
import com.rider.myride.Utils.AppUtil;
import com.rider.myride.adpter.AutoSuggestAdapter;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Profilecreate extends AppCompatActivity {

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private static final String TAG = "Profilecreate";
    EditText fName, lName, nin, town, dob, email;
    String namestring, stringnin, stringgender, stringdob, stringemail, stringtown;
    Boolean privacychecked = false;
    Button continu;
    ImageView profile;
    RadioGroup gender;
    boolean viewstatus = false;
    ProgressDialog progressBar;
    Button register;
    LinearLayout privacy;
    String fname, lname;
    StripedProcessButton button;
    RadioButton male, female;
    JSONObject profileObject;
    View v;
    boolean generclicked = false;
    Bitmap profileBitmap;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoSuggestAdapter autoSuggestAdapter;
    private Handler handler;
    private RadioButton radioSexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilecreate);

        initView();

        progressBar = new ProgressDialog(Profilecreate.this, R.style.Theme_MaterialComponents_Dialog);
        progressBar.setCancelable(false);

        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//you can cancel it by pressing back button
//        progressBar.setMessage("Please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Intent intent = getIntent();
        if (intent.hasExtra("view")) {
            viewstatus = intent.getBooleanExtra("view", false);
            if (viewstatus) {


                changeuiState(false);


                progressBar.show();


                NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                serviceCall.makeGetrequest(AppConstants.URL + "/" + AppUtil.getuserid(this));


            }


        }


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


        autoCompleteTextView =
                findViewById(R.id.town);
        gender = findViewById(R.id.gender);
        final com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener onDateSetListener = new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                dob.setText(simpleDateFormat.format(calendar.getTime()));

            }

        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SpinnerDatePickerDialogBuilder()
                        .context(Profilecreate.this)
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


        autoCompleteTextView.setThreshold(3);

        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);

        autoCompleteTextView.setAdapter(autoSuggestAdapter);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 0);//one can be replaced with any action code
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
                        makeApiCall(autoCompleteTextView.getText().toString(), true);
                    }

                }
                return false;
            }
        });

    }

    private void changeuiState(boolean b) {

        fName.setFocusable(b);
        lName.setFocusable(b);
        nin.setFocusable(b);
        town.setFocusable(b);
        email.setFocusable(b);
        dob.setEnabled(b);


        if (b) {
            fName.setFocusableInTouchMode(b);
            lName.setFocusableInTouchMode(b);
            nin.setFocusableInTouchMode(b);
            town.setFocusableInTouchMode(b);
            email.setFocusableInTouchMode(b);

        }


        if (!b) {
            // profile.setVisibility(View.GONE);
            profile.setEnabled(b);
            register.setVisibility(View.GONE);
            privacy.setVisibility(View.GONE);
        } else {
            profile.setVisibility(View.VISIBLE);
            profile.setEnabled(true);
            profile.setFocusable(true);
            profile.setClickable(true);
            register.setVisibility(View.VISIBLE);
            privacy.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        nin = findViewById(R.id.natid);
        town = findViewById(R.id.town);
        dob = findViewById(R.id.dob);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        email = findViewById(R.id.email);
        profile = findViewById(R.id.profile1);
        register = findViewById(R.id.Continue);
        privacy = findViewById(R.id.privacy);
    }

    private void makeApiCall(String text, Boolean b) {
        ApiCall.make(Profilecreate.this, text, b, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                List<String> paceid = new ArrayList<>();
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

                        Log.d(TAG, "onResponse: " + row.getString("description"));
                    }
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Crashlytics.logException(error);
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
    }

    public void onCheckboxClicked(View view) {


        privacychecked = ((CheckBox) view).isChecked();


    }

    public void onContinueclicked(View view) throws ParseException {

        button = findViewById(view.getId());
        v = view;
        int selectedId = gender.getCheckedRadioButtonId();
        radioSexButton = findViewById(selectedId);

        fname = fName.getText().toString();
        lname = lName.getText().toString();
        namestring = fname + " " + lname;
        stringnin = nin.getText().toString();
        stringtown = town.getText().toString();
        stringdob = dob.getText().toString();
        stringemail = email.getText().toString();


        if (privacychecked) {


            if (namestring != null && stringnin != null && generclicked && !stringdob.isEmpty() && stringtown != null) {


                if (profileBitmap != null) {


                    try {


                        stringgender = (selectedId == 1) ? "Male" : "Female";

                        SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
                        Date newDate = spf.parse(stringdob);
                        spf = new SimpleDateFormat("MM-dd-yyyy");
                        stringdob = spf.format(newDate);


                        button.start();

                        String profile64 = AppUtil.converttoBase64(profile);


                        String userid = AppUtil.getuserid(getApplicationContext());

                        profileObject = new JSONObject();

                        profileObject.put("userId", userid);
                        profileObject.put("firstName", fname);
                        profileObject.put("lastName", lname);
                        profileObject.put("nin", stringnin);
                        profileObject.put("gender", stringgender);
                        profileObject.put("town", stringtown);
                        profileObject.put("dob", stringdob);
                        profileObject.put("userPic", profile64);

                        Log.d(TAG, "profileobject: " + profileObject.toString());

                        NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                        serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                        serviceCall.makeJSONObjectPostRequest(AppConstants.URL + AppConstants.PROFILE_CREATE, profileObject, Request.Priority.IMMEDIATE);

                    } catch (JSONException e) {
                        Crashlytics.logException(e);
                        button.stop();
                        e.printStackTrace();
                    }


                } else {

                    showSnackbar("Please select a profile picture", view);
                }


            } else {


                showSnackbar("Fields cannot be empty", view);

            }

        } else {

            showSnackbar("You must agree the privacy policy", view);
            view.setVisibility(View.VISIBLE);

        }

    }

    private void showSnackbar(String s, View view) {

        Snackbar.make(findViewById(R.id.profilelayout), s, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void genderclicked(View view) {
        generclicked = true;

    }

    String convertDateformat(String date) {

        try {


            String[] s = date.split("T");
            date = s[0] + ":" + s[1];
            return s[0];
        } catch (Exception e) {
            Crashlytics.logException(e);
            return date;
        }
    }

    private void showalertforaddprofile() {

        changeuiState(true);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);

        builder.setMessage("Please update your profile");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                changeuiState(true);


            }
        });
        builder.show();


    }

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

                        profileBitmap = bitmap;
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

    private class onServiceCallCompleteListene implements ServicesCallListener {

        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            try {
                Log.d(TAG, "onJSONObjectResponse: ");

                if (viewstatus) {


                    if (jsonObject.has("userDetails") && !jsonObject.get("userDetails").equals(null)) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("userDetails");
                        if (jsonObject1 != null) {

                            fName.setText(jsonObject1.getString("firstName"));
                            lName.setText(jsonObject1.getString("lastName"));
                            String doo = convertDateformat(jsonObject1.getString("dob"));
                            dob.setText(doo);
                            nin.setText(jsonObject1.getString("nin"));
                            town.setText(jsonObject1.getString("town"));
                            String gender = jsonObject1.getString("gender");

                            String emal = jsonObject.getString("email");
                            if (emal != null && !emal.equals("null"))
                                email.setText(emal);
                            if (gender.equals("male"))
                                male.toggle();
                            else
                                female.toggle();

                            String userImage = jsonObject1.getString("userImage");


                            if (userImage != null && userImage.contains("jpg")) {
                                Glide.with(getApplicationContext())
                                        .load(AppConstants.host + AppConstants.UserImages + userImage)
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(profile);


                            }


                            progressBar.dismiss();


                        }
                    } else {


                        progressBar.dismiss();

                        showalertforaddprofile();
                        viewstatus = false;


                    }


                } else {

                    button.stop();

                    if (jsonObject.getBoolean("saveStatus")) {

                        SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


                        startActivity(new Intent(getApplicationContext(), Home.class));
                    } else
                        showSnackbar("Something went occured! please try again", v);
                }

            } catch (Exception e) {
                Crashlytics.logException(e);
                if (!viewstatus)
                    button.stop();
                else
                    progressBar.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onJSONObjectResponse: " + e.getMessage());

                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Crashlytics.logException(error);
            if (!viewstatus)
                button.stop();
            else
                progressBar.dismiss();
            showSnackbar("Something went occured! please try again", v);
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onErrorResponse: " + error.getMessage());
        }

        @Override
        public void onStringResponse(String string) {
            Log.d(TAG, "onStringResponse: ");
        }
    }


}
>>>>>>> 4f76eb22a123de7cb959c7a8907a0ebd1c435bfa:app/src/main/java/com/rider/myride/basic/Profilecreate.java
