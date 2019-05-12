package com.example.myride.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.myride.R;
import com.example.myride.model.Resultsetting;

import java.util.List;

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.GroceryViewHolder>{



    private List<Resultsetting> horizontalGrocderyList;
    Context context;

    public RecyclerViewHorizontalListAdapter(List<Resultsetting> horizontalGrocderyList, Context context){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.resultfullscreenadapter, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
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
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView profile,car;

        TextView drivername,starting,ending,departuretime,arrivaltime,totaltime,carname,regno;
        RatingBar driverrating,availableseat;

        public GroceryViewHolder(View view) {
            super(view);

            drivername=view.findViewById(R.id.drivername);
            starting=view.findViewById(R.id.stating);
            ending=view.findViewById(R.id.ending);
            departuretime=view.findViewById(R.id.departuretime);
            totaltime=view.findViewById(R.id.totaltime);
            arrivaltime=view.findViewById(R.id.arrivaltime);
            regno=view.findViewById(R.id.regno);
            carname=view.findViewById(R.id.carname);

            profile=view.findViewById(R.id.profile);
            car=view.findViewById(R.id.car);
            driverrating=view.findViewById(R.id.ratin);
            availableseat=view.findViewById(R.id.noofseat);




        }
    }
}
