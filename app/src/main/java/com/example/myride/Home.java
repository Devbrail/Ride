package com.example.myride;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myride.Listener.Interfaces;
import com.example.myride.OfferaRide.OfferaRide;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.Utils.MyJsonArrayRequest;
import com.example.myride.adpter.gridAdapter;
import com.example.myride.basic.Profilecreate;
import com.example.myride.basic.VehicleDetails;
import com.example.myride.findride.FindRide;
import com.example.myride.loginsignup.LoginSignup;
import com.example.myride.model.CardetailsPOJO;
import com.example.myride.permisions.Permisions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements Interfaces {
    private static final String TAG = "Home";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 88;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    public boolean allset = false;
    protected Location mLastLocation;
    ArrayList<String> basicFields;
    gridAdapter adapter;
    GridView gv;
    boolean doubleBackToExitPressedOnce = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        basicFields = new ArrayList<>();
        gv = (GridView) findViewById(R.id.grid);
        basicFields.add("Find a ride");
        basicFields.add("Offer a ride");
        adapter = new gridAdapter(this, basicFields, getApplicationContext(), this);
        gv.setAdapter(adapter);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        progressBar = new ProgressDialog(Home.this,R.style.Theme_MaterialComponents_Dialog);
        progressBar.setCancelable(false);

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//you can cancel it by pressing back button
//        progressBar.setMessage("Please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        decodeImage();

        if (!Permisions.hasSelfPermissions(getApplicationContext(), new String[]{Permisions.ACCES_STORAGE, Permisions.WRITE_STORAGE, Permisions.ACCESS_COARSE_LOCATION, Permisions.ACCESS_FINE_LOCATION, Permisions.CAMERA})) {

            Toast.makeText(this, "Please enable all the permisions", Toast.LENGTH_SHORT).show();

        } else {
            if (!isNetworkConnected())
                shownetworkAlert();
            else if (!isgpsenabled())
                buildAlertMessageNoGps();


        }

        SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        String cardetails=sharedpreferences.getString("cardetails",null);

        if(cardetails==null) {


            checkOfferirdeEligibility();
            progressBar.show();
        }
        else {
            try {
                fromcach=true;
                JSONObject jsonObject=new JSONObject(cardetails);
                this.cardetails=jsonObject;
                handleJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void decodeImage() {

        String s="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\"";
        byte[] bitmap= Base64.decode(s,Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);


    }

    boolean fromcach=false;

    boolean isCarsavedStatus=false;
    boolean isInsurancesavedStatus=false;
    boolean isDrivesaveStatus=false;

    String totalseat="7";
    private void handleJson(JSONObject jsonObject) {

        try {

            progressBar.dismiss();


            if(jsonObject.has("carName")&&!jsonObject.get("carName").equals(null))
            {

                isCarsavedStatus=true;
                String carId = jsonObject.getString("carId");
                String carName = jsonObject.getString("carName");
                String carNumber = jsonObject.getString("carNumber");
                String carModel = jsonObject.getString("carModel");
                String carColor = jsonObject.getString("carColor");
                String seatNumber = jsonObject.getString("seatNumber");
                totalseat=seatNumber;
                String userId = jsonObject.getString("userId");
                String carImage = jsonObject.getString("carImage");

                if (jsonObject.has("insurance")&&!jsonObject.get("insurance").equals("null")){

                    isInsurancesavedStatus=true;
                    JSONObject insurance = jsonObject.getJSONObject("insurance");
                    String insuranceId = insurance.getString("insuranceId");
                    String insuranceCompany = insurance.getString("insuranceCompany");
                    if(insuranceCompany.equals("null")){

                        isInsurancesavedStatus=false;
                    }
                    String expiryDate = insurance.getString("expiryDate");

                    if (jsonObject.has("driver")&&!jsonObject.get("driver").equals(null)){

                        isDrivesaveStatus=true;
                        JSONObject driver = jsonObject.getJSONObject("driver");



                        String driverId = driver.getString("driverId");
                        String driverName = driver.getString("firstName") + " " + driver.getString("lastName");
                        String nin = driver.getString("nin");
                        String gender = driver.getString("gender");
                        String email = driver.getString("email");
                        String dob = driver.getString("dob");
                        String userPic = driver.getString("userIamge");
                        String drivngLicence = driver.getString("drivngLicence");
                        String drivngLicenceExpiry = driver.getString("drivngLicenceExpiry");

                        if(gender.equals("null")){

                            isDrivesaveStatus=false;
                        }

                        CardetailsPOJO cardetailsPOJO = new CardetailsPOJO(carId, carName, carNumber,
                                carModel, carColor, seatNumber, userId, carImage, insuranceId, insuranceCompany, expiryDate, driverId,
                                driverName, nin, gender, email, dob, userPic, drivngLicence, drivngLicenceExpiry);


                        cardetailsPOJOArrayList.add(cardetailsPOJO);


                            SharedPreferences sharedpreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedpreferences.edit();

                            editor.putString("carId",carId);
                            editor.putString("cardetails",cardetails.toString());
                            editor.putString("noOfseast",totalseat);
                            editor.apply();


                    }

                }

            }










        } catch (Exception e) {
            progressBar.dismiss();
            progressBar.cancel();
            e.printStackTrace();
        }


    }

    private void showAlert(String s, final Class clas) {


        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);

        builder.setMessage("Please update your profile");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Intent intent=new Intent(getApplicationContext(),clas);
                startActivity(intent);


            }
        });
        builder.show();

    }

    private void checkOfferirdeEligibility() {


        String userid = AppUtil.getuserid(this);
        makearrayrequest(AppConstants.URL + AppConstants.GET_CAR + "/" + userid);


    }

    public void makearrayrequest(String url) {


        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                try {


                    Log.d(TAG, "onResponse: "+response);

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        cardetails=jsonObject;


                        handleJson(jsonObject);

                    }


                } catch (Exception e) {
                    progressBar.dismiss();
                    progressBar.cancel();

                    e.printStackTrace();
                    Log.wtf(TAG, "onResponse: " + e.getMessage());
                }

                Log.wtf("onResponse", "" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                progressBar.cancel();

                error.printStackTrace();
                Log.wtf("onErrorResponse", error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error)   {


            }
        });
        requestQueue.add(request);
    }

