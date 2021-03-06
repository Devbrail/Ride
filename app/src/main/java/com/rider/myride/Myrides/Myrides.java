package com.rider.myride.Myrides;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.rider.myride.R;
import com.rider.myride.Utils.AppConstants;
import com.rider.myride.Utils.AppUtil;
import com.rider.myride.Utils.MyJsonArrayRequest;
import com.rider.myride.adpter.FindRideListAdapter;
import com.rider.myride.adpter.OfferRideListAdapter;
import com.rider.myride.model.RidePOJO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Myrides extends AppCompatActivity {

    private static final String TAG = "Myrides";
    RecyclerView recyclerView;
    FindRideListAdapter findRideListAdapter;
    OfferRideListAdapter offerRideListAdapter;
    RecyclerView recyclerView1;
    ImageView noresult;
    ArrayList<RidePOJO> ridePOJOArrayList = new ArrayList<>();
    ArrayList<RidePOJOOffer> ridePOJOArrayListoffer = new ArrayList<>();
    LinearLayout rootlayout;
    RequestQueue requestQueue;
    boolean findrideiscalled = false;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrides);


        this.setTitle("My Rides");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView1 = findViewById(R.id.recyclerView1);
        rootlayout = findViewById(R.id.rootlayout);


        // pbar=findViewById(R.id.pbar);
        noresult = findViewById(R.id.noresult);

        progressBar = new ProgressDialog(this, R.style.Theme_MaterialComponents_Dialog);
        progressBar.setCancelable(false);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();


        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        ((SimpleItemAnimator) recyclerView1.getItemAnimator()).setSupportsChangeAnimations(false);


        findRideListAdapter = new FindRideListAdapter(ridePOJOArrayList, Myrides.this, rootlayout);
        recyclerView.setAdapter(findRideListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getApplicationContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        offerRideListAdapter = new OfferRideListAdapter(ridePOJOArrayListoffer, Myrides.this);


        recyclerView1.setAdapter(offerRideListAdapter);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView1.setHasFixedSize(true);
        recyclerView1.addItemDecoration(dividerItemDecoration);

        makearrayrequest(AppConstants.URL + AppConstants.RIDE_LIST + AppUtil.getuserid(this));


        findrideiscalled = true;


        makearrayrequest1(AppConstants.URL + AppConstants.OFFER_LIST + AppUtil.getuserid(getApplicationContext()));


    }

    public void makearrayrequest1(String url) {


        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    Log.d(TAG, "onResponse: " + response.toString());
                    Crashlytics.log(response.toString());
                    for (int i = 0; i < response.length(); i++) {


                        JSONObject jsonObject = response.getJSONObject(i);


                        if (jsonObject.has("car") && !jsonObject.isNull("car")) {


                            String offerRideId = jsonObject.getString("offerRideId");
                            String startDate = jsonObject.getString("startDate");
                            if (startDate.contains("T")) {
                                startDate = startDate.replace("T", "  ");
                            }
                            String fromLocation = jsonObject.getString("fromLocation");
                            String toLocation = jsonObject.getString("toLocation");

                            String pric = jsonObject.getInt("price") + ".00/-";
                            String noOfSeats = String.valueOf(jsonObject.getInt("noOfSeats"));
                            String occupies = String.valueOf(jsonObject.getInt("noOfSeatsOccupied"));

                            JSONObject car = jsonObject.getJSONObject("car");
                            JSONObject driver = jsonObject.getJSONObject("driver");

                            String carName = car.getString("carName");
                            String carNumber = car.getString("carNumber");


                            String drivernam = driver.getString("firstName");
                            String userIamge = driver.getString("userIamge");

                            RidePOJOOffer ridePOJO = new RidePOJOOffer(offerRideId, startDate, fromLocation, toLocation, pric, noOfSeats, carName, carNumber,
                                    drivernam, userIamge, occupies);
                            ridePOJOArrayListoffer.add(ridePOJO);
                            offerRideListAdapter.notifyDataSetChanged();


                        } else {


                            noresult.setVisibility(View.VISIBLE);

                        }


                    }


                } catch (Exception e) {
                    Crashlytics.logException(e);
                    recyclerView.setVisibility(View.GONE);


                    noresult.setVisibility(View.VISIBLE);


                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
                }

                Log.d("onResponse", "" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Crashlytics.logException(error);
                error.printStackTrace();
                Log.d("onErrorResponse", error.getMessage());
            }
        });
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
            public void retry(VolleyError error) {
                Crashlytics.logException(error);
            }
        });
        requestQueue.add(request);
    }

    public void makearrayrequest(String url) {


        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    Log.d(TAG, "onResponse: " + response.toString());
                    if (findrideiscalled)
                        progressBar.dismiss();
                    for (int i = 0; i < response.length(); i++) {



                        JSONObject jsonObject = response.getJSONObject(i);


                        if (jsonObject.has("offerRide") && !jsonObject.isNull("offerRide")) {
                            JSONObject offerRide = jsonObject.getJSONObject("offerRide");

                            JSONObject car = offerRide.getJSONObject("car");
                            JSONObject driver = offerRide.getJSONObject("driver");
                            findrideiscalled = false;
                            String startDate = offerRide.getString("startDate");
                            startDate = convertDateformat(startDate.replace("T", " "));
                            String fromLocation = offerRide.getString("fromLocation");
                            String toLocation = offerRide.getString("toLocation");

                            String pric = String.valueOf(offerRide.getInt("price"));
                            String noOfSeats = String.valueOf(offerRide.getInt("noOfSeats"));
                            int seatofferd = offerRide.getInt("noOfSeatsVacant");


                            String carName = car.getString("carName").toUpperCase();
                            String carNumber = car.getString("carNumber").toUpperCase();

                            String carImage = car.getString("carImage");

                            String driverimage = driver.getString("userIamge");


                            String drivernam = driver.getString("firstName").toUpperCase();


                            RidePOJO ridePOJO = new RidePOJO(startDate, fromLocation, toLocation, pric, noOfSeats, carName, carNumber,
                                    drivernam, carImage, driverimage, seatofferd);
                            ridePOJOArrayList.add(ridePOJO);
                            findRideListAdapter.notifyDataSetChanged();
                            noresult.setVisibility(View.GONE);


                        }


                    }


                } catch (Exception e) {
                    Crashlytics.logException(e);
                    recyclerView.setVisibility(View.GONE);

                    progressBar.dismiss();

                    noresult.setVisibility(View.VISIBLE);


                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
                }

                Log.d("onResponse", "" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Crashlytics.logException(error);
                error.printStackTrace();
                Log.d("onErrorResponse", error.getMessage());
            }
        });
        requestQueue = Volley.newRequestQueue(getApplicationContext());
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
            public void retry(VolleyError error) {
                Crashlytics.logException(error);
            }
        });
        requestQueue.add(request);
    }

    String convertDateformat(String date) {

        try {

            String[] s = date.split(":");
            date = s[0] + ":" + s[1];
            return date;
        } catch (Exception e) {
            Crashlytics.logException(e);
            return date;
        }
    }

    public class RidePOJOOffer {
        String userIamge;
        String carIamge;
        String offerRideId;
        String startDate;
        String fromLocation;
        String toLocation;
        String pric;
        String noOfSeats;
        String occupiedseat;
        String carName;
        String carNumber;
        String drivernam;

        public RidePOJOOffer(String offerRideId, String startDate, String fromLocation, String toLocation, String pric, String noOfSeats, String carName, String carNumber, String drivernam, String occupiedseat, String userIamge) {
            this.startDate = startDate;
            this.fromLocation = fromLocation;
            this.toLocation = toLocation;
            this.pric = pric;
            this.noOfSeats = noOfSeats;
            this.carName = carName;
            this.carNumber = carNumber;
            this.drivernam = drivernam;
            this.occupiedseat = occupiedseat;
            this.userIamge = userIamge;
            this.offerRideId = offerRideId;
        }

        public String getOfferRideId() {
            return offerRideId;
        }

        public String getUserIamge() {
            return userIamge;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getFromLocation() {
            return fromLocation;
        }

        public String getToLocation() {
            return toLocation;
        }

        public String getPric() {
            return pric;
        }

        public String getNoOfSeats() {
            return noOfSeats;
        }

        public String getCarName() {
            return carName;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public String getDrivernam() {
            return drivernam;
        }

        public String getOccupiedseat() {
            return occupiedseat;
        }
    }

}
