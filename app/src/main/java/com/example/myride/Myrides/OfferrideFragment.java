package com.example.myride.Myrides;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
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
import com.crashlytics.android.Crashlytics;
import com.example.myride.R;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.Utils.MyJsonArrayRequest;
import com.example.myride.adpter.OfferRideListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_findride_list, container, false);
    }

    OfferRideListAdapter offerRideListAdapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       /* from = view.findViewById(R.id.from);
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
        occupied = view.findViewById(R.id.occupied);*/


        recyclerView=view.findViewById(R.id.recyclerView);
        pbar=view.findViewById(R.id.pbar);
        noresult=view.findViewById(R.id.noresult);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        offerRideListAdapter=new OfferRideListAdapter(ridePOJOArrayList,getActivity());

        recyclerView.setAdapter(offerRideListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(myride));
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(myride).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        makearrayrequest(AppConstants.URL + AppConstants.OFFER_LIST + AppUtil.getuserid(myride));




    }
    ArrayList<RidePOJO> ridePOJOArrayList=new ArrayList<>();
    public void makearrayrequest(String url) {



        MyJsonArrayRequest request = new MyJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {


                    for (int i = 0; i < response.length(); i++) {


                        JSONObject jsonObject = response.getJSONObject(i);


                        if (jsonObject.has("car") &&! jsonObject.isNull("car") ) {


                            String startDate = jsonObject.getString("startDate");
                            if(startDate.contains("T"))
                            {
                                startDate=startDate.replace("T","  ");
                            }
                            String fromLocation = jsonObject.getString("fromLocation");
                            String toLocation = jsonObject.getString("toLocation");

                            String pric = String.valueOf(jsonObject.getInt("price"))+".00/-";
                            String noOfSeats = String.valueOf(jsonObject.getInt("noOfSeats"));
                            String occupies = String.valueOf(jsonObject.getInt("noOfSeatsOccupied"));

                            JSONObject car = jsonObject.getJSONObject("car");
                            JSONObject driver = jsonObject.getJSONObject("driver");

                            String carName = car.getString("carName");
                            String carNumber = car.getString("carNumber");



                            String drivernam = driver.getString("firstName");
                            String userIamge = driver.getString("userIamge");

                            RidePOJO ridePOJO=new RidePOJO(startDate,fromLocation,toLocation,pric,noOfSeats,carName,carNumber,
                                    drivernam,userIamge,occupies);
                            ridePOJOArrayList.add(ridePOJO);
                            offerRideListAdapter.notifyDataSetChanged();








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
                    Log.wtf(TAG, "onResponse: " + e.getMessage());
                }

                Log.wtf("onResponse", "" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error) {
    Crashlytics.logException(error);;;error.printStackTrace();
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
            public void retry (VolleyError error) {
    Crashlytics.logException(error);;;}
        });
        requestQueue.add(request);
    }

    String convertDateformat(String ss){

        try {
            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
            Date newDate=spf.parse(ss);
            spf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = spf.format(newDate);
            String s[]=date.split(":");
            date=s[0]+s[1];
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return ss;
        }
    }
    public class RidePOJO {
        public RidePOJO(String startDate, String fromLocation, String toLocation, String pric, String noOfSeats, String carName, String carNumber, String drivernam, String occupiedseat, String userIamge) {
            this.startDate = startDate;
            this.fromLocation = fromLocation;
            this.toLocation = toLocation;
            this.pric = pric;
            this.noOfSeats = noOfSeats;
            this.carName = carName;
            this.carNumber = carNumber;
            this.drivernam = drivernam;
            this.occupiedseat=occupiedseat;
            this.userIamge=userIamge;
        }

        String userIamge;
        String carIamge;

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

        String startDate ;
        String fromLocation ;
        String toLocation;

        String pric ;
        String noOfSeats ;
        String occupiedseat;

        public String getOccupiedseat() {
            return occupiedseat;
        }

        String carName ;
        String carNumber;


        String drivernam ;
    }
    private static final String TAG = "OfferrideFragment";
}
