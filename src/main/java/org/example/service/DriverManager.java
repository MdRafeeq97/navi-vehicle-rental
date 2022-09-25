package org.example.service;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class DriverManager {
    public static final String ADD_BRANCH = "ADD_BRANCH";
    public static final String ADD_VEHICLE = "ADD_VEHICLE";
    public static final String BOOK = "BOOK";
    public static final String DISPLAY_VEHICLES = "DISPLAY_VEHICLES";
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
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] input = line.split(" ");
            if(!validInput(input)) {
                break;
            }
            String command = input[0];
            String branchId = input[1];
            String vehicleType;
            String[] vehicles;
            switch (command) {
                case ADD_BRANCH:
                    vehicles = input[2].split(",");
                    boolean res = branchManager.addBranch(branchId, vehicles);
                    System.out.println(line+" "+res);
                    break;

                case ADD_VEHICLE:
                    vehicleType = input[2];
                    String vehicleId = input[3];
                    Integer price = Integer.parseInt(input[4]);
                    res = vehicleManager.addVehicle(branchId, vehicleType, vehicleId, price);
                    System.out.println(line+" "+res);
                    break;

                case BOOK:
                    vehicleType = input[2];
                    int startTime = Integer.parseInt(input[3]);
                    int endTime = Integer.parseInt(input[4]);
                    int totalPrice = vehicleManager.bookVehicle(branchId, vehicleType, startTime, endTime);
                    System.out.println(line+" "+totalPrice);
                    break;

                case DISPLAY_VEHICLES:
                    startTime = Integer.parseInt(input[2]);
                    endTime = Integer.parseInt(input[3]);
                    List<String> availableVehicles = vehicleManager.displayVehicles(branchId, startTime, endTime);
                    System.out.println(line+" "+String.join(",", availableVehicles));
                    break;

                default:
                    System.out.println("Invalid command+ "+ line);
            }
        }
    }

    private boolean validInput(String[] input) {
        if(input.length == 0) {
            System.out.println("No command Specified");
            return false;
        }
        String command = input[0];
        if(!(Objects.equals(command, ADD_BRANCH) || Objects.equals(command, ADD_VEHICLE)
                || Objects.equals(command, BOOK) || Objects.equals(command, DISPLAY_VEHICLES))) {
            System.out.println("Invalid command");
            return false;
        }

        if(Objects.equals(command, ADD_BRANCH) && input.length != 3) {
            System.out.println("Invalid input size");
            return false;
        }

        if(Objects.equals(command, ADD_VEHICLE)) {
            if(input.length != 5) {
                System.out.println("Invalid input size");
                return false;
            }

            try {
                Integer.parseInt(input[4]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price specified. Should be a number");
                return false;
            }
        }

        if(Objects.equals(command, BOOK)) {
            if(input.length != 5) {
                System.out.println("Invalid input size");
                return false;
            }

            try {
                Integer.parseInt(input[3]);
                Integer.parseInt(input[4]);

            } catch (NumberFormatException e) {
                System.out.println("Invalid start and end time specified. Should be a number");
                return false;
            }
        }

        if(Objects.equals(command, DISPLAY_VEHICLES)) {
            if(input.length != 4) {
                System.out.println("Invalid input size");
                return false;
            }

            try {
                Integer.parseInt(input[2]);
                Integer.parseInt(input[3]);

            } catch (NumberFormatException e) {
                System.out.println("Invalid start and end time specified. Should be a number");
                return false;
            }
        }

        return true;


    }
}
