package com.example.myride.adpter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.myride.R;
import com.example.myride.model.Resultsetting;

import java.util.List;

public class RecyclerViewHorizontalListAdapter extends PagerAdapter {


    private List<Resultsetting> horizontalGrocderyList;
    Context context;
    LayoutInflater inflater;

    public RecyclerViewHorizontalListAdapter(List<Resultsetting> horizontalGrocderyList, Context context) {
        this.horizontalGrocderyList = horizontalGrocderyList;
        this.context = context;


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



   /* @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.resultfullscreenadapter, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }*/

    /* @Override
     public void onBindViewHolder(GroceryViewHolder holder, final int position) {

         holder.drivername.setText(horizontalGrocderyList.get(position).getDrivername());
         holder.starting.setText(horizontalGrocderyList.get(position).getStarting());
         holder.ending.setText(horizontalGrocderyList.get(position).getEnding());
         holder.departuretime.setText(horizontalGrocderyList.get(position).getDeparturetime());
         holder.arrivaltime.setText(horizontalGrocderyList.get(position).getArrivaltime());
         holder.totaltime.setText(horizontalGrocderyList.get(position).getTotaltime());
         holder.carname.setText(horizontalGrocderyList.get(position).getCarname());
         holder.regno.setText(horizontalGrocderyList.get(position).getRegno());
         holder.driverrating.setRating(horizontalGrocderyList.get(position).getRating());
         holder.driverrating.setRating(horizontalGrocderyList.get(position).getAvailableseat());

         holder.profile.setImageBitmap(horizontalGrocderyList.get(position).getProfile());
         holder.car.setImageBitmap(horizontalGrocderyList.get(position).getCar());


 //        holder.imageView.setImageResource(horizontalGrocderyList.get(position).getProductImage());
 //        holder.txtview.setText(horizontalGrocderyList.get(position).getProductName());
 //        holder.imageView.setOnClickListener(new View.OnClickListener() {
 //            @Override
 //            public void onClick(View v) {
 //                String productName = horizontalGrocderyList.get(position).getProductName().toString();
 //                Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
 //            }
 //        });
     }*/
    ImageView profile, car;

    TextView drivername, starting, ending, departuretime, arrivaltime, totaltime, carname, regno,price;
    RatingBar driverrating, availableseat;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View groceryProductView = inflater.inflate(R.layout.resultfullscreenadapter, container, false);

        container.addView(groceryProductView);
        drivername = groceryProductView.findViewById(R.id.drivername);
        starting = groceryProductView.findViewById(R.id.stating);
        ending = groceryProductView.findViewById(R.id.ending);
        departuretime = groceryProductView.findViewById(R.id.departuretime);
        totaltime = groceryProductView.findViewById(R.id.totaltime);
        arrivaltime = groceryProductView.findViewById(R.id.arrivaltime);
        regno = groceryProductView.findViewById(R.id.regno);
        carname = groceryProductView.findViewById(R.id.carname);
        profile = groceryProductView.findViewById(R.id.profile);
        car = groceryProductView.findViewById(R.id.car);
        driverrating = groceryProductView.findViewById(R.id.ratin);
        availableseat = groceryProductView.findViewById(R.id.noofseat);
        price = groceryProductView.findViewById(R.id.price);


        drivername.setText(horizontalGrocderyList.get(position).getDrivername());
        starting.setText(horizontalGrocderyList.get(position).getStarting());
        ending.setText(horizontalGrocderyList.get(position).getEnding());
        departuretime.setText(horizontalGrocderyList.get(position).getDeparturetime());
        arrivaltime.setText(horizontalGrocderyList.get(position).getArrivaltime());
        totaltime.setText(horizontalGrocderyList.get(position).getTotaltime());
        price.setText(horizontalGrocderyList.get(position).getPrice());
        carname.setText(horizontalGrocderyList.get(position).getCarname());
        regno.setText(horizontalGrocderyList.get(position).getRegno());
        driverrating.setRating(horizontalGrocderyList.get(position).getRating());
        driverrating.setRating(horizontalGrocderyList.get(position).getAvailableseat());
        profile.setImageResource(R.drawable.african);
        car.setImageResource(R.drawable.yu);


        return groceryProductView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return horizontalGrocderyList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

}
