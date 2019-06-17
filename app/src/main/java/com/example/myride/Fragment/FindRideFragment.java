package com.example.myride.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myride.R;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.Utils.MyJsonArrayRequest;
import com.example.myride.adpter.FindRideListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FindRideFragment extends Fragment {

    private static final String TAG = "FindRideFragment";

    ImageView noresult;
    ProgressBar pbar;
    private Context myride;



    public FindRideFragment() {
    }

    @SuppressLint("ValidFragment")
    public FindRideFragment (Context myride) {
        this.myride = myride;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_findride_list, container, false);
    }
RecyclerView recyclerView;
    FindRideListAdapter findRideListAdapter;
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView=view.findViewById(R.id.recyclerView);
        pbar=view.findViewById(R.id.pbar);
        noresult=view.findViewById(R.id.noresult);




        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        findRideListAdapter=new FindRideListAdapter(ridePOJOArrayList);
        recyclerView.setAdapter(findRideListAdapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(myride));
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(myride).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        makearrayrequest(AppConstants.URL + AppConstants.RIDE_LIST + AppUtil.getuserid(myride));


    }

ArrayList<RidePOJO> ridePOJOArrayList=new ArrayList<>();

    public void makearrayrequest(String url) {



        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {


                    for (int i = 0; i < response.length(); i++) {


                        JSONObject jsonObject = response.getJSONObject(i);


                        if (jsonObject.has("offerRide") && jsonObject.getJSONObject("offerRide") != null) {
                            JSONObject offerRide = jsonObject.getJSONObject("offerRide");


                            String startDate = offerRide.getString("startDate");
                            String fromLocation = offerRide.getString("fromLocation");
                            String toLocation = offerRide.getString("toLocation");

                            String pric = String.valueOf(offerRide.getInt("price"));
                            String noOfSeats = String.valueOf(offerRide.getInt("noOfSeats"));
                            JSONObject car = offerRide.getJSONObject("car");
                            JSONObject driver = offerRide.getJSONObject("driver");

                            String carName = car.getString("carName");
                            String carNumber = car.getString("carNumber");


                            String drivernam = driver.getString("firstName");


                            RidePOJO ridePOJO=new RidePOJO(startDate,fromLocation,toLocation,pric,noOfSeats,carName,carNumber,
                                                                    drivernam);
                            ridePOJOArrayList.add(ridePOJO);
                            findRideListAdapter.notifyDataSetChanged();








                            pbar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {

                            recyclerView.setVisibility(View.GONE);
                            pbar.setVisibility(View.GONE);

                            noresult.setVisibility(View.VISIBLE);

                        }


                    }


                } catch (Exception e) {

                    recyclerView.setVisibility(View.GONE);
                    pbar.setVisibility(View.GONE);

                    noresult.setVisibility(View.VISIBLE);


                    e.printStackTrace();
                    Log.wtf(TAG, "onResponse: " + e.getMessage());
                }

                Log.wtf("onResponse", "" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                error.printStackTrace();
                Log.wtf("onErrorResponse", error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(myride.getApplicationContext());
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


            }
        });
        requestQueue.add(request);
    }


    public class RidePOJO {
        public RidePOJO(String startDate, String fromLocation, String toLocation, String pric, String noOfSeats, String carName, String carNumber, String drivernam) {
            this.startDate = startDate;
            this.fromLocation = fromLocation;
            this.toLocation = toLocation;
            this.pric = pric;
            this.noOfSeats = noOfSeats;
            this.carName = carName;
            this.carNumber = carNumber;
            this.drivernam = drivernam;
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

        String startDate ;
        String fromLocation ;
        String toLocation;

        String pric ;
        String noOfSeats ;

        String carName ;
        String carNumber;


        String drivernam ;
    }
}
