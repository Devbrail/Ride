package com.example.myride.OfferaRide;

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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offera_ride);
        availableSeats=findViewById(R.id.number_picker);



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
        autoCompleteTextView1 =
                findViewById(R.id.auto_complete_edit_text2);
//        autoCompleteTextView.setThreshold(3);
//        autoCompleteTextView1.setThreshold(3);
        editText=findViewById(R.id.when);

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





        String addresses= GetLocationAddress.getAddressLine(OfferaRide.this,new LatLng(latitude,longitude));
        autoCompleteTextView.setText(addresses);
        currentLocation=new LatLng(latitude,longitude);

        mapFragment.getMapAsync(OfferaRide.this);



        ((Button)findViewById(R.id.continu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from=autoCompleteTextView.getText().toString();
                from=from.split(",")[0];
                String to=autoCompleteTextView1.getText().toString();
                to=to.split(",")[0];



                Intent intent=new Intent(OfferaRide.this, RideResults.class);
                intent.putExtra("numbers", editText.getText().toString());
                startActivity(intent);

            }
        });


    }
    ProgressDialog progressBar;

    int progressBarStatus;
    double latitude=0;
    double longitude=0;

    LatLng currentLocation;


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


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
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key=AIzaSyBXVlsVMU9Uzy6jJZ7lbnLxpocMzA-id5g";


        return url;
    }
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        return true;
    }
}