package com.example.myride;

import android.graphics.Bitmap;

public class Movie {
    String drivername,vehciledetail;
    Float rating;
    Bitmap profile;


    public Movie(String drivername, String vehciledetail, Float rating, Bitmap profile) {
        this.drivername = drivername;
        this.vehciledetail = vehciledetail;
        this.rating = rating;
        this.profile = profile;
    }

    public String getDrivername() {
        return drivername;
    }


    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getVehciledetail() {
        return vehciledetail;
    }

    public void setVehciledetail(String vehciledetail) {
        this.vehciledetail = vehciledetail;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Bitmap getProfile() {
        return profile;
    }

    public void setProfile(Bitmap profile) {
        this.profile = profile;
    }
}
