package com.example.myride;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RideResults extends AppCompatActivity implements RideviewFragment.ExampleDialogListener  {

    ArrayList<String> drivename;
    ArrayList<String> vehicle_details;

    int rating;
    ArrayList<Bitmap> profile;



    Ridesearchadapter adapter;
     TextView tittle;
    RecyclerView recyclerView;
    private List< Movie> movieList = new ArrayList<>();

    private static final String TAG = "RideResults";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_results);
        drivename = new ArrayList<>();
        vehicle_details = new ArrayList<>();
        profile = new ArrayList<>();



          icon = BitmapFactory.decodeResource(getResources(),R.drawable.round);

         recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new Ridesearchadapter(movieList,RideResults.this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        tittle=findViewById(R.id.textView);


        prepareMovieData();

//        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("numbers");
//        tittle.setText(myList.get(2)+" -\n"+myList.get(3));

       // Log.d(TAG, "onCreate: "+myList.toString());
    }
    public void openDialog() {
        RideviewFragment exampleDialog = new RideviewFragment();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    Bitmap icon;
    private void prepareMovieData() {
        Movie movie=new  Movie("John lucifer","Toyota Innovo\nKL 23405", (float) 4.0,icon);

        movieList.add(movie);
          movie=new  Movie("Suhail TS","Mercedes \nHN 1356",(float)4,icon);
        movieList.add(movie);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void applyTexts(String username, String password) {


    }
}
