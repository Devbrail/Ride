package com.example.myride.adpter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.myride.R;
import com.example.myride.findride.Resultfullscreen;
import com.example.myride.model.Movie;

import java.util.List;

public class Ridesearchadapter extends RecyclerView.Adapter<Ridesearchadapter.MyViewHolder> {

    private List<Movie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView,vehicle;
        ImageView imageView;
        RatingBar ratingBar;
        LinearLayout itemview;

        public MyViewHolder(View view) {
            super(view);
            textView= (TextView)view.findViewById(R.id.drivername);
            imageView = (ImageView)view.findViewById(R.id.profile);
            vehicle=view.findViewById(R.id.vehicle);
            ratingBar=view.findViewById(R.id.ratin);
            itemview=view.findViewById(R.id.itemview);
        }
    }

    Context context;
    public Ridesearchadapter(List<Movie> moviesList, Context rideResults) {
        this.moviesList = moviesList;
        this.context=rideResults;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rideresult, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.textView.setText(movie.getDrivername());
        holder.vehicle.setText(movie.getVehciledetail());
//        holder.ratingBar.setRating((float)movie.getRating());
        holder.imageView.setImageBitmap(movie.getProfile());

        holder.itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               context.startActivity(new Intent(context, Resultfullscreen.class));
                


            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}