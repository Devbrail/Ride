package com.rider.myride.findride;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rider.myride.Home;
import com.rider.myride.R;
import com.rider.myride.Services.ApiCall;
import com.rider.myride.Services.DownloadTask;
import com.rider.myride.Utils.GetLocationAddress;
import com.rider.myride.adpter.AutoSuggestAdapter;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class FindRide extends AppCompatActivity implements
        OnMapReadyCallback {

    private static final String TAG = "FindRide";

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    GoogleMap mMap;
    AppCompatAutoCompleteTextView autoCompleteTextView, autoCompleteTextView1;
    EditText editText;

    SupportMapFragment mapFragment;
    String locationString;
    int progressBarStatus;
    double latitude = 0;
    double longitude = 0;
    LatLng currentLocation = null;
    long time;
    String date;
    ArrayList markerPoints = new ArrayList();
    LatLng origin, dest;
    private Handler handler, handler1;
    private AutoSuggestAdapter autoSuggestAdapter;
    //
    ///==========  for map draw ================//
    private GoogleMap mMapplott;
    ProgressBar progressbarfind,progressbar_offer;

    ImageView finderror,offererror;
    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride);

        Intent intent = getIntent();
        initView();

        mapFragment.getMapAsync(this);


        if (intent.hasExtra("location")) {

            locationString = (intent.getStringExtra("location"));
            latitude = Double.parseDouble(locationString.split("-")[0]);
            longitude = Double.parseDouble(locationString.split("-")[1]);

        }
        if (latitude != 0) {
            currentLocation = new LatLng(latitude, longitude);

            String addresses = GetLocationAddress.getAddressLine(FindRide.this, new LatLng(latitude, longitude));
            autoCompleteTextView.setText(addresses);
        }

        hideKeyboard();


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

                        getLocationAPI(autoSuggestAdapter.getObject(position));

                    }
                });


        autoCompleteTextView1.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        getLocationAPI(autoSuggestAdapter.getObject(position));

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
                        makeApiCall(autoCompleteTextView.getText().toString(), true);
                        progressbarfind.setVisibility(View.VISIBLE);
                        finderror.setVisibility(View.INVISIBLE);
                    }else
                        progressbarfind.setVisibility(View.INVISIBLE);

                }
                return false;
            }
        });
        handler1 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView1.getText())) {
                        makeApiCall(autoCompleteTextView1.getText().toString(), true);
                        progressbar_offer.setVisibility(View.VISIBLE);
                        offererror.setVisibility(View.INVISIBLE);

                    }else

                        progressbar_offer.setVisibility(View.INVISIBLE);

                }
                return false;
            }
        });


        findViewById(R.id.continu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = autoCompleteTextView.getText().toString();
                from = from.split(",")[0];
                String to = autoCompleteTextView1.getText().toString();
                to = to.split(",")[0];

                String when = editText.getText().toString();
                String fullText = from + "_" + to + "_" + editText.getText().toString();
                if (when.length() > 3 && from.length() > 3 && to.length() > 3) {

                    Intent intent = new Intent(FindRide.this, RideResults.class);
                    intent.putExtra("numbers", fullText);
                    startActivity(intent);
                }

            }
        });


    }

    private void hideKeyboard() {
        //initMapFragment(11.2763223, 76.2234366);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private void initView() {


        autoCompleteTextView = findViewById(R.id.auto_complete_edit_text);
        autoCompleteTextView1 = findViewById(R.id.auto_complete_edit_text2);
        progressbarfind = findViewById(R.id.progressbar_find);
        progressbar_offer = findViewById(R.id.progressbar_offer);
        finderror=findViewById(R.id.finderror);
        offererror=findViewById(R.id.offererror);


        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView1.setThreshold(1);
        editText = findViewById(R.id.when);
        FragmentManager fm = getSupportFragmentManager();/// getChildFragmentManager();

        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.smallmap);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.smallmap, mapFragment).commit();
        }

    }

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


        ApiCall.make(this, object, false, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                List<String> paceid = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    Log.w(TAG, "onResponse: " + responseObject.toString());
                    JSONArray array = responseObject.getJSONArray("results");
                    JSONObject row = null;
                    JSONObject object1 = null, object2 = null;
                    for (int i = 0; i < array.length(); i++) {
                        row = array.getJSONObject(i);
                        Log.d(TAG, "onResponse: " + row);
                        object1 = row.getJSONObject("geometry");
                        object2 = object1.getJSONObject("location");
                    }
                    String s = object2.getString("lat");
                    String lo = object2.getString("lng");
                    if (s.length() > 5)
                        //  initMapFragment(Double.parseDouble(s),Double.parseDouble(lo));
                        Log.d(TAG, "onResponse: " + s + lo);

                } catch (Exception e) {

                    Crashlytics.logException(e);
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
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

    private void makeApiCall(String text, Boolean b) {
        ApiCall.make(this, text, b, new Response.Listener<String>() {
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
                    Log.d(TAG, responseObject.toString());

                    if(responseObject.has("error_message")){

                        Log.e(TAG, "onResponse: query overlimit" );
                        if(progressbar_offer.isShown()){
                            progressbar_offer.setVisibility(View.INVISIBLE);
                            offererror.setVisibility(View.VISIBLE);

                        }
                        if(progressbarfind.isShown()){
                            progressbarfind.setVisibility(View.INVISIBLE);
                            finderror.setVisibility(View.VISIBLE);

                        }
                    }

                    JSONArray array = responseObject.getJSONArray("predictions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        //String list = row.getString("description")+","+row.getString("place_id");
                        // val = list.split(",");
                        //result.add(val[1]);
                        stringList.add(row.getString("description"));

                        progressbar_offer.setVisibility(View.INVISIBLE);
                        progressbarfind.setVisibility(View.INVISIBLE);

                        Log.d(TAG, "onResponse: " + row.getString("description"));
                    }
                } catch (Exception e) {

                        if(progressbar_offer.isShown()){
                            progressbar_offer.setVisibility(View.INVISIBLE);
                            offererror.setVisibility(View.VISIBLE);

                        }
                        if(progressbar_offer.isShown()){
                            progressbarfind.setVisibility(View.INVISIBLE);
                            finderror.setVisibility(View.VISIBLE);

                        }

                     Crashlytics.logException(e);
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void whenclicked(View view) {

        final com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener onDateSetListener = new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                editText.setText(simpleDateFormat.format(calendar.getTime()));
                date = simpleDateFormat.format(calendar.getTime()) + " 00:00:00";


            }

        };
        Calendar cal = Calendar.getInstance();

        new SpinnerDatePickerDialogBuilder()
                .context(FindRide.this)
                .callback(onDateSetListener)
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(true)
                .showDaySpinner(true)
                .defaultDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, cal.get(Calendar.DAY_OF_MONTH))
                .maxDate(cal.get(Calendar.YEAR) + 2, cal.get(Calendar.MONTH) + 5, cal.get(Calendar.DAY_OF_WEEK))
                .minDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .build()
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        LatLng sydney = new LatLng(20.5937, 78.9629);
        if (currentLocation != null) {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(currentLocation, 16)));

        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(sydney, 16)));

        }


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
                    autoCompleteTextView.setText(GetLocationAddress.getAddressLine(FindRide.this, origin));
                    dest = (LatLng) markerPoints.get(1);
                    autoCompleteTextView1.setText(GetLocationAddress.getAddressLine(FindRide.this, dest));
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(autoCompleteTextView1.getWindowToken(), 0);


                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);
                    Log.d(TAG, "onMapClick: " + url);
                    Log.d(TAG, "onMapClick: " + url);

                    DownloadTask downloadTask = new DownloadTask(mMap, getApplicationContext());

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
                parameters + "&key=AIzaSyBc2Wdvg-4DcDG7ZijN9C7aS6u1Driw-xQ";


        return url;
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();
        return true;
    }
}
