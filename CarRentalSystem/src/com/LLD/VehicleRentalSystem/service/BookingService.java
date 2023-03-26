package com.LLD.VehicleRentalSystem.service;

import com.LLD.VehicleRentalSystem.constants.VehicleTypes;
import com.LLD.VehicleRentalSystem.models.*;
import com.LLD.VehicleRentalSystem.repository.BookingRepository;
import com.LLD.VehicleRentalSystem.strategy.BookingStrategy;

import java.util.Date;
import java.util.List;

public class BookingService {
    private BookingRepository bookingRepository=new BookingRepository();
    private BranchService branchService;
    private BookingStrategy bookingStrategy;
    private VehicleService vehicleService;
    private UserService userService;

    public BookingService(BranchService branchService, BookingStrategy bookingStrategy, VehicleService vehicleService, UserService userService) {
        this.branchService = branchService;
        this.bookingStrategy = bookingStrategy;
        this.vehicleService = vehicleService;
        this.userService = userService;
    }

    public Booking createBooking(Vehicle vehicle, User user, Date startDateTime, Date endDateTime, Location location) {
        Booking booking = new Booking(user, startDateTime, endDateTime, location);
        vehicle.setBooked(Boolean.TRUE);
        // book vehicle start and end date time
        return  booking;
    }
    public Booking bookAVehicle(String vehicleType, Date startDateTime, Date endDateTime, BookingStrategy bookingStrategy, String userName) {
        // get all unbooked vehicle from stations
        List<Vehicle> unbookedVehicles = vehicleService.getUnbookedVehicle();
        Vehicle vehicle = bookingStrategy.searchBestVehicle(unbookedVehicles, VehicleTypes.valueOf(vehicleType));
        Location location = branchService.getBranchByName(vehicle.getBranchName()).getLocation();
        User user = new User(userName);
        Booking booking = this.createBooking(vehicle, user, startDateTime, endDateTime, location);
        return  booking;
    }
}
