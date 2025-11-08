package com.trainapp.utils;

import com.trainapp.database.DatabaseConnection;
import com.trainapp.model.Connection;
import com.trainapp.model.Route;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConnectionFinder {

    public ConnectionFinder() {    }

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
        String sql = "SELECT * FROM routes WHERE departure_city = ?";

        try (java.sql.Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, city.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Route route = resultSetToRoute(rs);
                routes.add(route);
            }

        } catch (SQLException e) {
            System.out.println("Error getting routes from city: " + e.getMessage());
        }

        return routes;
    }

    // Convert database ResultSet to Route object
    private Route resultSetToRoute(ResultSet rs) throws SQLException {
        String daysStr = rs.getString("days_of_operation");
        List<String> daysOfOperation = parseDaysFromDB(daysStr);

        return new Route(
                rs.getString("route_id"),
                rs.getString("departure_city"),
                rs.getString("arrival_city"),
                rs.getTime("departure_time").toLocalTime(),
                rs.getTime("arrival_time").toLocalTime(),
                rs.getString("train_type"),
                daysOfOperation,
                rs.getDouble("first_class_price"),
                rs.getDouble("second_class_price"),
                rs.getInt("arrives_next_day") == 1
        );
    }

    // Parse days string from database (similar to your CSVReader.parseDays)
    private List<String> parseDaysFromDB(String daysStr) {
        List<String> daysList = new ArrayList<>();

        if (daysStr == null) {
            return daysList;
        }

        if (daysStr.equalsIgnoreCase("Daily")) {
            String[] allDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            Collections.addAll(daysList, allDays);
        } else if (daysStr.contains("-")) {
            parseDayRangeFromDB(daysStr, daysList);
        } else {
            String[] days = daysStr.split(",");
            for (String day : days) {
                daysList.add(day.trim());
            }
        }
        return daysList;
    }

    private void parseDayRangeFromDB(String range, List<String> daysList) {
        String[] parts = range.split("-");
        if (parts.length == 2) {
            String startDay = parts[0].trim();
            String endDay = parts[1].trim();

            String[] dayOrder = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

            int startIndex = -1;
            int endIndex = -1;

            for (int i = 0; i < dayOrder.length; i++) {
                if (dayOrder[i].equalsIgnoreCase(startDay)) {
                    startIndex = i;
                }
                if (dayOrder[i].equalsIgnoreCase(endDay)) {
                    endIndex = i;
                }
            }

            if (startIndex != -1 && endIndex != -1) {
                if (startIndex <= endIndex) {
                    daysList.addAll(Arrays.asList(dayOrder).subList(startIndex, endIndex + 1));
                } else {
                    daysList.addAll(Arrays.asList(dayOrder).subList(startIndex, dayOrder.length));
                    daysList.addAll(Arrays.asList(dayOrder).subList(0, endIndex + 1));
                }
            }
        }
    }

}

