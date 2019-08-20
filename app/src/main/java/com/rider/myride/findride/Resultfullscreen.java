package com.rider.myride.findride;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.rider.myride.R;
import com.rider.myride.Services.NetworkServiceCall;
import com.rider.myride.Services.ServicesCallListener;
import com.rider.myride.Utils.AppConstants;
import com.rider.myride.Utils.AppUtil;
import com.rider.myride.adpter.RecyclerViewHorizontalListAdapter;
import com.rider.myride.model.Resultsetting;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Resultfullscreen extends AppCompatActivity implements RecyclerViewHorizontalListAdapter.AdapterCallback {
    private static final String TAG = "Resultfullscreen";
    private static final float thresholdOffset = 0.5f;
    Button accept, next;
    LinearLayoutManager horizontalLayoutManager;
    ViewPager viewPager;
    int srollcount = 0;
    int seatbooked = 0;
    Button btnReopenId;
    TextView tv;
    ProgressBar pb;
    ImageView imageView;
    int position = 0;
    private List<Resultsetting> rideresultlist = new ArrayList<>();
    private RecyclerView resultrecyclerview;
    private RecyclerViewHorizontalListAdapter resultrecycleradapter;
    private boolean scrollStarted, checkDirection;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.result);

        initComponents();

        populategroceryList();
        resultrecycleradapter = new RecyclerViewHorizontalListAdapter(rideresultlist, getApplicationContext(), Resultfullscreen.this);

        viewPager.setAdapter(resultrecycleradapter);
        resultrecycleradapter.notifyDataSetChanged();


        viewPager.setCurrentItem(position);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int currentPosition = viewPager.getCurrentItem();
                if (currentPosition == resultrecycleradapter.getCount() - 1)
                    viewPager.setCurrentItem(0);
                else
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (seatbooked != 0) {


                        int position = viewPager.getCurrentItem();

                        String offerRideId = rideresultlist.get(position).getOfferRideId();
                        String userId = AppUtil.getuserid(getApplicationContext());
                        String paymentStatus = rideresultlist.get(position).getOfferRideId();
                        String noOfSeats = rideresultlist.get(position).getOfferRideId();
                        String amount = rideresultlist.get(position).getPrice().split("\\.")[0];

                        int totalamout = Integer.parseInt(amount) * seatbooked;
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("offerRideId", offerRideId);
                        jsonObject.put("userId", userId);
                        jsonObject.put("paymentStatus", 1);
                        jsonObject.put("noOfSeats", seatbooked);
                        jsonObject.put("amount", totalamout);
                        jsonObject.put("status", 1);


                        NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                        serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                        serviceCall.makeJSONObjectPostRequest(AppConstants.URL + AppConstants.CONFIRM_OFFER, jsonObject, Request.Priority.IMMEDIATE);

                        showAcknowledgement();

                    } else {
                        Toast.makeText(Resultfullscreen.this, "Please select atleast one seat", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.d(TAG, "onClick: " + e.getMessage());
                    e.printStackTrace();
                }

//


            }
        });


    }

    private void showAcknowledgement() {


        final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_completedofferride);
        dialog.setCanceledOnTouchOutside(true);

        btnReopenId = dialog.findViewById(R.id.ok);
        tv = dialog.findViewById(R.id.msg);
        pb = dialog.findViewById(R.id.pbar);
        imageView = dialog.findViewById(R.id.tick);


        btnReopenId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnReopenId.getText().toString().equals("OK"))
                    dialog.dismiss();

                startActivity(new Intent(getApplicationContext(), Paymentacivity.class));
                finish();


            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    @Override
    public void onItemClicked(int position) {

        seatbooked = position;

    }

    private void initComponents() {

        viewPager = findViewById(R.id.result);
        accept = findViewById(R.id.accept);
        next = findViewById(R.id.next);
    }

    private void populategroceryList() {
        Bundle b = getIntent().getExtras();


        rideresultlist = b.getParcelableArrayList("array");
        position = Integer.parseInt(b.getString("posi"));


    }

    private class onServiceCallCompleteListene implements ServicesCallListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            if (jsonObject.has("offerRideId")) {

                pb.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                tv.setText("Succesfully Booked the ride,Now got to payment process");
                btnReopenId.setText("OK");


            }

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Crashlytics.logException(error);
        }

        @Override
        public void onStringResponse(String string) {

        }
    }
}
