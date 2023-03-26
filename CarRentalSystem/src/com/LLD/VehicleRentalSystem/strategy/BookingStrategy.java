package com.LLD.VehicleRentalSystem.strategy;

import com.LLD.VehicleRentalSystem.constants.VehicleTypes;
import com.LLD.VehicleRentalSystem.models.Vehicle;
import com.LLD.VehicleRentalSystem.service.BranchService;

import java.util.List;

public interface BookingStrategy {
    public Vehicle searchBestVehicle(List<Vehicle> vehicleList, VehicleTypes vehicleTypes);
}
