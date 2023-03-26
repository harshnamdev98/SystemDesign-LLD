package com.LLD.VehicleRentalSystem.strategy;

import com.LLD.VehicleRentalSystem.constants.VehicleTypes;
import com.LLD.VehicleRentalSystem.models.Branches;
import com.LLD.VehicleRentalSystem.models.Vehicle;
import com.LLD.VehicleRentalSystem.service.BranchService;

import java.security.KeyPair;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class LowCostRentalStrategyImpl implements BookingStrategy{
    private BranchService branchService;

    public LowCostRentalStrategyImpl(BranchService branchService) {
        this.branchService = branchService;
    }

    /*
        search best vehicle:
            construct pq of all branches based on price and custom comparator, and get branch
            in branch service create a method to get unbooked vehicle of type given, but if not present again poll and repeat
         */
    class BranchPriceComparator implements Comparator<Branches> {
        private VehicleTypes vehicleTypes;

        public BranchPriceComparator(VehicleTypes vehicleTypes) {
            this.vehicleTypes = vehicleTypes;
        }

        public int compare (Branches a, Branches b) {
            if (a.getVehicleTypePriceMap().get(this.vehicleTypes) < b.getVehicleTypePriceMap().get(this.vehicleTypes)) {
                return 1;
            } else if (a.getVehicleTypePriceMap().get(this.vehicleTypes) > b.getVehicleTypePriceMap().get(this.vehicleTypes)) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    @Override
    public Vehicle searchBestVehicle(List<Vehicle> vehicleList, VehicleTypes vehicleTypes) {
        int minPrice = Integer.MAX_VALUE;
        Vehicle bestVehicle = null;
        for (Vehicle vehicle: vehicleList) {
            // we can implement PQ on all branch -> vehicle type price
            if (vehicle.getVehicleType() == vehicleTypes) {
                int branchPrice = branchService.getBranchByName(vehicle.getBranchName()).getVehicleTypePriceMap().get(vehicleTypes);
                if (minPrice > branchPrice) {
                    minPrice = branchPrice;
                    bestVehicle = vehicle;
                }
            }
        }

        return bestVehicle;


         /*
         implement this problem is how to keep branch, vehicle pair together in PQ
          */
                /*
                PriorityQueue<Branches> queue = new PriorityQueue<>(new BranchPriceComparator(vehicleTypes));
        for (Vehicle vehicle: vehicleList) {
            // we can implement PQ on all branch -> vehicle type price
            if (vehicle.getVehicleType() == vehicleTypes) {
                queue.add(
                    branchService.getBranchByName(vehicle.getBranchName())
                );
            }
        }

        branches =  queue.poll();

        return  null;
        *?
                 */
    }
}
