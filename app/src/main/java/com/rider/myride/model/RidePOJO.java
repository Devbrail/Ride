package com.rider.myride.model;

public class RidePOJO {
    String carImage, driverImage;
    int seatofferd;
    String startDate;
    String fromLocation;
    String toLocation;
    String pric;
    String noOfSeats;
    String carName;
    String carNumber;
    String drivernam;

    public RidePOJO(String startDate, String fromLocation, String toLocation, String pric, String noOfSeats, String carName, String carNumber, String drivernam, String carImage, String driverimage, int seatofferd) {
        this.startDate = startDate;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.pric = pric;
        this.noOfSeats = noOfSeats;
        this.carName = carName;
        this.carNumber = carNumber;
        this.drivernam = drivernam;
        this.carImage = carImage;
        this.driverImage = driverimage;
        this.seatofferd = seatofferd;
    }

    public String getCarImage() {
        return carImage;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public int getSeatofferd() {
        return seatofferd;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public String getPric() {
        return pric;
    }

    public String getNoOfSeats() {
        return noOfSeats;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getDrivernam() {
        return drivernam;
    }
}
