package com.rider.myride.Myrides;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.rider.myride.R;
import com.rider.myride.Utils.AppConstants;
import com.rider.myride.Utils.MyJsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activeforoffer extends Fragment {

    private static final String TAG = "Activeforoffer";
    Myrides.RidePOJOOffer ridePOJOOffer;
    Context context;
    ActiveAdapter activeAdapter;

    RecyclerView recyclerView;
    ArrayList<ActivePojo> activePojoArrayList = new ArrayList<>();

    public Activeforoffer(Myrides.RidePOJOOffer ridePOJOOffer, Context activity) {
        this.ridePOJOOffer = ridePOJOOffer;
        this.context = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activeoffers, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        makearrayrequest(AppConstants.URL + AppConstants.GetOfferRide + ridePOJOOffer.getOfferRideId());


        return rootView;
    }

    public void makearrayrequest(String url) {


        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    Log.d(TAG, "onResponse: " + response.toString());

                    for (int i = 0; i < response.length(); i++) {


                        JSONObject jsonObject = response.getJSONObject(i);

                        if (jsonObject != null) {

                            String userId = jsonObject.getString("userId");
                            String userName = jsonObject.getString("userName");
                            String phone = jsonObject.getString("phone");


                            if (jsonObject.has("userDetails")) {

                                int rideStatus = jsonObject.getJSONObject("offerRide").getJSONArray("offerRideDetails").getJSONObject(0).getInt("status");


                                JSONObject userDetails = jsonObject.getJSONObject("userDetails");
                                JSONObject offerRide = jsonObject.getJSONObject("offerRide");
                                JSONObject car = offerRide.getJSONObject("car");

                                String Name = userDetails.getString("firstName") + " " + userDetails.getString("lastName");
                                String fromLocation = offerRide.getString("fromLocation");
                                String toLocation = offerRide.getString("toLocation");
                                String price = offerRide.getString("price");
                                String startDate = offerRide.getString("startDate");
                                String carName = car.getString("carName");

                                ActivePojo activePojo = new ActivePojo();

                                activePojo.setUserId(userId);
                                activePojo.setUserName(userName);
                                activePojo.setPhone(phone);
                                activePojo.setRideStatus(rideStatus);
                                activePojo.setName(Name);
                                activePojo.setFromLocation(fromLocation);
                                activePojo.setToLocation(toLocation);
                                activePojo.setPrice(price);
                                activePojo.setStartDate(startDate);
                                activePojo.setCarName(carName);


                                activePojoArrayList.add(activePojo);

                            }

                        }
                        activeAdapter = new ActiveAdapter(activePojoArrayList, ridePOJOOffer, getActivity());

                        recyclerView.setAdapter(activeAdapter);


                        activeAdapter.notifyDataSetChanged();
                    }


                } catch (Exception e) {
                    Crashlytics.logException(e);


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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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

    public class ActivePojo {
        String userId;
        String userName;
        String phone;
        String carName;
        int rideStatus;
        String Name;
        String fromLocation;
        String toLocation;
        String price;
        String startDate;

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getRideStatus() {
            return rideStatus;
        }

        public void setRideStatus(int rideStatus) {
            this.rideStatus = rideStatus;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getFromLocation() {
            return fromLocation;
        }

        public void setFromLocation(String fromLocation) {
            this.fromLocation = fromLocation;
        }

        public String getToLocation() {
            return toLocation;
        }

        public void setToLocation(String toLocation) {
            this.toLocation = toLocation;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }


    }

}
