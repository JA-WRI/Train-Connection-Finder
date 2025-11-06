package com.trainapp;

import com.trainapp.DTO.UserDTO;
import com.trainapp.model.*;
import com.trainapp.services.*;
import com.trainapp.utils.CSVReader;
import com.trainapp.utils.filterConnections;
import org.eclipse.jetty.util.DateCache;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class console {

    private UserService userService;
    private TripService tripService;
    private TicketService ticketService;
    private ReservationService reservationService;
    private ConnectionService connectionService;

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


    public String bookTrip(Connection connection, UserDTO userDTO){
        //create user
        User user = userService.createUser(userDTO);

        //save connection in database
        connectionService.saveConnection(connection);

        //start creating reservations
        Reservation reservation = reservationService.createReservation(user);

        //create ticket
        Ticket ticket = ticketService.createTicket(connection);


        return "Yay you booked a trip";
    }

    public String addTravellers(List<UserDTO> travelers, Connection connection){ // might have to search for the connection
        for(UserDTO t: travelers){
            //not that all these travellers will have the isBooker set to false
            User traveler = userService.createUser(t);

            //create reservation
            Reservation reservation = reservationService.createReservation(traveler);

            //create ticket
            Ticket ticket = ticketService.createTicket(connection);

        }
        return "yay you added travellers";
    }


    public void displayConnections(List<Connection> connections){
        for (Connection connection : connections) {
            System.out.println(connection);
            System.out.println("-----");
        }
    }


}
