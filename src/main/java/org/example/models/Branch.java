package org.example.models;

import lombok.Data;
import org.example.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

@Data
public class Branch {
    private String id;
    private List<VehicleType> supportedVehicles;
    private List<Vehicle> vehicles = new ArrayList<>();

}
