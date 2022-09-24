package org.example.service;

import org.example.enums.VehicleType;
import org.example.models.Branch;
import org.example.models.Slot;
import org.example.models.Vehicle;
import utils.CommonUtils;

import java.util.*;
import java.util.stream.Collectors;

import static utils.CommonUtils.isSlotAvailable;

public class VehicleManager {
    private final BranchManager branchManager;

    public VehicleManager(BranchManager branchManager) {
        this.branchManager = branchManager;
    }

    /***
     *
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
        if (Objects.isNull(vehicleType) || supportedVehicles.isEmpty()
                || !supportedVehicles.contains(vehicleType)) {
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
     *
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
        if (Objects.isNull(vehicleType) || supportedVehicles.isEmpty()
                || !supportedVehicles.contains(vehicleType)) {
            return -1;
        }

        List<Vehicle> vehicles = branch.getVehicles();
        Optional<Vehicle> optionalVehicle = vehicles.stream()
                .filter(vehicle -> vehicleType.equals(vehicle.getVehicleType()))
                .filter(vehicle -> isSlotAvailable(vehicle.getSlots(), startTime, endTime))
                .min(Comparator.comparing(Vehicle::getPrice));

        if (!optionalVehicle.isPresent()) {
            return -1;
        }

        Vehicle pickedVehicle = optionalVehicle.get();
        Slot slot = getNewSlot(startTime, endTime);
        pickedVehicle.getSlots().add(slot);

        return pickedVehicle.getPrice() * (endTime - startTime);
    }

    private Slot getNewSlot(Integer startTime, Integer endTime) {
        Slot slot = new Slot();
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setBooked(true);
        return slot;
    }


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
