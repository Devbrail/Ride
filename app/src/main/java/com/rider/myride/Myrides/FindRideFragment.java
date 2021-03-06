package com.rider.myride.Myrides;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FindRideFragment extends Fragment {

    private static final String TAG = "FindRideFragment";

    ImageView noresult;
    ProgressBar pbar;
    RecyclerView recyclerView;
    FindRideListAdapter findRideListAdapter;
    ArrayList<RidePOJO> ridePOJOArrayList = new ArrayList<>();
    private Context myride;

    public FindRideFragment() {
    }

    @SuppressLint("ValidFragment")
    public FindRideFragment(Context myride) {
        this.myride = myride;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_findride_list, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.recyclerView);
        pbar = view.findViewById(R.id.pbar);
        noresult = view.findViewById(R.id.noresult);


        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        findRideListAdapter = new FindRideListAdapter(null, null, null);
        recyclerView.setAdapter(findRideListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(myride));
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(myride).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        makearrayrequest(AppConstants.URL + AppConstants.RIDE_LIST + AppUtil.getuserid(myride));


    }

    public void makearrayrequest(String url) {


        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {


                    for (int i = 0; i < response.length(); i++) {


                        JSONObject jsonObject = response.getJSONObject(i);


                        if (jsonObject.has("offerRide") && !jsonObject.isNull("offerRide")) {
                            JSONObject offerRide = jsonObject.getJSONObject("offerRide");


                            String startDate = offerRide.getString("startDate");
                            startDate = convertDateformat(startDate.replace("T", " "));
                            String fromLocation = offerRide.getString("fromLocation");
                            String toLocation = offerRide.getString("toLocation");

                            String pric = String.valueOf(offerRide.getInt("price"));
                            String noOfSeats = String.valueOf(offerRide.getInt("noOfSeats"));
                            int seatofferd = offerRide.getInt("noOfSeatsVacant");
                            JSONObject car = offerRide.getJSONObject("car");
                            JSONObject driver = offerRide.getJSONObject("driver");

                            String carName = car.getString("carName");
                            String carNumber = car.getString("carNumber");

                            String carImage = car.getString("carImage");
                            String driverimage = driver.getString("userIamge");


                            String drivernam = driver.getString("firstName");


                            RidePOJO ridePOJO = new RidePOJO(startDate, fromLocation, toLocation, pric, noOfSeats, carName, carNumber,
                                    drivernam, carImage, driverimage, seatofferd);
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
                    Crashlytics.logException(e);
                    recyclerView.setVisibility(View.GONE);
                    pbar.setVisibility(View.GONE);

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

    public class RidePOJO {
        String carImage, driverImage;
        int seatofferd;
        String startDate;
        String fromLocation;
        String toLocation;
        String pric;
        String noOfSeats;
        String carName;
        String carNumber;
        String drivernam;

        public RidePOJO(String startDate, String fromLocation, String toLocation, String pric, String noOfSeats, String carName, String carNumber, String drivernam, String carImage, String driverimage, int seatofferd) {
            this.startDate = startDate;
            this.fromLocation = fromLocation;
            this.toLocation = toLocation;
            this.pric = pric;
            this.noOfSeats = noOfSeats;
            this.carName = carName;
            this.carNumber = carNumber;
            this.drivernam = drivernam;
            this.carImage = carImage;
            this.driverImage = driverimage;
            this.seatofferd = seatofferd;
        }

        public String getCarImage() {
            return carImage;
        }

        public String getDriverImage() {
            return driverImage;
        }

        public int getSeatofferd() {
            return seatofferd;
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
    }
}
