package org.example;

import org.example.service.DriverManager;
import org.example.service.VehicleManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {

        DriverManager driverManager = new DriverManager();
        driverManager.readInputFile("input.txt");
    }
}