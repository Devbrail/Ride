package com.example.myride.findride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
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
Button accept,next;
    LinearLayoutManager horizontalLayoutManager;
    int srollcount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.result);


        resultrecyclerview = findViewById(R.id.idRecyclerViewHorizontalList);
        // add a divider after each item for more clarity
        resultrecyclerview.addItemDecoration(new DividerItemDecoration(Resultfullscreen.this, LinearLayoutManager.HORIZONTAL));
        resultrecycleradapter = new RecyclerViewHorizontalListAdapter(rideresultlist, getApplicationContext());
          horizontalLayoutManager = new LinearLayoutManager(Resultfullscreen.this, LinearLayoutManager.HORIZONTAL, false);
        resultrecyclerview.setLayoutManager(horizontalLayoutManager);
        resultrecyclerview.setAdapter(resultrecycleradapter);
        PagerSnapHelper mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(resultrecyclerview);
        accept=findViewById(R.id.accept);
        next=findViewById(R.id.next);

       resultrecycleradapter.notifyDataSetChanged();

       resultrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
               super.onScrollStateChanged(recyclerView, newState);

                int visibleItemCount = horizontalLayoutManager.getChildCount();
               int totalItemCount = horizontalLayoutManager.getItemCount();
               int pastVisibleItems = horizontalLayoutManager.findFirstCompletelyVisibleItemPosition();

               if(srollcount>=resultrecycleradapter.getItemCount()-1){
                   // End of the list is here.
                   Toast.makeText(Resultfullscreen.this, "eti", Toast.LENGTH_SHORT).show();
               }

           }

           @Override
           public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);

           }
       });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (horizontalLayoutManager.findLastCompletelyVisibleItemPosition() == (resultrecycleradapter.getItemCount() -1)) {

                    resultrecyclerview.smoothScrollToPosition(0);
                }


                if (horizontalLayoutManager.findLastCompletelyVisibleItemPosition() < (resultrecycleradapter.getItemCount() - 1)) {
                        resultrecyclerview.smoothScrollToPosition(horizontalLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
            }
        });


        try {
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Resultsetting resultsetting=  rideresultlist.get(resultrecycleradapter.getItemCount()-1);

                    startActivity(new Intent(getApplicationContext(),Paymentacivity.class));
                    finish();


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        populategroceryList();
    }

    private void populategroceryList() {
        Bitmap myLogo = BitmapFactory.decodeResource(this.getResources(), R.drawable.yu);
        Bitmap myLogo1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.african);
        Resultsetting resultsetting = new Resultsetting("Driveaname", "Trivandrum", "Mumbai", "10:50am", "09:00pm", "9Hrs", "Merceedes bens", "Kl1056", (float) 5, (float) 5, myLogo1, myLogo);
        rideresultlist.add(resultsetting);
          resultsetting = new Resultsetting("Suhail T S", "Trivandrum", "Mumbai", " 10:50am", "  09:00pm", " 9Hrs", "Merceedes bens", "Kl1056", (float) 5, (float) 5, myLogo1, myLogo);


        rideresultlist.add(resultsetting);
        rideresultlist.add(resultsetting);

    }

}
