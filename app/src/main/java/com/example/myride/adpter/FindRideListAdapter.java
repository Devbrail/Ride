package com.example.myride.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.example.myride.Myrides.FindRideFragment;
import com.example.myride.Myrides.Myrides;
import com.example.myride.R;
import com.example.myride.Utils.AppConstants;
import com.example.myride.model.RidePOJO;

import java.util.ArrayList;


public class FindRideListAdapter extends RecyclerView.Adapter<FindRideListAdapter.ViewHolder> {
    boolean isexpanded = false;
    Context context;
    private ArrayList<RidePOJO> ridePOJOArrayList;

    public FindRideListAdapter(ArrayList<RidePOJO> ridePOJOArrayList) {
        this.ridePOJOArrayList = ridePOJOArrayList;
    }

    public FindRideListAdapter(ArrayList<RidePOJO> ridePOJOArrayList, Context activity) {

        this.ridePOJOArrayList = ridePOJOArrayList;
        this.context = activity;

    }

   private Bitmap bitmap=null;
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
        holder.price.setText(ridePOJOArrayList.get(position).getPric() + ".00/-");
        holder.seats.setText(ridePOJOArrayList.get(position).getNoOfSeats());
        holder.carname.setText(ridePOJOArrayList.get(position).getCarName() + "\n" + ridePOJOArrayList.get(position).getCarNumber());
//        holder.carno.setText(ridePOJOArrayList.get(position).getCarNumber());
        holder.drivername.setText(ridePOJOArrayList.get(position).getDrivernam());
      final String carimage;
      String driveimag=ridePOJOArrayList.get(position).getDriverImage();

        if(driveimag.contains("jpg")) {
            Glide.with(context)
                    .load(AppConstants.host + AppConstants.Driver + driveimag)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.driverImage);
        }


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final PopupWindow popupWindow;
                try {
                    LayoutInflater inflater = (LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.resultfullscreenadapter, null);


                    int height = LinearLayout.LayoutParams.MATCH_PARENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    popupWindow = new PopupWindow(popupView, height, height, focusable);

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken

                    LinearLayout picker;

                    final ImageView driverImage,CarIamge;
                    final TextView drivername, starting, ending, departuretime, arrivaltime, totaltime, carname, regno, price;
                    RatingBar driverrating, availableseat;
                    drivername = popupView.findViewById(R.id.drivername);
                    starting = popupView.findViewById(R.id.stating);
                    ending = popupView.findViewById(R.id.ending);
                    departuretime = popupView.findViewById(R.id.depru);
                    totaltime = popupView.findViewById(R.id.totu);
                    departuretime.setVisibility(View.INVISIBLE);
                    totaltime.setVisibility(View.INVISIBLE);
                    arrivaltime = popupView.findViewById(R.id.arrivaltime);
                    regno = popupView.findViewById(R.id.regno);
                    carname = popupView.findViewById(R.id.carname);
                    CarIamge = popupView.findViewById(R.id.car);
                    picker = popupView.findViewById(R.id.picker);
                    picker.setVisibility(View.GONE);


                    driverrating = popupView.findViewById(R.id.ratin);
                    availableseat = popupView.findViewById(R.id.noofseat);
                    price = popupView.findViewById(R.id.price);
                    driverImage = popupView.findViewById(R.id.profile);

                    int a= Integer.parseInt(ridePOJOArrayList.get(position).getNoOfSeats());


                    int b= ridePOJOArrayList.get(position).getSeatofferd();
                    availableseat.setMax(a);
                    availableseat.setNumStars(a);

                    availableseat.setRating(b);

                    drivername.setText(ridePOJOArrayList.get(position).getDrivernam());
                    starting.setText(ridePOJOArrayList.get(position).getFromLocation());
                    ending.setText(ridePOJOArrayList.get(position).getToLocation());
                    arrivaltime.setText(ridePOJOArrayList.get(position).getStartDate());
                    drivername.setText(ridePOJOArrayList.get(position).getDrivernam());
                    regno.setText(ridePOJOArrayList.get(position).getCarNumber());
                    carname.setText(ridePOJOArrayList.get(position).getCarName());
                    price.setText(ridePOJOArrayList.get(position).getPric() + ".00/-");


                    String driveimag=ridePOJOArrayList.get(position).getDriverImage();
                    String carimage=ridePOJOArrayList.get(position).getCarImage();

                   if(driveimag.contains("jpg")) {
                          Glide.with(context)
                    .load(AppConstants.host + AppConstants.Driver + driveimag)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(driverImage);
                    }
                     if(carimage.contains("jpg")) {
                         Glide.with(context)
                                 .load(AppConstants.host + AppConstants.Car + carimage)
                                 .thumbnail(0.5f)
                                 .crossFade()
                                 .diskCacheStrategy(DiskCacheStrategy.ALL)
                                 .into(CarIamge);
                    }
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                } catch (Exception e) {
            Crashlytics.logException(e);
e.printStackTrace();

                }

                //availableseat.setProgress(Integer.parseInt(ridePOJOArrayList.get(position).getNoOfSeats()));




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
        ImageView driverImage;
        RatingBar noofseat;

        ViewHolder(View itemView) {
            super(itemView);

            this.from = itemView.findViewById(R.id.from);

            this.date = itemView.findViewById(R.id.date);
            this.price = itemView.findViewById(R.id.price);
            this.seats = itemView.findViewById(R.id.seats);
            this.carname = itemView.findViewById(R.id.vehicle);
            this.carno = itemView.findViewById(R.id.carno);
            this.drivername = itemView.findViewById(R.id.drivername);
            this.layout = itemView.findViewById(R.id.itemview);
//            this.noofseat=itemView.findViewById(R.id.noofseat);
            this.driverImage = itemView.findViewById(R.id.profile);
            //this.noofseat.setVisibility(View.GONE);


        }
    }
}
