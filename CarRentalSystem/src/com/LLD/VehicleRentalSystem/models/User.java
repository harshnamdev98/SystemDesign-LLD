package com.LLD.VehicleRentalSystem.models;

public class User {
    private String userId;
    private String userName;
    private String drivingLicense;

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public User(String userName) {
        this.userName = userName;
    }
}

