package org.example.models;

import lombok.Data;
import org.example.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Vehicle {
    private String vehicleId;
    private Integer price;
    private VehicleType vehicleType;
    private List<Slot> slots = new ArrayList<>();

    public static Vehicle getInstance(VehicleType vehicleType) {
        switch (vehicleType) {
            case CAR: return new Car();
            case VAN: return new Van();
            default:
                return new Bike();
        }
    }
}