ArrayList<CardetailsPOJO> cardetailsPOJOArrayList=new ArrayList<>();
    boolean carsavedStatus = false;


    JSONObject cardetails;

    @Override
    protected void onStart() {
        super.onStart();

        if (isgpsenabled()) {

            getLastLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case  R.id.myprofile:

                Intent intent =new Intent(getApplicationContext(), Profilecreate.class);
                intent.putExtra("view",true);
                startActivity(intent);
                return true;


            case  R.id.mycar:
                Intent intent2=new Intent(getApplicationContext(),Mycar.class);
                intent2.putExtra("carsaved",isCarsavedStatus);
                intent2.putExtra("insurancesaved",isInsurancesavedStatus);
                intent2.putExtra("driversaved",isDrivesaveStatus);
                intent2.putExtra("cardetails",cardetails.toString());

                startActivity(intent2);


//                final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.cardetails);
//                dialog.setCanceledOnTouchOutside(true);



                return true;
            case  R.id.myride:

                Intent intent1 =new Intent(getApplicationContext(), Myride.class);
                intent1.putExtra("view",true);
                startActivity(intent1);
//                final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.cardetails);
//                dialog.setCanceledOnTouchOutside(true);



                return true;


            case R.id.logout:

                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                startActivity(new Intent(this, LoginSignup.class));

                return (true);
            case R.id.about:
                //add the function to perform here
                return (true);
            case R.id.exit:

                if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 21) {
                    finishAffinity();
                } else if (Build.VERSION.SDK_INT >= 21) {
                    finishAndRemoveTask();
                }

                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        return cm.getActiveNetworkInfo() != null;
    }


    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            gpsfixed = true;


//                            Toast.makeText(Home.this, (String.format(Locale.ENGLISH, "%s: %f",
//                                    "Latitude",
//                                    mLastLocation.getLatitude())), Toast.LENGTH_SHORT).show();

//
//                            Toast.makeText(Home.this, (String.format(Locale.ENGLISH, "%s: %f",
//                                    "Longitude",
//                                    mLastLocation.getLongitude())), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());

                            //showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }


        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public boolean isgpsenabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;

        }

    }

    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        opengps();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void opengps() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(Home.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        getLastLocation();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Home.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.wtf("GPS", "Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.wtf("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.wtf("GPS", "checkLocationSettings -> onCanceled");
                    }
                });
    }

    private void shownetworkAlert() {
        android.app.AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();

        alertDialog.setTitle("Info");
        alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
        //alertDialog.setIcon(R.drawable.alerticon);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            switch (requestCode) {
                case 1:
                    break;
            }
        }
    }

    boolean gpsfixed = false;

    @Override
    public void ItemClicked(int positin) {

        try {

            if (gpsfixed) {
                if (positin == 0) {


                    Intent intent = new Intent(getApplicationContext(), FindRide.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    String location = mLastLocation.getLatitude() + "-" + mLastLocation.getLongitude();
                    intent.putExtra("location", location);
                    startActivity(intent);


                } else if (positin == 1) {
                    if (isCarsavedStatus&&isDrivesaveStatus&&isInsurancesavedStatus) {
                        Intent intent = new Intent(getApplicationContext(), OfferaRide.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        String location = mLastLocation.getLatitude() + "-" + mLastLocation.getLongitude();
                        intent.putExtra("location", location);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), VehicleDetails.class);
                        startActivity(intent);
                    }
                }
            } else {

                getLastLocation();
                Toast.makeText(this, "Location not fetched, please wait", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.wtf(TAG, "ItemClicked: " + e.getMessage());
        }
    }
}
