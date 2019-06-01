package com.example.myride.OfferaRide;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myride.R;
 

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myride.Home;
import com.example.myride.Services.ApiCall;
import com.example.myride.Services.DownloadTask;
import com.example.myride.Utils.GetLocationAddress;
import com.example.myride.adpter.AutoSuggestAdapter;
import com.example.myride.findride.RideResults;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class OfferaRide extends AppCompatActivity implements
        OnMapReadyCallback        {

    private static final String TAG = "FindRide";

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler,handler1;
    private AutoSuggestAdapter autoSuggestAdapter;
    EditText price;


    GoogleMap mMap;




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Home.class));
    }
    AppCompatAutoCompleteTextView autoCompleteTextView,autoCompleteTextView1;
    EditText editText;
    SupportMapFragment mapFragment;
    NumberPicker availableSeats;
    String locationString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offera_ride);
        availableSeats=findViewById(R.id.number_picker);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("location")!= null){

            locationString=(bundle.getString("location"));
            latitude= Double.parseDouble(locationString.split("-")[0]);
            longitude=Double.parseDouble(locationString.split("-")[1]);


        }

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);



        autoCompleteTextView =
                findViewById(R.id.auto_complete_edit_text);
        String addresses= GetLocationAddress.getAddressLine(OfferaRide.this,new LatLng(latitude,longitude));
        currentLocation=new LatLng(latitude,longitude);
        autoCompleteTextView.setText(addresses);

        autoCompleteTextView1 =
                findViewById(R.id.auto_complete_edit_text2);
//        autoCompleteTextView.setThreshold(3);
//        autoCompleteTextView1.setThreshold(3);
        editText=findViewById(R.id.when);
        price=findViewById(R.id.price);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.smallmap);

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);


        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView1.setAdapter(autoSuggestAdapter);



        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        getLocationAPI(autoSuggestAdapter.getObject(position)) ;

                    }
                });


        autoCompleteTextView1.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        getLocationAPI(autoSuggestAdapter.getObject(position)) ;


//                        Log.d(TAG, "onItemClick: "+autoSuggestAdapter.getObject(position)+autoSuggestAdapter.getObject(position+1));
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

        autoCompleteTextView1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler1.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler1.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
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
        handler1 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView1.getText())) {
                        makeApiCall(autoCompleteTextView1.getText().toString(),true);
                    }

                }
                return false;
            }
        });







        mapFragment.getMapAsync(OfferaRide.this);



        ((Button)findViewById(R.id.continu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from=autoCompleteTextView.getText().toString();
                from=from.split(",")[0];
                String to=autoCompleteTextView1.getText().toString();
                to=to.split(",")[0];



                showAcknowledgement();
            }
        });


    }

    private void showAcknowledgement() {

/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
         View dialogLayout = inflater.inflate(R.layout.alert_completedofferride, null);

        builder.setView(dialogLayout);

        builder.show();*/

       final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_completedofferride);
        dialog.setCanceledOnTouchOutside(true);

        final Button btnReopenId = (Button) dialog.findViewById(R.id.ok);
        final TextView tv=dialog.findViewById(R.id.msg);
        final ProgressBar pb=dialog.findViewById(R.id.pbar);
        final ImageView imageView=dialog.findViewById(R.id.tick);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pb.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                tv.setText("Succesfully Completed offer ride");
                btnReopenId.setText("OK");
            }
        },3000);



        btnReopenId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnReopenId.getText().toString().equals("OK"))
                dialog.dismiss();
                finish();
                startActivity(new Intent(getApplicationContext(),Home.class));



            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    ProgressDialog progressBar;

    int progressBarStatus;
    double latitude=0;
    double longitude=0;

    LatLng currentLocation;







    private void getLocationAPI(String object) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(autoCompleteTextView1.getWindowToken(), 0);


        ApiCall.make(this, object,false, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                List<String> paceid=new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    Log.w(TAG, "onResponse: "+responseObject.toString());
                    JSONArray array = responseObject.getJSONArray("results");
                    JSONObject row=null;
                    JSONObject object1=null,object2=null;
                    for (int i = 0; i < array.length(); i++) {
                        row = array.getJSONObject(i);
                        Log.d(TAG, "onResponse: "+row);
                        object1 = row.getJSONObject("geometry");
                        object2=object1.getJSONObject("location");
                    }
                    String s=object2.getString("lat");
                    String lo=object2.getString("lng");
                    if(s.length()>5)
                        //  initMapFragment(Double.parseDouble(s),Double.parseDouble(lo));
                        Log.d(TAG, "onResponse: "+s+lo);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: "+e.getMessage());
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

    private void makeApiCall(String text,Boolean b) {
        ApiCall.make(this, text,b, new Response.Listener<String>() {
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
                    Log.w(TAG,responseObject.toString());
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
                    Log.e(TAG, "onResponse: "+e.getMessage());
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



    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    long time;
    public void whenclicked(View view) {

        final View dialogView =   View.inflate(OfferaRide.this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(OfferaRide.this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                time = calendar.getTimeInMillis();
                String datetime= String.valueOf(datePicker.getYear())+"-"+
                        String.valueOf(datePicker.getMonth())+"-"+
                        String.valueOf(datePicker.getDayOfMonth())+" "+
                        timePicker.getCurrentHour()+":"+
                        timePicker.getCurrentMinute()+"";
                editText.setText(datetime);
                alertDialog.dismiss();
            }});
        alertDialog.setView(dialogView);
        alertDialog.show();

    }
    DialogFragment dialogFragment;
    //
    ///==========  for map draw ================//
    private GoogleMap mMapplott;
    ArrayList markerPoints= new ArrayList();

    LatLng origin,dest;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        LatLng sydney = new LatLng(20.5937, 78.9629);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                markerPoints.add(latLng);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(latLng);

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (markerPoints.size() >= 2) {
                    origin = (LatLng) markerPoints.get(0);
                    autoCompleteTextView.setText(GetLocationAddress.getAddressLine(OfferaRide.this,origin));
                    dest = (LatLng) markerPoints.get(1);
                    autoCompleteTextView1.setText(GetLocationAddress.getAddressLine(OfferaRide.this,dest));
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(autoCompleteTextView1.getWindowToken(), 0);



                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);
                    Log.d(TAG, "onMapClick: "+url);
                    Log.wtf(TAG, "onMapClick: "+url);

                    DownloadTask downloadTask = new  DownloadTask(mMap,getApplicationContext());

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }

            }
        });

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" +
                parameters+"&key=AIzaSyC5GfpSETFXER-UznjaKJsr0QKxkQWufDg";


        return url;
    }
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        return true;
    }
}
