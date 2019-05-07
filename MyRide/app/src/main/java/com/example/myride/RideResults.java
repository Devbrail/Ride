package com.example.myride;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class RideResults extends AppCompatActivity {

    ArrayList<String> basicFields;
    Ridesearchadapter adapter;
    GridView gv;
    TextView tittle;
    private static final String TAG = "RideResults";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_results);
        basicFields = new ArrayList<>();
        gv = (GridView) findViewById(R.id.grid);
        tittle=findViewById(R.id.textView);
        basicFields.add("Find a ride");
        basicFields.add("Offer a ride");
        adapter = new Ridesearchadapter(this, basicFields,getApplicationContext());
        gv.setAdapter(adapter);

        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("numbers");
        tittle.setText(myList.get(2)+" -\n"+myList.get(3));

        Log.d(TAG, "onCreate: "+myList.toString());
    }
}
