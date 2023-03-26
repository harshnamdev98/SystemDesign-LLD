package com.LLD.VehicleRentalSystem.service;

import com.LLD.VehicleRentalSystem.models.Location;
import com.LLD.VehicleRentalSystem.repository.LocationRepository;

import java.util.List;

public class LocationService {

    Location location;
    LocationRepository locationRepository;

    public Location getLocationByCity(String city) {
        List<Location> locationList = locationRepository.getLocationList();
        for (Location location: locationList) {
            if (location.getCity() == city) {
                return location;
            }
        }
        return null;
    }
}
