<<<<<<< HEAD:app/src/main/java/com/example/myride/adpter/OfferRideListAdapter.java
package com.example.myride.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myride.Fragment.OfferrideFragment;
import com.example.myride.R;
import com.example.myride.Utils.AppConstants;

import java.util.ArrayList;


public class OfferRideListAdapter extends RecyclerView.Adapter<OfferRideListAdapter.ViewHolder> {
    boolean isexpanded = false;
    private ArrayList<OfferrideFragment.RidePOJO> ridePOJOArrayList;
Context mcontext;
    public OfferRideListAdapter(ArrayList<OfferrideFragment.RidePOJO> ridePOJOArrayList,Context context) {
        this.ridePOJOArrayList = ridePOJOArrayList;
        mcontext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.fragmentt_findride, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String header = ridePOJOArrayList.get(position).getFromLocation() + " - " + ridePOJOArrayList.get(position).getToLocation();
        holder.from.setText(header);
        holder.date.setText(ridePOJOArrayList.get(position).getStartDate());
        holder.price.setText(ridePOJOArrayList.get(position).getPric());
        holder.seats.setText(ridePOJOArrayList.get(position).getNoOfSeats());
        holder.carname.setText(ridePOJOArrayList.get(position).getCarName() + "\n" + ridePOJOArrayList.get(position).getCarNumber());

        holder.drivername.setText(ridePOJOArrayList.get(position).getDrivernam());
        holder.occupied.setText(ridePOJOArrayList.get(position).getOccupiedseat());

        String driverimag = ridePOJOArrayList.get(position).getUserIamge();

            if(driverimag.contains("jpg")) {
                Glide.with(mcontext)
                        .load(AppConstants.host + AppConstants.Driver + driverimag)
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.caricon)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView);
            }


            holder.from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });


    }

    @Override
    public int getItemCount() {
        return ridePOJOArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView from;
        TextView date;
        TextView price;
        TextView seats;
        TextView carname;

        TextView occupied;
        TextView drivername;
        ImageView imageView;


        ViewHolder(View itemView) {
            super(itemView);

            this.from = itemView.findViewById(R.id.from);

            this.date = itemView.findViewById(R.id.date);
            this.price = itemView.findViewById(R.id.price);
            this.seats = itemView.findViewById(R.id.seats);
            this.carname = itemView.findViewById(R.id.vehicle);

            this.drivername = itemView.findViewById(R.id.drivername);
            // this.rotate=itemView.findViewById(R.id.rotate);
            this.occupied = itemView.findViewById(R.id.occupied);


            imageView = itemView.findViewById(R.id.profile);


        }
    }
}
=======
package com.rider.myride.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rider.myride.Myrides.Myrides;
import com.rider.myride.Myrides.Offerfragment;
import com.rider.myride.R;
import com.rider.myride.Utils.AppConstants;

import java.util.ArrayList;
import java.util.Objects;


public class OfferRideListAdapter extends RecyclerView.Adapter<OfferRideListAdapter.ViewHolder> {
    boolean isexpanded = false;
    Context mcontext;
    private ArrayList<Myrides.RidePOJOOffer> ridePOJOArrayList;

    public OfferRideListAdapter(ArrayList<Myrides.RidePOJOOffer> ridePOJOArrayList, Context context) {
        this.ridePOJOArrayList = ridePOJOArrayList;
        mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.fragmentt_findride, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String header = ridePOJOArrayList.get(position).getFromLocation() + " - " + ridePOJOArrayList.get(position).getToLocation();
        holder.from.setText(header);
        holder.date.setText(ridePOJOArrayList.get(position).getStartDate());
        holder.price.setText(ridePOJOArrayList.get(position).getPric());
        holder.seats.setText(ridePOJOArrayList.get(position).getNoOfSeats());
        holder.carname.setText(ridePOJOArrayList.get(position).getCarName() + "\n" + ridePOJOArrayList.get(position).getCarNumber());

        holder.drivername.setText(ridePOJOArrayList.get(position).getDrivernam());
        holder.occupied.setText(ridePOJOArrayList.get(position).getOccupiedseat());

        String driverimag = ridePOJOArrayList.get(position).getUserIamge();

        if (driverimag.contains("jpg")) {
            Glide.with(mcontext)
                    .load(AppConstants.host + AppConstants.Driver + driverimag)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }


        holder.offeritem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AppCompatActivity activity = (AppCompatActivity) mcontext;

                Objects.requireNonNull(activity.getSupportActionBar()).hide();
                Fragment myFragment = new Offerfragment(ridePOJOArrayList.get(position));
                activity.getSupportFragmentManager().beginTransaction().replace(android.R.id.content, myFragment).addToBackStack(null).commit();


            }
        });


    }

    @Override
    public int getItemCount() {
        return ridePOJOArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView from;
        TextView date;
        TextView price;
        TextView seats;
        TextView carname;

        TextView occupied;
        TextView drivername;
        ImageView imageView;
        LinearLayout offeritem;


        ViewHolder(View itemView) {
            super(itemView);

            this.from = itemView.findViewById(R.id.from);

            this.date = itemView.findViewById(R.id.date);
            this.price = itemView.findViewById(R.id.price);
            this.seats = itemView.findViewById(R.id.seats);
            this.carname = itemView.findViewById(R.id.vehicle);

            this.drivername = itemView.findViewById(R.id.drivername);
            // this.rotate=itemView.findViewById(R.id.rotate);
            this.occupied = itemView.findViewById(R.id.occupied);


            imageView = itemView.findViewById(R.id.profile);
            offeritem = itemView.findViewById(R.id.itemview);


        }
    }
}
>>>>>>> 4f76eb22a123de7cb959c7a8907a0ebd1c435bfa:app/src/main/java/com/rider/myride/adpter/OfferRideListAdapter.java
