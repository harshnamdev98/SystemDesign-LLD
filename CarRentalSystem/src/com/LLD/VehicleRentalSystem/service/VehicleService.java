package com.LLD.VehicleRentalSystem.service;

import com.LLD.VehicleRentalSystem.constants.VehicleTypes;
import com.LLD.VehicleRentalSystem.models.Branches;
import com.LLD.VehicleRentalSystem.models.Vehicle;
import com.LLD.VehicleRentalSystem.repository.VehicleRepository;

import java.beans.JavaBean;
import java.util.ArrayList;
import java.util.List;


public class VehicleService {
    private VehicleRepository vehicleRepository = new VehicleRepository(new ArrayList<>());

    public Vehicle addVehicle(String vehicleNo, VehicleTypes vehicleType, Branches branches) {
        Vehicle vehicle = new Vehicle(vehicleNo, branches.getBranchId(), branches.getBranchName(), vehicleType);
        vehicleRepository.addVehicle(vehicle);
        return  vehicle;
    }

    public List<Vehicle> getUnbookedVehicle() {
        List<Vehicle> vehicleList = vehicleRepository.getAllUnBookedVehicles();

        return vehicleList;
    }

}
