package com.example.myride.adpter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myride.Fragment.OfferrideFragment;
import com.example.myride.R;

import java.util.ArrayList;



public class OfferRideListAdapter extends RecyclerView.Adapter<OfferRideListAdapter.ViewHolder> {
    private ArrayList<OfferrideFragment.RidePOJO> ridePOJOArrayList;
    public OfferRideListAdapter(ArrayList<OfferrideFragment.RidePOJO> ridePOJOArrayList) {
        this.ridePOJOArrayList=ridePOJOArrayList;
    }

    boolean isexpanded=false;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.fragmentt_findride, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String header=ridePOJOArrayList.get(position).getFromLocation()+" - "+ridePOJOArrayList.get(position).getToLocation();
        holder.from.setText(header);
        holder.date.setText(ridePOJOArrayList.get(position).getStartDate());
        holder.price.setText(ridePOJOArrayList.get(position).getPric());
        holder.seats.setText(ridePOJOArrayList.get(position).getNoOfSeats());
        holder.carname.setText(ridePOJOArrayList.get(position).getCarName()+"\n"+ridePOJOArrayList.get(position).getCarNumber());

        holder.drivername.setText(ridePOJOArrayList.get(position).getDrivernam());
        holder.occupied.setText(ridePOJOArrayList.get(position).getOccupiedseat());

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


        ViewHolder(View itemView) {
            super(itemView);

            this.from=itemView.findViewById(R.id.from);

            this.date=itemView.findViewById(R.id.date);
            this.price=itemView.findViewById(R.id.price);
            this.seats=itemView.findViewById(R.id.seats);
            this.carname=itemView.findViewById(R.id.vehicle);

            this.drivername=itemView.findViewById(R.id.drivername);
           // this.rotate=itemView.findViewById(R.id.rotate);
            this.occupied=itemView.findViewById(R.id.occupied);



        }
    }
}
