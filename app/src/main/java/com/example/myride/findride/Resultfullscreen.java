package com.example.myride.findride;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.myride.Home;
import com.example.myride.R;
import com.example.myride.Services.NetworkServiceCall;
import com.example.myride.Services.ServicesCallListener;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.adpter.RecyclerViewHorizontalListAdapter;
import com.example.myride.basic.VehicleDetails;
import com.example.myride.model.Resultsetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Resultfullscreen extends AppCompatActivity {
    private List<Resultsetting> rideresultlist = new ArrayList<>();
    private RecyclerView resultrecyclerview;
    private RecyclerViewHorizontalListAdapter resultrecycleradapter;
    Button accept, next;
    LinearLayoutManager horizontalLayoutManager;
    ViewPager viewPager;
    int srollcount = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.result);

        initComponents();

        populategroceryList();
        resultrecycleradapter = new RecyclerViewHorizontalListAdapter(rideresultlist, getApplicationContext());

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

                    int position=viewPager.getCurrentItem();

                    String offerRideId=rideresultlist.get(position).getOfferRideId();
                    String userId= AppUtil.getuserid(getApplicationContext());
                    String paymentStatus=rideresultlist.get(position).getOfferRideId();
                    String noOfSeats=rideresultlist.get(position).getOfferRideId();
                    String amount=rideresultlist.get(position).getPrice();

                    JSONObject jsonObject=new JSONObject();

                    jsonObject.put("offerRideId",offerRideId);
                    jsonObject.put("userId",userId);
                    jsonObject.put("paymentStatus",1);
                    jsonObject.put("noOfSeats",1);
                    jsonObject.put("amount",amount);


                    NetworkServiceCall serviceCall = new NetworkServiceCall(getApplicationContext(), false);
                    serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                    serviceCall.makeJSONObjectPostRequest( AppConstants.URL+AppConstants.CONFIRM_OFFER,jsonObject, Request.Priority.IMMEDIATE);

                    showAcknowledgement();

                } catch (Exception e) {
                    Log.wtf(TAG, "onClick: "+e.getMessage() );
                    e.printStackTrace();
                }

//


            }
        });
        /*
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (checkDirection) {
                    if (thresholdOffset > v) {
                        Log.wtf(TAG, "going left");
                        int currentPosition = viewPager.getCurrentItem();
                        if (currentPosition == resultrecycleradapter.getCount() - 1)
                            viewPager.setCurrentItem(0);
                    } else {
                        Log.wtf(TAG, "going right");


                    }
                    checkDirection = false;
                }

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (!scrollStarted && i == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        });*/


    }

    Button btnReopenId;
    TextView tv;
    ProgressBar pb;
    ImageView imageView;
    private void showAcknowledgement() {


        final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_completedofferride);
        dialog.setCanceledOnTouchOutside(true);

        btnReopenId = (Button) dialog.findViewById(R.id.ok);
        tv=dialog.findViewById(R.id.msg);
        pb=dialog.findViewById(R.id.pbar);
        imageView=dialog.findViewById(R.id.tick);



        btnReopenId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnReopenId.getText().toString().equals("OK"))
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

    private class onServiceCallCompleteListene implements ServicesCallListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            if(jsonObject.has("offerRideId")){

                pb.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                tv.setText("Succesfully Booked the ride,Now got to payment process");
                btnReopenId.setText("OK");





            }

        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void onStringResponse(String string) {

        }
    }
    private static final String TAG = "Resultfullscreen";
    private static final float thresholdOffset = 0.5f;
    private boolean scrollStarted, checkDirection;

    private void initComponents() {

        viewPager = findViewById(R.id.result);
        accept = findViewById(R.id.accept);
        next = findViewById(R.id.next);
    }

    private void populategroceryList() {
        Bundle b=getIntent().getExtras();


        rideresultlist = b.getParcelableArrayList("array");
         position= Integer.parseInt(b.getString("posi"));


    }
    int position=0;
}
