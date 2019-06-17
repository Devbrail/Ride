package com.example.myride.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myride.Myride;
import com.example.myride.R;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.Utils.MyJsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class OfferrideFragment extends Fragment {
    private Context myride;



    TextView from;
    TextView to;
    TextView date;
    TextView price;
    TextView seats;
    TextView occupied;
    TextView carname;
    TextView carno;
    TextView drivername;
    LinearLayout layout;
    ImageView noresult;


    public OfferrideFragment(){}
    public OfferrideFragment(Context myride) {
        this.myride=myride;
        // Required empty public constructor
    }
    ProgressBar pbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmentt_offerride, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        from = view.findViewById(R.id.from);
        to = view.findViewById(R.id.to);
        date = view.findViewById(R.id.date);
        price = view.findViewById(R.id.price);
        layout = view.findViewById(R.id.layout);
        noresult = view.findViewById(R.id.noresult);

        pbar = view.findViewById(R.id.pbar);

        drivername = view.findViewById(R.id.drivername);
        carno = view.findViewById(R.id.carno);
        carname = view.findViewById(R.id.carname);
        seats = view.findViewById(R.id.seats);
        occupied = view.findViewById(R.id.occupied);


        makearrayrequest(AppConstants.URL + AppConstants.OFFER_LIST + AppUtil.getuserid(myride));




    }

    public void makearrayrequest(String url) {


        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {


                    for (int i = 0; i < response.length(); i++) {


                        JSONObject jsonObject = response.getJSONObject(i);


                        if (jsonObject.has("car") && jsonObject.getJSONObject("car") != null) {


                            String startDate = jsonObject.getString("startDate");
                            String fromLocation = jsonObject.getString("fromLocation");
                            String toLocation = jsonObject.getString("toLocation");

                            String pric = String.valueOf(jsonObject.getInt("price"));
                            String noOfSeats = String.valueOf(jsonObject.getInt("noOfSeats"));
                            String occupies = String.valueOf(jsonObject.getInt("noOfSeatsOccupied"));

                            JSONObject car = jsonObject.getJSONObject("car");
                            JSONObject driver = jsonObject.getJSONObject("driver");

                            String carName = car.getString("carName");
                            String carNumber = car.getString("carNumber");


                            String drivernam = driver.getString("firstName");

                            from.setText(fromLocation);
                            to.setText(toLocation);
                            date.setText(startDate);
                            price.setText(pric);
                            price.setText(pric);
                            carname.setText(carName);
                            carno.setText(carNumber);
                            drivername.setText(drivernam);
                            seats.setText(noOfSeats);
                            occupied.setText(occupies);



                            pbar.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                        } else {

                            layout.setVisibility(View.GONE);
                            pbar.setVisibility(View.GONE);

                            noresult.setVisibility(View.VISIBLE);

                        }



                    }


                } catch (Exception e) {

                    layout.setVisibility(View.GONE);
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

    private static final String TAG = "OfferrideFragment";
}
