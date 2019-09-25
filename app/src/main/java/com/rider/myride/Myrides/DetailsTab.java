package com.rider.myride.Myrides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rider.myride.R;
import com.rider.myride.Utils.AppConstants;
import com.rider.myride.model.RidePOJO;

public class DetailsTab extends Fragment {

    RidePOJO ridePOJOArrayList;

    public DetailsTab(RidePOJO ridePOJOArrayList) {
        this.ridePOJOArrayList = ridePOJOArrayList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.resultfullscreenadapter, container, false);

        final ImageView driverImage, CarIamge;
        final TextView drivername, starting, ending, departuretime, arrivaltime, totaltime, carname, regno, price;
        LinearLayout picker;

        RatingBar driverrating, availableseat;
        drivername = rootView.findViewById(R.id.drivername);
        starting = rootView.findViewById(R.id.stating);
        ending = rootView.findViewById(R.id.ending);
        departuretime = rootView.findViewById(R.id.depru);
        totaltime = rootView.findViewById(R.id.totu);
        departuretime.setVisibility(View.INVISIBLE);
        totaltime.setVisibility(View.INVISIBLE);
        arrivaltime = rootView.findViewById(R.id.arrivaltime);
        regno = rootView.findViewById(R.id.regno);
        carname = rootView.findViewById(R.id.carname);
        CarIamge = rootView.findViewById(R.id.car);
        picker = rootView.findViewById(R.id.picker);
        picker.setVisibility(View.GONE);


        driverrating = rootView.findViewById(R.id.ratin);
        availableseat = rootView.findViewById(R.id.noofseat);
        price = rootView.findViewById(R.id.price);
        driverImage = rootView.findViewById(R.id.profile);

        int a = Integer.parseInt(ridePOJOArrayList.getNoOfSeats());


        int b = ridePOJOArrayList.getSeatofferd();
        availableseat.setMax(a);
        availableseat.setNumStars(a);

        availableseat.setRating(b);

        drivername.setText(ridePOJOArrayList.getDrivernam());
        starting.setText(ridePOJOArrayList.getFromLocation());
        ending.setText(ridePOJOArrayList.getToLocation());
        arrivaltime.setText(ridePOJOArrayList.getStartDate());
        drivername.setText(ridePOJOArrayList.getDrivernam());
        regno.setText(ridePOJOArrayList.getCarNumber());
        carname.setText(ridePOJOArrayList.getCarName());
        price.setText(ridePOJOArrayList.getPric() + ".00/-");


        String driveimag = ridePOJOArrayList.getDriverImage();
        String carimage = ridePOJOArrayList.getCarImage();

        if (driveimag.contains("jpg")) {
            Glide.with(getActivity())
                    .load(AppConstants.host + AppConstants.Driver + driveimag)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(driverImage);
        }
        if (carimage.contains("jpg")) {
            Glide.with(getActivity())
                    .load(AppConstants.host + AppConstants.Car + carimage)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(CarIamge);
        }
        return rootView;
    }
}
