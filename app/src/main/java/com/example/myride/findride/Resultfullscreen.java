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
        Bundle b=getIntent().getExtras();


        rideresultlist = b.getParcelableArrayList("array");
         position= Integer.parseInt(b.getString("posi"));


    }
    int position=0;
}
