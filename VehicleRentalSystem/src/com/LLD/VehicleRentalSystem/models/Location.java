package com.LLD.VehicleRentalSystem.models;

public class Location {
    private String address;
    private String city;
    private String state;
    private String country;
    private int pinCode;


    public Location(String address, String city, String state, String country, int pinCode) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
