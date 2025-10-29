package com.trainapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trainapp.DTO.ConnectionDTO;
import com.trainapp.services.ConnectionFinder;
import com.trainapp.utils.LocalTimeAdapter;
import spark.Spark.*;
import com.trainapp.model.Connection;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();

        // Enable basic CORS (frontend on different port)
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
        });

        get("/searchConnections", (req, res) -> {
            String departureCity = req.queryParams("departureCity");
            String arrivalCity = req.queryParams("arrivalCity");

            if (departureCity == null || arrivalCity == null) {
                res.status(400);
                return gson.toJson("Missing departureCity or arrivalCity");
            }
            ConnectionFinder findConnections = new ConnectionFinder();

            List<Connection> connections = findConnections.searchDirectConnections(departureCity, arrivalCity);

            if (connections.isEmpty()) {
                System.out.println("No direct connections found. Searching for indirect connections");
                connections = findConnections.searchIndirectConnections(departureCity, arrivalCity);
            }

            if (connections.isEmpty()) {
                System.out.println("No connections found.");
            }

            List<ConnectionDTO> connectionsDTO = connections.stream()
                    .map(c -> new ConnectionDTO(c, false)) // false = standard class
                    .toList();

            return gson.toJson(connectionsDTO);
        });








//        List<Connection> foundConnections = new ArrayList<>();
//        console mainConsole = new console();
//        mainConsole.startProgram();
//
//        Scanner scanner = new Scanner(System.in);
//        int choice;
//
//        do {
//            System.out.println("-------------------Welcome Connection Finder-------------------");
//            System.out.println("1. Search Connections");
//            System.out.println("2. Create a Trip");
//            System.out.println("3. View Trip");
//            System.out.println("4. Exit");
//            System.out.print("Enter your choice: ");
//
//            choice = scanner.nextInt();
//
//            switch (choice) {
//                case 1:
//                    List<Connection> connections = mainConsole.searchConnections();;
//                    //mainConsole.displayConnections(connections);
//                    break;
//                case 2:
//                    System.out.println("Creating the trip");
//                    break;
//                case 3:
//                    System.out.println("View trip");
//                case 4:
//                    System.out.println("Thank you for using connection finder, have a nice day");
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//
//            System.out.println(); // Adds a blank line for readability
//        } while (choice != 4);
//
//        scanner.close();


    }


}