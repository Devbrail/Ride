package com.rider.myride.adpter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rider.myride.R;
import com.rider.myride.Utils.AppConstants;
import com.rider.myride.model.Movie;

import java.util.List;

public class Ridesearchadapter extends RecyclerView.Adapter<Ridesearchadapter.MyViewHolder> {

    AdapterCallback callback;
    Context context;
    private List<Movie> moviesList;

    public Ridesearchadapter(List<Movie> moviesList, Context rideResults, AdapterCallback results) {
        this.moviesList = moviesList;
        this.context = rideResults;
        this.callback = results;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rideresult, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Movie movie = moviesList.get(position);
        holder.textView.setText(movie.getDrivername());
        holder.vehicle.setText(movie.getVehciledetail());
//        holder.ratingBar.setRating((float)movie.getRating());


        Glide.with(context)
                .load(AppConstants.host + AppConstants.Driver + movie.getProfile())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);


        holder.itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClicked(position);
                //  context.startActivity(new Intent(context, Resultfullscreen.class));


            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public interface AdapterCallback {
        void onItemClicked(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView, vehicle;
        ImageView imageView;
        RatingBar ratingBar;
        LinearLayout itemview;

        public MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.drivername);
            imageView = view.findViewById(R.id.profile);
            vehicle = view.findViewById(R.id.vehicle);
            ratingBar = view.findViewById(R.id.ratin);
            itemview = view.findViewById(R.id.itemview);
        }
    }

}