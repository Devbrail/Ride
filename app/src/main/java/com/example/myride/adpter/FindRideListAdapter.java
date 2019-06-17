package com.example.myride.adpter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myride.Fragment.FindRideFragment;
import com.example.myride.R;

import java.util.ArrayList;



public class FindRideListAdapter extends RecyclerView.Adapter<FindRideListAdapter.ViewHolder> {
    private ArrayList<FindRideFragment.RidePOJO> ridePOJOArrayList;
    public FindRideListAdapter(ArrayList<FindRideFragment.RidePOJO> ridePOJOArrayList) {
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
        holder.carname.setText(ridePOJOArrayList.get(position).getCarName());
        holder.carno.setText(ridePOJOArrayList.get(position).getCarNumber());
        holder.drivername.setText(ridePOJOArrayList.get(position).getDrivernam());
        final RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setInterpolator(new LinearInterpolator());

        holder.from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isexpanded ){
                    holder.layout.setVisibility(View.GONE);
                    isexpanded=false;
                    notifyItemChanged(position);

                    holder.rotate.startAnimation(rotate);

                }
                else {
                    holder.layout.setVisibility(View.VISIBLE);
                    isexpanded=true;
                    notifyItemChanged(position);

                    holder.rotate.startAnimation(rotate);

                }
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
        TextView carno;
        TextView drivername;
        LinearLayout layout;
        ImageView rotate;

        ViewHolder(View itemView) {
            super(itemView);

            this.from=itemView.findViewById(R.id.from);

            this.date=itemView.findViewById(R.id.date);
            this.price=itemView.findViewById(R.id.price);
            this.seats=itemView.findViewById(R.id.seats);
            this.carname=itemView.findViewById(R.id.carname);
            this.carno=itemView.findViewById(R.id.carno);
            this.drivername=itemView.findViewById(R.id.drivername);
            this.layout=itemView.findViewById(R.id.layout);
            this.rotate=itemView.findViewById(R.id.rotate);


        }
    }
}
