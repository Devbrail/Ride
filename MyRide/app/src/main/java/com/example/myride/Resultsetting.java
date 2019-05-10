package com.example.myride;

import android.graphics.Bitmap;

class Resultsetting {

    String drivername,starting,ending,departuretime,arrivaltime,totaltime,carname,regno;
    float rating,availableseat;
    Bitmap profile,car;

    public Resultsetting(String drivername, String starting, String ending, String departuretime, String arrivaltime, String totaltime, String carname, String regno, float rating, float availableseat, Bitmap profile, Bitmap car) {
        this.drivername = drivername;
        this.starting = starting;
        this.ending = ending;
        this.departuretime = departuretime;
        this.arrivaltime = arrivaltime;
        this.totaltime = totaltime;
        this.carname = carname;
        this.regno = regno;
        this.rating = rating;
        this.availableseat = availableseat;
        this.profile = profile;
        this.car = car;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public String getEnding() {
        return ending;
    }

    public void setEnding(String ending) {
        this.ending = ending;
    }

    public String getDeparturetime() {
        return departuretime;
    }

    public void setDeparturetime(String departuretime) {
        this.departuretime = departuretime;
    }

    public String getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(String arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getAvailableseat() {
        return availableseat;
    }

    public void setAvailableseat(float availableseat) {
        this.availableseat = availableseat;
    }

    public Bitmap getProfile() {
        return profile;
    }

    public void setProfile(Bitmap profile) {
        this.profile = profile;
    }

    public Bitmap getCar() {
        return car;
    }

    public void setCar(Bitmap car) {
        this.car = car;
    }
}

