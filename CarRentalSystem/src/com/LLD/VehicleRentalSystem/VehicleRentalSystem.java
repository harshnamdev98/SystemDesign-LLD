package com.LLD.VehicleRentalSystem;

import com.LLD.VehicleRentalSystem.constants.VehicleTypes;
import com.LLD.VehicleRentalSystem.exceptions.BranchNotFoundException;
import com.LLD.VehicleRentalSystem.models.*;
import com.LLD.VehicleRentalSystem.service.BookingService;
import com.LLD.VehicleRentalSystem.service.BranchService;
import com.LLD.VehicleRentalSystem.service.LocationService;
import com.LLD.VehicleRentalSystem.service.VehicleService;
import com.LLD.VehicleRentalSystem.strategy.BookingStrategy;
import com.LLD.VehicleRentalSystem.strategy.LowCostRentalStrategyImpl;

import java.awt.print.Book;
import java.util.Date;
import java.util.List;

public class VehicleRentalSystem {
    private List<Branches> branchesList;
    private List<User> userList;

    private LocationService locationService;
    private BranchService branchService;
    private VehicleService vehicleService;
    private BookingService bookingService;
    private BookingStrategy bookingStrategy;

    public VehicleRentalSystem(List<Branches> branchesList, List<User> userList, LocationService locationService, BranchService branchService, VehicleService vehicleService, BookingService bookingService, BookingStrategy bookingStrategy) {
        this.branchesList = branchesList;
        this.userList = userList;
        this.locationService = locationService;
        this.branchService = branchService;
        this.vehicleService = vehicleService;
        this.bookingService = bookingService;
        this.bookingStrategy = bookingStrategy;
    }

    public VehicleRentalSystem(List<Branches> branchesList, List<User> userList) {
        this.branchesList = branchesList;
        this.userList = userList;
    }

    public Branches addBranch(String branchName, String city){
        Location location = locationService.getLocationByCity(city);
        Branches branch = branchService.createAndAddBranch(branchName, location);
        return branch;
    }

    public void allocatePrice(String branchName, String vehicleType, int price) throws BranchNotFoundException {
        branchService.updateVehicleTypePrice(branchName, vehicleType, price);
    }

    public Vehicle addVehicle(String vehicleNo, VehicleTypes vehicleTypes, String branchName) {
        return branchService.addVehicleToBranch(vehicleNo, vehicleTypes, branchName);
    }

    public Booking bookAVehicle(String vehicleType, Date startDateTime, Date endDateTime, String userName) {
        return bookingService.bookAVehicle(vehicleType, startDateTime, endDateTime, bookingStrategy, userName);
    }

}
