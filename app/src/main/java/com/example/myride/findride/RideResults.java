package com.example.myride.findride;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.Utils.MyJsonArrayRequest;
import com.example.myride.adpter.Ridesearchadapter;
import com.example.myride.basic.Profilecreate;
import com.example.myride.model.Movie;
import com.example.myride.model.Resultsetting;

import org.json.JSONArray;
import org.json.JSONException;
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
     TextView tittle,noofseats,textView;
    RecyclerView recyclerView;
    private List<Movie> movieList = new ArrayList<>();
    String fullText,from,to,when;
    private static final String TAG = "RideResults";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_results);
        drivename = new ArrayList<>();
        vehicle_details = new ArrayList<>();
        profile = new ArrayList<>();



          icon = BitmapFactory.decodeResource(getResources(),R.drawable.african);

         recyclerView =  findViewById(R.id.recyclerView);
        adapter = new Ridesearchadapter(movieList,RideResults.this,RideResults.this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        tittle=findViewById(R.id.textView);
        noofseats=findViewById(R.id.seatavailed);
        textView=findViewById(R.id.textView);




        Intent intent = getIntent();
        if(intent.hasExtra("numbers"))
        {
          fullText = intent.getStringExtra("numbers");

          if(fullText   .contains("_")) {
              from = fullText.split("_")[0];
              to = fullText.split("_")[1];

              textView.setText(from+" -\n"+to);

              when = fullText.split("_")[2];
          }
        }


        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("startDate","2019-06-14T13:42:47");
            jsonObject.put("fromLocation",from);
            jsonObject.put("toLocation",to);
            jsonObject.put("userId", AppUtil.getuserid(getApplicationContext()));







            MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.POST, AppConstants.URL+
                    AppConstants.GET_OFFERRESULT, jsonObject, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {

                        resultsettingArrayList=new ArrayList<>();
                        Bitmap myLogo = BitmapFactory.decodeResource(getResources(), R.drawable.yu);
                        Bitmap myLogo1 = BitmapFactory.decodeResource(getResources(), R.drawable.african);
                        for (int i=0; i<response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            for (int j=0;j<jsonObject.length();j++) {

                                if(jsonObject.has("car")&&jsonObject.has("driver")){
                                JSONObject car = jsonObject.getJSONObject("car");


                                    String carId = car.getString("carId");
                                String carName = car.getString("carName");
                                String carNumber = car.getString("carNumber");
                                String carModel = car.getString("carModel");
                                String carColor = car.getString("carColor");
                                String seatNumber = car.getString("seatNumber");
                                String userId = car.getString("userId");
                                String carImage = car.getString("carImage");
                                byte[] decodedString = Base64.decode(carImage, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                                    JSONObject driver = jsonObject.getJSONObject("driver");
                                    String driverId = driver.getString("driverId");
                                    String driverName = driver.getString("driverName");
                                    String nin = driver.getString("nin");
                                    String gender = driver.getString("gender");
                                    String email = driver.getString("email");
                                    String dob = driver.getString("dob");
                                    String userPic = driver.getString("userPic");
                                    String drivngLicence = driver.getString("drivngLicence");
                                    String drivngLicenceExpiry = driver.getString("drivngLicenceExpiry");


                                    Resultsetting resultsetting=new Resultsetting(driverName,from,to,when,"Not estimated","NE",carName,carNumber,3f,4f,myLogo1,myLogo);
                                    resultsettingArrayList.add(resultsetting);



                                    Movie movie=new Movie(driverName,carName,4f,icon);
                                    movieList.add(movie);
                                    adapter.notifyDataSetChanged();
                                    noofseat++;




                                }



                            }


                            noofseats.setText(noofseat+" seats are available");

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(RideResults.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onResponse: "+e.getMessage());
                    }

                   // Log.i("onResponse", "" + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i("onErrorResponse", "Error");
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







        } catch (JSONException e) {
            e.printStackTrace();
        }

//        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("numbers");
//        tittle.setText(myList.get(2)+" -\n"+myList.get(3));

       // Log.d(TAG, "onCreate: "+myList.toString());
    }
int noofseat=0;
    Bitmap icon;
    ArrayList<Resultsetting> resultsettingArrayList;


    @Override
    public void onItemClicked(int position) {


        Intent intent=new Intent(RideResults.this,Resultfullscreen.class);
        Bundle b=new Bundle();

        b.putString("posi", String.valueOf(position));
        b.putParcelableArrayList("array",resultsettingArrayList);
        intent.putExtras(b);
        startActivity(intent);


    }
//    private void prepareMovieData() {
//        Movie movie=new  Movie("John lucifer","Toyota Innovo\nKL 23405", (float) 4.0,icon);
//
//        movieList.add(movie);
//          movie=new  Movie("Suhail TS","Mercedes \nHN 1356",(float)4,icon);
//        movieList.add(movie);
//        adapter.notifyDataSetChanged();
//    }

}
