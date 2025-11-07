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

    public List<Connection> SelectedConnection(
            List<Connection> connections, // <-- input list to filter
            int maxPriceParam,
            int maxDurationParam,
            String ticketClass,
            String departureTimeParam,
            String arrivalTimeParam,
            String departureDay,
            String arrivalDay,
            String sortCriteria,
            String sortOrder) {

        filterConnections filter = new filterConnections();

        List<Connection> filtered = connections;

        if (departureDay != null && !departureDay.isEmpty()) {
            filtered = filter.filterByDayOfDeparture(filtered, departureDay);
        }

        if (arrivalDay != null && !arrivalDay.isEmpty()) {
            filtered = filter.filterByDayOfArrival(filtered, arrivalDay);
        }

        if (departureTimeParam != null && !departureTimeParam.isEmpty()) {
            filtered = filter.filterByDepartureTime(filtered, LocalTime.parse(departureTimeParam + ":00"));
        }

        if (arrivalTimeParam != null && !arrivalTimeParam.isEmpty()) {
            filtered = filter.filterByArrivalTime(filtered, LocalTime.parse(arrivalTimeParam + ":00"));
        }

        if (ticketClass != null && !ticketClass.isEmpty()) {
            if(ticketClass.equalsIgnoreCase("first-class") && maxPriceParam > 0){
                filtered = filter.filterPriceFirstClass(maxPriceParam,filtered);
            } else if (ticketClass.equalsIgnoreCase("second-class") && maxPriceParam != 0) {
                filtered = filter.filterPriceSecondClass(maxPriceParam,filtered);
            }

        }
        if(maxDurationParam > 0){
            filtered = filter.filterDuration(maxDurationParam, filtered);
        }

        // Apply sorting if provided
        if (sortCriteria != null && !sortCriteria.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
            switch (sortCriteria) {
                case "duration":
                    if ("asc".equalsIgnoreCase(sortOrder)) {
                        filtered = filter.AscenSortDuration(filtered);
                    } else {
                        filtered = filter.DescenSortDuration(filtered);
                    }
                    break;
                case "priceFirst":
                    if ("asc".equalsIgnoreCase(sortOrder)) {
                        filtered = filter.sortByFirstClassPriceAscending(filtered);
                    } else {
                        filtered = filter.sortByFirstClassPriceDescending(filtered);
                    }
                    break;
                case "priceSecond":
                    if ("asc".equalsIgnoreCase(sortOrder)) {
                        filtered = filter.sortBySecondClassPriceAscending(filtered);
                    } else {
                        filtered = filter.sortBySecondClassPriceDescending(filtered);
                    }
                    break;
                case "departureTime":
                    // only ascending defined; reverse for desc
                    filtered = filter.sortByDepartureTime(filtered);
                    if ("desc".equalsIgnoreCase(sortOrder)) {
                        java.util.Collections.reverse(filtered);
                    }
                    break;
                case "routesCount":
                    // only ascending defined; reverse for desc
                    filtered = filter.sortByNumRoutes(filtered);
                    if ("desc".equalsIgnoreCase(sortOrder)) {
                        java.util.Collections.reverse(filtered);
                    }
                    break;
                default:
                    // no-op for unknown criteria
                    break;
            }
        }

        return filtered;
    }




}
