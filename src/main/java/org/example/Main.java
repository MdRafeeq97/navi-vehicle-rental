package org.example;

import org.example.service.DriverManager;
import org.example.service.VehicleManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        DriverManager driverManager = new DriverManager();
        driverManager.readInputFile("");
    }
}