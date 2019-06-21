package com.example.myride.adpter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myride.R;
import com.example.myride.Utils.AppConstants;
import com.example.myride.Utils.AppUtil;
import com.example.myride.model.Resultsetting;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.List;

public class RecyclerViewHorizontalListAdapter extends PagerAdapter {


    private List<Resultsetting> horizontalGrocderyList;
    Context context;
    LayoutInflater inflater;
    AdapterCallback adapterCallback;

    public RecyclerViewHorizontalListAdapter(List<Resultsetting> horizontalGrocderyList, Context context,AdapterCallback adapterCallback) {
        this.horizontalGrocderyList = horizontalGrocderyList;
        this.context = context;

        this.adapterCallback= adapterCallback;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    ImageView profile, car;

    TextView drivername, starting, ending, departuretime, arrivaltime, totaltime, carname, regno,price;
    RatingBar driverrating, availableseat;
    NumberPicker seatpicker;
    String priceString;

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
        seatpicker = groceryProductView.findViewById(R.id.seatpicker);


        drivername.setText(horizontalGrocderyList.get(position).getDrivername());
        starting.setText(horizontalGrocderyList.get(position).getStarting());
        ending.setText(horizontalGrocderyList.get(position).getEnding());
        departuretime.setText(horizontalGrocderyList.get(position).getDeparturetime());
        arrivaltime.setText(horizontalGrocderyList.get(position).getArrivaltime());
        totaltime.setText(horizontalGrocderyList.get(position).getTotaltime());

        priceString=horizontalGrocderyList.get(position).getPrice();
        price.setText(priceString);
        carname.setText(horizontalGrocderyList.get(position).getCarname());
        regno.setText(horizontalGrocderyList.get(position).getRegno());
        driverrating.setRating(horizontalGrocderyList.get(position).getRating());



        float sad=horizontalGrocderyList.get(position).getAvailableseat();

        float totalseat=horizontalGrocderyList.get(position).getAvailablesea();

        int df= (int) sad;
        int to= (int) totalseat;
        availableseat.setMax(df);
        availableseat.setRating(to-df);

        seatpicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {


                Log.d(TAG, "valueChanged: "+priceString);
                System.out.println("valueChanged: "+priceString);
                int pricInt=Integer.parseInt(priceString.split("\\.")[0]);
                price.setText(pricInt*value+".0/-");
                adapterCallback.onItemClicked(value);
                availableseat.setRating((float)value);

            }
        });

        String driverimage=horizontalGrocderyList.get(position).getDriverImage();
        String carimage=horizontalGrocderyList.get(position).getCar();
        Bitmap myLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.yu);
        Bitmap myLogo1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.african);


        if(driverimage.contains(".jpg")){


            Glide.with(context)
                    .load(AppConstants.host + AppConstants.Car + driverimage)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(car);

        }else {
            car.setImageResource(R.drawable.yu);

        }
        if(carimage.contains(".jpg")){


            Glide.with(context)
                    .load(AppConstants.host + AppConstants.Driver + carimage)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile);


        }else {
            profile.setImageResource(R.drawable.african);

        }



        return groceryProductView;
    }

    private static final String TAG = "RecyclerViewHorizontalL";
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
    public interface AdapterCallback{
        void onItemClicked(int position);
    }
}
