package com.trainapp;

import com.trainapp.model.Connection;
import com.trainapp.services.ConnectionFinder;
import com.trainapp.utils.CSVReader;
import com.trainapp.utils.filterConnections;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class console {

    public console(){}

    public void startProgram(){
        System.out.println("Starting Railway System Database Setup...");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // This will create the database and load all routes from CSV
        CSVReader.loadRoutesFromCSVToDatabase("data/eu_rail_network.csv");
        System.out.println("Database will be at: " + new java.io.File("../railway_system.db").getAbsolutePath());

        System.out.println("Database setup complete YIPPEE YIPPEE");
    }

    public List<Connection> getConnection(String departureCity, String arrivalCity){
        ConnectionFinder findConnections = new ConnectionFinder();
        List<Connection> connections = findConnections.searchDirectConnections(departureCity, arrivalCity);

        if (connections.isEmpty()) {
            System.out.println("No direct connections found. Searching for indirect connections");
            connections = findConnections.searchIndirectConnections(departureCity, arrivalCity);
        }
        if (connections.isEmpty()) {
            System.out.println("No connections found.");
        }
        displayConnections(connections);
        return connections;
    }
//
//    public void createTrip(List<Connection> finalListOfConnections){
//        //ask user to pick one of the connections....ect.....
//        System.out.println("Creating trip");
//    }

//    private static boolean isValidDay(String day) {
//        String[] validDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
//        for (String validDay : validDays) {
//            if (validDay.equalsIgnoreCase(day)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
    public void displayConnections(List<Connection> connections){
        for (Connection connection : connections) {
            System.out.println(connection);
            System.out.println("-----");
        }
    }


}
