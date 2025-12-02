package com.trainapp.utils;

import com.trainapp.database.DatabaseConnection;
import com.trainapp.model.Connection;
import com.trainapp.model.Route;
import com.trainapp.repository.RouteRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionFinder {

    private RouteRepository routeRepository;

    public ConnectionFinder() {
        this.routeRepository = new RouteRepository();
    }

    public List<Connection> searchIndirectConnections(String departureCity, String arrivalCity){
        List<Connection> foundConnections = new ArrayList<>();

        List<Route> routesFromDeparture = getRoutesFromCity(departureCity.toLowerCase());

        if (routesFromDeparture.isEmpty()) return foundConnections;

        for (Route firstLeg : routesFromDeparture) {
            String firstStop = firstLeg.getArrivalCity();

            List<Route> secondLegs = getRoutesFromCity(firstStop);
            if (secondLegs.isEmpty()) continue;

            for (Route secondLeg : secondLegs) {
                String secondStop = secondLeg.getArrivalCity();

                // 1-stop route
                if (secondStop.equalsIgnoreCase(arrivalCity)) {
                    Connection connection = new Connection(List.of(firstLeg, secondLeg));
                    foundConnections.add(connection);
                } else {
                    // Check for 2-stop route
                    List<Route> thirdLegs = getRoutesFromCity(secondStop);
                    if (thirdLegs.isEmpty()) continue;

                    for (Route thirdLeg : thirdLegs) {
                        if (thirdLeg.getArrivalCity().equalsIgnoreCase(arrivalCity)) {
                            Connection connection = new Connection(List.of(firstLeg, secondLeg, thirdLeg));
                            foundConnections.add(connection);
                        }
                    }
                }
            }
        }
        return foundConnections;

    }

    public List<Connection> searchDirectConnections(String departureCity, String arrivalCity){
        List<Connection> connections = new ArrayList<>();
        List<Route> routesDepartingFromCity = getRoutesFromCity(departureCity);

        if (!routesDepartingFromCity.isEmpty()) {
            // Get routes that depart from this city (directly from the map)
            for (Route route: routesDepartingFromCity){
                if(route.getArrivalCity().equalsIgnoreCase(arrivalCity)){
                    Connection connection = new Connection(List.of(route));
                    connections.add(connection);
                }
            }
        }
        return connections;
    }

    private List<Route> getRoutesFromCity(String city) {
        List<Route> routes = new ArrayList<>();

        try (java.sql.Connection conn = DatabaseConnection.getConnection()) {
            routes = routeRepository.findRoutesByDepartureCity(conn, city);
        } catch (SQLException e) {
            System.out.println("Error getting routes from city: " + e.getMessage());
        }

        return routes;
    }

}

