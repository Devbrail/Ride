package com.example.myride.findride;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.myride.R;
import com.example.myride.adpter.RecyclerViewHorizontalListAdapter;
import com.example.myride.model.Resultsetting;

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


/*
        resultrecyclerview = findViewById(R.id.idRecyclerViewHorizontalList);
        // add a divider after each item for more clarity
        resultrecyclerview.addItemDecoration(new DividerItemDecoration(Resultfullscreen.this, LinearLayoutManager.HORIZONTAL));
        resultrecycleradapter = new RecyclerViewHorizontalListAdapter(rideresultlist, getApplicationContext());
          horizontalLayoutManager = new LinearLayoutManager(Resultfullscreen.this, LinearLayoutManager.HORIZONTAL, false);
        resultrecyclerview.setLayoutManager(horizontalLayoutManager);
        resultrecyclerview.addItemDecoration(new DividerItemDecoration(this, 0));
        resultrecyclerview.setAdapter(resultrecycleradapter);
        PagerSnapHelper mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(resultrecyclerview);

       resultrecycleradapter.notifyDataSetChanged();

       resultrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
               super.onScrollStateChanged(recyclerView, newState);


               if(srollcount>=resultrecycleradapter.getItemCount()-1){
                   // End of the list is here.
                   Toast.makeText(Resultfullscreen.this, "eti", Toast.LENGTH_SHORT).show();
               }

           }

           @Override
           public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);

           }
       });*/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    if (horizontalLayoutManager.findLastCompletelyVisibleItemPosition() == (resultrecycleradapter.getItemCount() -1)) {

                    resultrecyclerview.smoothScrollToPosition(0);
                }


                if (horizontalLayoutManager.findLastCompletelyVisibleItemPosition() < (resultrecycleradapter.getItemCount() - 1)) {
                        resultrecyclerview.smoothScrollToPosition(horizontalLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }*/

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
                //  Resultsetting resultsetting=  rideresultlist.get(resultrecycleradapter.getItemCount()-1);

                startActivity(new Intent(getApplicationContext(), Paymentacivity.class));
                finish();


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

    private static final String TAG = "Resultfullscreen";
    private static final float thresholdOffset = 0.5f;
    private boolean scrollStarted, checkDirection;

    private void initComponents() {

        viewPager = findViewById(R.id.result);
        accept = findViewById(R.id.accept);
        next = findViewById(R.id.next);
    }

    private void populategroceryList() {
        Bitmap myLogo = BitmapFactory.decodeResource(this.getResources(), R.drawable.yu);
        Bitmap myLogo1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.african);
        Resultsetting resultsetting = new Resultsetting("Driveaname", "Trivandrum", "Mumbai", "10:50am", "09:00pm", "9Hrs", "Merceedes bens", "Kl1056", (float) 3, (float) 3, myLogo1, myLogo);
        rideresultlist.add(resultsetting);
        resultsetting = new Resultsetting("Suhail T S", "Trivandrum", "Mumbai", " 10:50am", "  09:00pm", " 9Hrs", "Merceedes bens", "Kl1056", (float) 3, (float) 3, myLogo1, myLogo);


        rideresultlist.add(resultsetting);
        rideresultlist.add(resultsetting);

    }

}
