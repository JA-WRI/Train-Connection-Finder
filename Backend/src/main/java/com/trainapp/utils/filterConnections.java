package com.trainapp.utils;

import com.trainapp.model.Connection;
import com.trainapp.model.Route;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class filterConnections {
    //Sort connections from low to high duration
    public List<Connection> AscenSortDuration(List<Connection> connections){
        connections.sort(Comparator.comparingDouble(Connection::getDuration));
        return connections;
    }
    //Sort connections from high to low duration
    public List<Connection> DescenSortDuration(List<Connection> connections){
        connections.sort(Comparator.comparingDouble(Connection::getDuration).reversed());
        return connections;
    }
    //Filtering the connections based on a given max duration
    public List<Connection> filterDuration(int givenDuration, List<Connection> connections) {
        List<Connection> filteredConnection = new ArrayList<>();

        for (Connection connection : connections) {
            String durationStr = connection.getDuration().toString(); // e.g. "6h52m"

            // Extract only the number before 'h'
            int hours = 0;
            if (durationStr.contains("h")) {
                try {
                    hours = Integer.parseInt(durationStr.substring(0, durationStr.indexOf("h")));
                } catch (NumberFormatException e) {
                    // Ignore invalid format
                    continue;
                }
            }

            // Compare only by hours
            if (hours <= givenDuration) {
                filteredConnection.add(connection);
            }
        }

        return filteredConnection;
    }


    public List<Connection> sortByFirstClassPriceAscending(List<Connection> connections){
        connections.sort(Comparator.comparing(Connection::getFirstClassPrice));
        return connections;
    }

    public List<Connection> sortByFirstClassPriceDescending(List<Connection> connections){
        connections.sort(Comparator.comparing(Connection::getFirstClassPrice).reversed());
        return connections;
    }

    public List<Connection> sortBySecondClassPriceAscending(List<Connection> connections){
        connections.sort(Comparator.comparing(Connection::getSecondClassPrice));
        return connections;
    }

    public List<Connection> sortBySecondClassPriceDescending(List<Connection> connections){
        connections.sort(Comparator.comparing(Connection::getSecondClassPrice).reversed());
        return connections;
    }

    public List<Connection> sortByDepartureTime(List<Connection> connections) {
        connections.sort(Comparator.comparing(Connection::getDepartureTime));
        return connections;
    }

    public List<Connection> sortByNumRoutes(List<Connection> connections) {
        connections.sort(Comparator.comparing(Connection::getNumOfRoutes));
        return connections;
    }

    //Filtering the connections based on a given max price for first class
    public List<Connection> filterPriceFirstClass(int givenPrice,List<Connection> connections) {
        double tripPrice = 0.0;
        List<Connection> filteredConnection = new ArrayList<>();
        for (Connection connection : connections) {
            tripPrice = connection.getFirstClassPrice();
            if (tripPrice <= givenPrice) {
                filteredConnection.add(connection);
            }
        }
        return filteredConnection;
    }
    //Filtering the connections based on a given max price for second class
    public List<Connection> filterPriceSecondClass(int givenPrice,List<Connection> connections) {
        double tripPrice = 0.0;
        List<Connection> filteredConnection = new ArrayList<>();
        for (Connection connection : connections) {
            tripPrice = connection.getSecondClassPrice();
            if (tripPrice <= givenPrice) {
                filteredConnection.add(connection);
            }
        }
        return filteredConnection;
    }

    public List<Connection> filterByDayOfDeparture(List<Connection> connections, String day){
        List<Connection> filteredConnections = new ArrayList<>();
        for (Connection connection : connections){
            Route firstRoute = connection.getRoutes().get(0);

            if (firstRoute.getDaysOfOperation().contains(day)) {
                filteredConnections.add(connection);
            }
        }
        return filteredConnections;
    }

    public List<Connection> filterByDayOfArrival(List<Connection> connections, String day){
        List<Connection> filteredConnections = new ArrayList<>();
        for (Connection connection : connections){
            List<Route> routes = connection.getRoutes();
            Route firstRoute = routes.get(routes.size() - 1);

            if (firstRoute.getDaysOfOperation().contains(day)) {
                filteredConnections.add(connection);
            }
        }
        return filteredConnections;
    };

    public List<Connection> filterByDepartureTime(List<Connection> connections, LocalTime departureTime) {
        List<Connection> filteredConnections = new ArrayList<>();
        for (Connection connection : connections) {
            // Get the departure time of the connection (first route's departure time)
            LocalTime connectionDepartureTime = connection.getDepartureTime();

            // Filter connections that depart at or after the specified time
            if (!connectionDepartureTime.isBefore(departureTime)) {
                filteredConnections.add(connection);
            }
        }
        return filteredConnections;
    }

    public List<Connection> filterByArrivalTime(List<Connection> connections, LocalTime arrivalTime) {
        List<Connection> filteredConnections = new ArrayList<>();
        for (Connection connection : connections) {
            // Get the arrival time of the connection (last route's arrival time)
            LocalTime connectionArrivalTime = connection.getArrivalTime();

            // Filter connections that arrive at or before the specified time
            if (!connectionArrivalTime.isAfter(arrivalTime)) {
                filteredConnections.add(connection);
            }
        }
        return filteredConnections;
    }


}
