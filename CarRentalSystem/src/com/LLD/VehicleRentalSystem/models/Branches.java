package com.LLD.VehicleRentalSystem.models;

import com.LLD.VehicleRentalSystem.repository.VehicleRepository;
import com.LLD.VehicleRentalSystem.constants.VehicleTypes;

import java.util.*;

public class Branches {
    private String branchId;
    private String branchName;
    private Location location;
    private VehicleRepository vehicleInventoryMangement;
    private List<Booking> bookingList = new ArrayList<>();
    private Map<VehicleTypes, Integer> vehicleTypePriceMap = new HashMap<>();

    public Branches(String branchName, Location location) {
        String uniqueID = UUID.randomUUID().toString();
        this.branchId = uniqueID;
        this.branchName = branchName;
        this.location = location;
    }

    public void setVehicleTypePriceMap(Map<VehicleTypes, Integer> vehicleTypePriceMap) {
        this.vehicleTypePriceMap = vehicleTypePriceMap;
    }

    public Map<VehicleTypes, Integer> getVehicleTypePriceMap() {
        System.out.println("vehicleTypePriceMap "+vehicleTypePriceMap);
        return vehicleTypePriceMap;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    //    public List<Vehicle>

}
