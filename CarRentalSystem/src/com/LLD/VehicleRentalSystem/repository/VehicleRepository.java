package com.LLD.VehicleRentalSystem.repository;

import com.LLD.VehicleRentalSystem.constants.VehicleTypes;
import com.LLD.VehicleRentalSystem.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleRepository {
    private List<Vehicle> vehicles;

    public VehicleRepository(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Vehicle> getAllVehicles(String VehicleType) {
        return vehicles;
    }

    public List<Vehicle> getVehiclesFilterByType(VehicleTypes vehicleType) {
        List<Vehicle> ansList = new ArrayList<>();
        for (Vehicle vehicle: vehicles) {
            if (vehicle.getVehicleType() == vehicleType){
                ansList.add(vehicle);
            }
        }
        return ansList;
    }

    public List<Vehicle> getAllUnBookedVehicles() {
        List<Vehicle> ansList = new ArrayList<>();
        for (Vehicle vehicle: vehicles) {
            if (!vehicle.getBooked()){
                ansList.add(vehicle);
            }
        }
        return ansList;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }


}
