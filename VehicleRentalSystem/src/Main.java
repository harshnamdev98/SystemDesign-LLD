import com.LLD.VehicleRentalSystem.VehicleRentalSystem;
import com.LLD.VehicleRentalSystem.constants.VehicleTypes;
import com.LLD.VehicleRentalSystem.models.Booking;
import com.LLD.VehicleRentalSystem.models.Branches;
import com.LLD.VehicleRentalSystem.models.Location;
import com.LLD.VehicleRentalSystem.models.User;
import com.LLD.VehicleRentalSystem.service.*;
import com.LLD.VehicleRentalSystem.strategy.BookingStrategy;
import com.LLD.VehicleRentalSystem.strategy.LowCostRentalStrategyImpl;

import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        System.out.println("Hello world!");



        // run the program and test
        // LLD is created

        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            User user = new User("user" + i);
            userList.add(user);
        }

        List<Location> locationList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Location location = new Location("address" + i, "city" + i, "state" + i, "ind", 1);
            locationList.add(location);
        }

        List<Branches> branchesList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Branches branches = new Branches("branch" + i, locationList.get(i-1));
            branchesList.add(branches);

        }

        VehicleService vehicleService = new VehicleService();
        UserService userService = new UserService();
        BranchService branchService = new BranchService(branchesList, vehicleService);
//        System.out.println(branchService.getAllBranches());

        BookingStrategy  bookingStrategy = new LowCostRentalStrategyImpl(branchService);
        BookingService bookingService = new BookingService(branchService, bookingStrategy, vehicleService, userService);
        VehicleRentalSystem vehicleRentalSystem = new VehicleRentalSystem(branchesList, userList, new LocationService(),
                branchService, vehicleService, bookingService, bookingStrategy);

        vehicleRentalSystem.addVehicle("v1", VehicleTypes.SUV, branchesList.get(0).getBranchName());
        System.out.println(vehicleService.getUnbookedVehicle().toString());

        vehicleRentalSystem.allocatePrice(branchesList.get(0).getBranchName(), VehicleTypes.SUV.toString(), 100);
        Date startDate = new Date(2023, 03, 26, 3, 00);
        Date endDate = new Date(2023, 03, 26, 6, 00);

        Booking booking = vehicleRentalSystem.bookAVehicle(VehicleTypes.SUV.toString(), startDate, endDate, userList.get(0).getUserName());
        System.out.println(booking.toString());

        System.out.println(vehicleService.getUnbookedVehicle().toString());

    }
}