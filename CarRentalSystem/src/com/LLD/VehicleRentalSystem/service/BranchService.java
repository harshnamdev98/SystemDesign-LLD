package com.LLD.VehicleRentalSystem.service;

import com.LLD.VehicleRentalSystem.constants.VehicleTypes;
import com.LLD.VehicleRentalSystem.exceptions.BranchNotFoundException;
import com.LLD.VehicleRentalSystem.models.Branches;
import com.LLD.VehicleRentalSystem.models.Location;
import com.LLD.VehicleRentalSystem.models.Vehicle;
import com.LLD.VehicleRentalSystem.repository.BranchRepository;

import java.util.List;

public class BranchService {
    private Branches branches;

    public BranchService(List<Branches> branchesList, VehicleService vehicleService) {
        this.branchRepository = new BranchRepository(branchesList);
        this.branches = branches;
        this.vehicleService = vehicleService;
    }

    private BranchRepository branchRepository;
    private VehicleService vehicleService;

    public Branches createAndAddBranch(String branchName, Location location) {
        Branches branch = new Branches(branchName, location);
        branchRepository.addBranch(branches);
        return branch;
    }

    public void updateVehicleTypePrice(String branchName, String vehicleType, int price) throws BranchNotFoundException {
        Branches branch = branchRepository.getBranchByName(branchName);
        if (branch==null) {
            throw new BranchNotFoundException("Branch not found with name: " + branchName);
        }
        VehicleTypes type = VehicleTypes.valueOf(vehicleType);
        this.updateVehicleTypePriceMap(branch, type, price);
    }

    public void updateVehicleTypePriceMap(Branches branch, VehicleTypes vehicleType, int price) {
        branch.getVehicleTypePriceMap().put(vehicleType, price);
    }

    public Branches getBranchByName(String branchName) {
        return branchRepository.getBranchByName(branchName);
    }

    public Vehicle addVehicleToBranch(String vehicleNo, VehicleTypes vehicleType, String branchName) {
        Branches branch = this.getBranchByName(branchName);

        Vehicle vehicle = vehicleService.addVehicle(vehicleNo, vehicleType, branch);
        return  vehicle;
    }
    public List<Branches> getAllBranches() {
        return branchRepository.getBranchesList();
    }
}
