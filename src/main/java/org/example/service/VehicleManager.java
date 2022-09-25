package org.example.service;

import org.example.enums.VehicleType;
import org.example.models.Branch;
import org.example.models.Slot;
import org.example.models.Vehicle;
import org.example.utils.CommonUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.utils.CommonUtils.isSlotAvailable;

public class VehicleManager {
    private final BranchManager branchManager;

    public VehicleManager(BranchManager branchManager) {
        this.branchManager = branchManager;
    }

    /***
     * Adds a vehicle to the given branch
     * @param branchId
     * @param vehicleTypeStr
     * @param vehicleId
     * @param price
     * @return
     */
    public boolean addVehicle(String branchId, String vehicleTypeStr, String vehicleId, Integer price) {
        Branch branch = branchManager.getBranch(branchId);
        if (Objects.isNull(branch)) {
            return false;
        }

        List<VehicleType> supportedVehicles = branch.getSupportedVehicles();
        VehicleType vehicleType = CommonUtils.parseVehicleType(vehicleTypeStr);
        //check if the selected vehicle exists in branch
        if (Objects.isNull(vehicleType) || supportedVehicles.isEmpty()
                || !supportedVehicles.contains(vehicleType)) {
            return false;
        }

        //check if vehicle with the given ID already exists
        boolean vehicleExists  = branch.getVehicles().stream()
                .anyMatch(vehicle -> vehicle.getVehicleId().equals(vehicleId));

        if(vehicleExists) {
            return false;
        }

        Vehicle vehicle = Vehicle.getInstance(vehicleType);
        vehicle.setVehicleId(vehicleId);
        vehicle.setVehicleType(vehicleType);
        vehicle.setPrice(price);

        branch.getVehicles().add(vehicle);
        return true;
    }

    /***
     * Books a vehicle for the given slot
     * @param branchId
     * @param vehicleTypeStr
     * @param startTime
     * @param endTime
     * @return
     */
    public Integer bookVehicle(String branchId, String vehicleTypeStr, Integer startTime, Integer endTime) {
        Branch branch = branchManager.getBranch(branchId);
        if (Objects.isNull(branch)) {
            return -1;
        }

        List<VehicleType> supportedVehicles = branch.getSupportedVehicles();
        VehicleType vehicleType = CommonUtils.parseVehicleType(vehicleTypeStr);
        //check if the selected vehicle exists in branch
        if (Objects.isNull(vehicleType) || supportedVehicles.isEmpty()
                || !supportedVehicles.contains(vehicleType)) {
            return -1;
        }

        List<Vehicle> vehicles = branch.getVehicles();
        //check if any vehicle exists in branch that has free slot for the given time range
        Optional<Vehicle> optionalVehicle = vehicles.stream()
                .filter(vehicle -> vehicleType.equals(vehicle.getVehicleType()))
                .filter(vehicle -> isSlotAvailable(vehicle.getSlots(), startTime, endTime))
                .min(Comparator.comparing(Vehicle::getPrice));

        //if there is no vehicle exists in the given time range slot
        if (!optionalVehicle.isPresent()) {
            return -1;
        }

        Vehicle pickedVehicle = optionalVehicle.get();
        Slot slot = getNewSlot(startTime, endTime);
        //book slot
        pickedVehicle.getSlots().add(slot);
        //return the total expenditure
        return pickedVehicle.getPrice() * (endTime - startTime);
    }

    private Slot getNewSlot(Integer startTime, Integer endTime) {
        Slot slot = new Slot();
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setBooked(true);
        return slot;
    }


    /***
     * Display vehicles sorted by price for the given branch
     * @param branchId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<String> displayVehicles(String branchId, int startTime, int endTime) {
        Branch branch = branchManager.getBranch(branchId);
        if (Objects.isNull(branch)) {
            return Collections.emptyList();
        }

        List<Vehicle> vehicles = branch.getVehicles();
        return vehicles.stream()
                .filter(vehicle -> isSlotAvailable(vehicle.getSlots(), startTime, endTime))
                .sorted(Comparator.comparing(Vehicle::getPrice))
                .map(Vehicle::getVehicleId)
                .collect(Collectors.toList());
    }
}
