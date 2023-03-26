package com.LLD.VehicleRentalSystem.repository;

import com.LLD.VehicleRentalSystem.models.Location;

import java.util.List;

public class LocationRepository {
    public static List<Location> locationList;

    public List<Location> getLocationList() {
        return locationList;
    }

    public void addLocation(Location location) {
        locationList.add(location);
    }
}
