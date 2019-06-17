package com.example.myride.model;

public class CardetailsPOJO {

    String carId,  carName,
     carNumber,  carModel,  carColor,
     seatNumber,  userId,  carImage,  insuranceId,
     insuranceCompany,  expiryDate,  driverId,
     driverName,  nin,  gender,  email,
     dob,  userPic,  drivngLicence,
     drivngLicenceExpiry;

    public String getCarId() {
        return carId;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getCarImage() {
        return carImage;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getNin() {
        return nin;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getUserPic() {
        return userPic;
    }

    public String getDrivngLicence() {
        return drivngLicence;
    }

    public String getDrivngLicenceExpiry() {
        return drivngLicenceExpiry;
    }

    public CardetailsPOJO(String carId, String carName, String carNumber,
                          String carModel, String carColor, String seatNumber,
                          String userId, String carImage, String insuranceId,
                          String insuranceCompany, String expiryDate, String driverId,
                          String driverName, String nin, String gender, String email,
                          String dob, String userPic,
                          String drivngLicence, String drivngLicenceExpiry) {
        this.carId = carId;
        this.carName = carName;
        this.carNumber = carNumber;
        this.carModel = carModel;
        this.carColor = carColor;
        this.seatNumber = seatNumber;
        this.userId = userId;
        this.carImage = carImage;
        this.insuranceId = insuranceId;
        this.insuranceCompany = insuranceCompany;
        this.expiryDate = expiryDate;
        this.driverId = driverId;
        this.driverName = driverName;
        this.nin = nin;
        this.gender = gender;
        this.email = email;
        this.dob = dob;
        this.userPic = userPic;
        this.drivngLicence = drivngLicence;
        this.drivngLicenceExpiry = drivngLicenceExpiry;
    }
}
