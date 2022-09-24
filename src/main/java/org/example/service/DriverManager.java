package org.example.service;

import java.io.*;
import java.util.List;

public class DriverManager {
    private BranchManager branchManager;
    private VehicleManager vehicleManager;

    public DriverManager(BranchManager branchManager, VehicleManager vehicleManager) {
        this.branchManager = branchManager;
        this.vehicleManager = vehicleManager;
    }

    public DriverManager() {
        this.branchManager = new BranchManager();
        this.vehicleManager = new VehicleManager(this.branchManager);
    }

    public void readInputFile(String filePath) throws IOException {
        File file = new File("/home/rafeeq/vehicles.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s;
        while ((s = br.readLine()) != null) {
            String[] input = s.split(" ");
            String branchId = input[1];
            String vehicleType;
            String[] vehicles;
            switch (input[0]) {
                case "ADD_BRANCH" :
                    branchId = input[1];
                    vehicles = input[2].split(",");
                    boolean res = branchManager.addBranch(branchId, vehicles);
                    System.out.println(s+" "+res);
                    break;

                case "ADD_VEHICLE":
                    branchId = input[1];
                    vehicleType = input[2];
                    String vehicleId = input[3];
                    Integer price = Integer.parseInt(input[4]);
                    res = vehicleManager.addVehicle(branchId, vehicleType, vehicleId, price);
                    System.out.println(s+" "+res);
                    break;

                case "BOOK":
                    branchId = input[1];
                    vehicleType = input[2];
                    int startTime = Integer.parseInt(input[3]);
                    int endTime = Integer.parseInt(input[4]);
                    int totalPrice = vehicleManager.bookVehicle(branchId, vehicleType, startTime, endTime);
                    System.out.println(s+" "+totalPrice);
                    break;

                case "DISPLAY_VEHICLES":
                    branchId = input[1];
                    startTime = Integer.parseInt(input[2]);
                    endTime = Integer.parseInt(input[3]);
                    List<String> availableVehicles = vehicleManager.displayVehicles(branchId, startTime, endTime);
                    System.out.println(s+" "+String.join(",", availableVehicles));
                    break;
            }
        }
    }
}
