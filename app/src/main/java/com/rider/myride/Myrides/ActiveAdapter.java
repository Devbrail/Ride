package com.rider.myride.Myrides;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.rider.myride.R;
import com.rider.myride.Services.NetworkServiceCall;
import com.rider.myride.Services.ServicesCallListener;
import com.rider.myride.Utils.AppConstants;
import com.rider.myride.Utils.AppUtil;

import org.json.JSONObject;

import java.util.ArrayList;

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.MyViewHolder> {

    private static final String TAG = "ActiveAdapter";
    ArrayList<Activeforoffer.ActivePojo> activePojoArrayList;
    Myrides.RidePOJOOffer ridePOJOOffer;
    Context context;

    ActiveAdapter(ArrayList<Activeforoffer.ActivePojo> activePojoArrayList, Myrides.RidePOJOOffer ridePOJOOffer, FragmentActivity activity) {
        this.activePojoArrayList = activePojoArrayList;
        this.ridePOJOOffer = ridePOJOOffer;
        this.context = activity;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activeadapter, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: ");
        holder.from.setText(activePojoArrayList.get(position).fromLocation + " - " +
                activePojoArrayList.get(position).getToLocation());
        holder.vehicle.setText(activePojoArrayList.get(position).userName);
        holder.vehicle.setText(activePojoArrayList.get(position).price);

        if (activePojoArrayList.get(position).getRideStatus() == 1) {

            holder.acction.setVisibility(View.VISIBLE);
        }
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("offerRideId", ridePOJOOffer.offerRideId);
                    jsonObject.put("userId", AppUtil.getuserid(context));
                    jsonObject.put("paymentStatus", 1);
                    jsonObject.put("amount", ridePOJOOffer.getPric());
                    jsonObject.put("status", 2);


                    NetworkServiceCall serviceCall = new NetworkServiceCall(context, true);
                    serviceCall.setOnServiceCallCompleteListener(new onServiceCallCompleteListene());
                    serviceCall.makeJSONObjectPostRequest(AppConstants.URL + AppConstants.CONFIRM_OFFER, jsonObject, Request.Priority.IMMEDIATE);


                } catch (Exception e) {

                }

            }
        });


    }

    @Override
    public int getItemCount() {
//        return 4;
        return activePojoArrayList.size();
    }

    private class onServiceCallCompleteListene implements ServicesCallListener {
        @Override
        public void onJSONObjectResponse(JSONObject jsonObject) {
            if (jsonObject.has("offerRideId")) {

                ((AppCompatActivity) context).onBackPressed();


            }

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Crashlytics.logException(error);

        }

        @Override
        public void onStringResponse(String string) {

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView from, date, vehicle, price, noofseat;
        ImageView imageView;

        LinearLayout acction;
        Button accept, deny;

        public MyViewHolder(View view) {
            super(view);

            Log.d(TAG, "MyViewHolder: ");
            from = view.findViewById(R.id.from);
            date = view.findViewById(R.id.date);
            date = view.findViewById(R.id.date);
            vehicle = view.findViewById(R.id.vehicle);
            price = view.findViewById(R.id.price);
            noofseat = view.findViewById(R.id.noofseat);
            acction = view.findViewById(R.id.acction);
            accept = view.findViewById(R.id.accept);
            deny = view.findViewById(R.id.deny);

            acction.setVisibility(View.GONE);
        }
    }
}

