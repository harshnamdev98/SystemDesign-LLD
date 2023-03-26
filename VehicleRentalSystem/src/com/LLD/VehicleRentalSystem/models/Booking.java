package com.LLD.VehicleRentalSystem.models;

import com.LLD.VehicleRentalSystem.strategy.BookingStrategy;
import com.LLD.VehicleRentalSystem.strategy.CostCalculationStrategy;
import com.LLD.VehicleRentalSystem.strategy.HourlyCostCalculationStrategyImpl;
import com.LLD.VehicleRentalSystem.strategy.LowCostRentalStrategyImpl;

import java.util.Date;
import java.util.UUID;

public class Booking {
    @Override
    public String toString() {
        return bookingId;
    }
    private String bookingId;
    private User user;
    private Date startDate;
    private Date endDate;
    private Location location;
    private CostCalculationStrategy costCalculationStrategy;

    public Booking(User user, Date startDate, Date endDate, Location location) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingId = UUID.randomUUID().toString();
        this.location = location;
        this.costCalculationStrategy = new HourlyCostCalculationStrategyImpl();
    }

}
