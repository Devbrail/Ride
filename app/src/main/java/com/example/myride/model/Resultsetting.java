package com.example.myride.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Resultsetting implements Parcelable {

    String drivername,starting,ending,departuretime,arrivaltime,totaltime,carname,regno;
    float rating,availableseat;
    Bitmap profile,car;
String price;
String offerRideId;

    public String getOfferRideId() {
        return offerRideId;
    }

    public String getPrice() {
        return price;
    }

    public Resultsetting(String offerRideId, String drivername, String starting, String ending, String departuretime, String arrivaltime, String totaltime, String carname, String regno, float rating, float availableseat, Bitmap profile, Bitmap car, String price) {
        this.offerRideId = offerRideId;
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
        this.price = price;
//        this.profile = profile;
//        this.car = car;
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
    public Resultsetting(Parcel in){

        // the order needs to be the same as in writeToParcel() method

        this.offerRideId = in.readString();
        this.drivername = in.readString();
        this.starting = in.readString();;
        this.ending = in.readString();;
        this.departuretime = in.readString();;
        this.arrivaltime = in.readString();;
        this.totaltime = in.readString();;
        this.carname = in.readString();;
        this.regno = in.readString();;
        this.rating = in.readFloat();
        this.availableseat = in.readFloat();
        this.price = in.readString();
//        this.profile = in.readParcelable(Bitmap.class.getClassLoader());
//        this.car = in.readParcelable(Bitmap.class.getClassLoader());


    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(offerRideId);
        dest.writeString(drivername);
        dest.writeString(starting);
        dest.writeString(ending);
        dest.writeString(departuretime);
        dest.writeString(arrivaltime);
        dest.writeString(totaltime);
        dest.writeString(carname);
        dest.writeString(regno);
        dest.writeFloat(rating);
        dest.writeFloat(availableseat);
        dest.writeString(price);
//        dest.writeValue(profile);
//        dest.writeValue(car);
     }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Resultsetting createFromParcel(Parcel in) {
            return new Resultsetting(in);
        }

        public Resultsetting[] newArray(int size) {
            return new Resultsetting[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

}

