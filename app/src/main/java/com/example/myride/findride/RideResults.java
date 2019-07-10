package com.example.myride.findride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.example.myride.R;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.Utils.MyJsonArrayRequest;
import com.example.myride.adpter.Ridesearchadapter;
import com.example.myride.model.Movie;
import com.example.myride.model.Resultsetting;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RideResults extends AppCompatActivity implements Ridesearchadapter.AdapterCallback {

    ArrayList<String> drivename;
    ArrayList<String> vehicle_details;

    int rating;
    ArrayList<Bitmap> profile;


    Ridesearchadapter adapter;
    Ridesearchadapter.AdapterCallback callback;
    TextView tittle, noofseats, textView;
    RecyclerView recyclerView;
    private List<Movie> movieList = new ArrayList<>();
    String fullText, from, to, when;
    ProgressBar pb;
    private static final String TAG = "RideResults";
    ImageView noresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_results);
        drivename = new ArrayList<>();
        vehicle_details = new ArrayList<>();
        profile = new ArrayList<>();


        pb = findViewById(R.id.progressbar);
        noresult=findViewById(R.id.noresult);

        icon = BitmapFactory.decodeResource(getResources(), R.drawable.african);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new Ridesearchadapter(movieList, RideResults.this, RideResults.this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        tittle = findViewById(R.id.textView);
        noofseats = findViewById(R.id.seatavailed);
        textView = findViewById(R.id.textView);


        Intent intent = getIntent();
        if (intent.hasExtra("numbers")) {
            fullText = intent.getStringExtra("numbers");

            if (fullText.contains("_")) {
                from = fullText.split("_")[0];
                to = fullText.split("_")[1];

                textView.setText(from + " -\n" + to);

                when = fullText.split("_")[2]+"T00:00:00";
            }
        }


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startDate", when);
            jsonObject.put("fromLocation", from);
            jsonObject.put("toLocation", to);
            jsonObject.put("typeId", 1);
            jsonObject.put("userId", AppUtil.getuserid(getApplicationContext()));


            Log.w(TAG, "ride: " + jsonObject);


            MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.POST, AppConstants.URL +
                    AppConstants.GET_OFFERRESULT, jsonObject, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {

                        Log.wtf(TAG,response.toString());
                        resultsettingArrayList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);


                            if (jsonObject.has("car") && jsonObject.has("driver")&&!jsonObject.isNull("car")) {



                                String price = jsonObject.getString("price");
                                String offerRideId = jsonObject.getString("offerRideId");
                                JSONObject car = jsonObject.getJSONObject("car");
                                String carId = car.getString("carId");
                                String carName = car.getString("carName");
                                String carNumber = car.getString("carNumber");
                                String carModel = car.getString("carModel");
                                String carColor = car.getString("carColor");
                                String seatNumber = car.getString("seatNumber");
                                String availablesea=jsonObject.getString("noOfSeatsVacant");
                                String userId = car.getString("userId");
                                String carImage = car.getString("carImage");


                                JSONObject driver = jsonObject.getJSONObject("driver");
                                String driverId = driver.getString("driverId");
                                String driverName = driver.getString("firstName") + " " + driver.getString("lastName");

                                String nin = driver.getString("nin");
                                String gender = driver.getString("gender");
                                String email = driver.getString("email");
                                String dob = driver.getString("dob");
                                String driverpic = driver.getString("userIamge");
                                String drivngLicence = driver.getString("drivngLicence");
                                String drivngLicenceExpiry = driver.getString("drivngLicenceExpiry");



                                when=when.split("T")[0];
                                Resultsetting resultsetting = new Resultsetting(offerRideId, driverName, from, to, when, "Not estimated", "NE", carName, carNumber, 3f, Float.parseFloat(seatNumber),Float.parseFloat(availablesea), carImage,driverpic, price+"/-");
                                resultsettingArrayList.add(resultsetting);


                                Movie movie = new Movie(driverName, carName, 4f, driverpic);
                                movieList.add(movie);
                                adapter.notifyDataSetChanged();
                                noofseat++;


                                pb.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                            }else {
                                pb.setVisibility(View.GONE);

                                noresult.setVisibility(View.VISIBLE);

                            }


                            noofseats.setText(noofseat + " seats are available");

                        }


                    } catch (Exception e) {
            Crashlytics.logException(e);
e.printStackTrace();
                        pb.setVisibility(View.GONE);

                        noresult.setVisibility(View.VISIBLE);
                       // Toast.makeText(RideResults.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.wtf(TAG, "onResponse: " + e.getMessage());
                    }

                    // Log.wtf("onResponse", "" + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse (VolleyError error) {
    Crashlytics.logException(error);;;pb.setVisibility(View.GONE);

                    noresult.setVisibility(View.VISIBLE);
                    Toast.makeText(RideResults.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    Log.wtf("onErrorResponse", error);
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
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            requestQueue.add(request);


        } catch (Exception e) {
            Crashlytics.logException(e);
e.printStackTrace();
        }

//        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("numbers");
//        tittle.setText(myList.get(2)+" -\n"+myList.get(3));

        // Log.wtf(TAG, "onCreate: "+myList.toString());
    }

    int noofseat = 0;
    Bitmap icon;
    ArrayList<Resultsetting> resultsettingArrayList;


    @Override
    public void onItemClicked(int position) {


        Intent intent = new Intent(RideResults.this, Resultfullscreen.class);
        Bundle b = new Bundle();

        b.putString("posi", String.valueOf(position));
        b.putParcelableArrayList("array", resultsettingArrayList);
        intent.putExtras(b);
        startActivity(intent);


    }


}
