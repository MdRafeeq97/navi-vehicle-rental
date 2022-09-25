package org.example.utils;

import org.example.enums.VehicleType;
import org.example.models.Slot;

import java.util.List;

public class CommonUtils {
    public static VehicleType parseVehicleType(String vehicleType) {
        switch (vehicleType) {
            case "CAR": return VehicleType.CAR;
            case "BIKE" : return VehicleType.BIKE;
            case "VAN": return VehicleType.VAN;
            default: return null;
        }
    }

    public static boolean isSlotAvailable(List<Slot> slots, Integer startTime, Integer endTime) {
        return slots.stream()
                .noneMatch(slot -> startTime >= slot.getStartTime() && startTime < slot.getEndTime()
                        || endTime > slot.getStartTime() && endTime <= slot.getEndTime()
                        || startTime <= slot.getStartTime() && endTime >= slot.getEndTime()
                );
    }
}
