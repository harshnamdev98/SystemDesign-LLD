package com.LLD.VehicleRentalSystem.models;

import com.LLD.VehicleRentalSystem.constants.VehicleStatus;
import com.LLD.VehicleRentalSystem.constants.VehicleTypes;

import java.util.UUID;

public class Vehicle {
    @Override
    public String toString(){
        return vehicleNo+vehicleType+isBooked;
    }
    private String vehicleId;
    private String vehicleNo;
    private String branchId;
    private String branchName;
    private VehicleTypes vehicleType;
    private VehicleStatus vehicleStatus;
    private Boolean isBooked;

    public Vehicle(String vehicleNo, String branchId, String branchName, VehicleTypes vehicleType) {
        this.vehicleId = UUID.randomUUID().toString();
        this.vehicleNo = vehicleNo;
        this.branchName = branchName;
        this.branchId = branchId;
        this.vehicleType = vehicleType;
        this.vehicleStatus = VehicleStatus.ACTIVE;
        this.isBooked = Boolean.FALSE;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public VehicleTypes getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypes vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }
}
