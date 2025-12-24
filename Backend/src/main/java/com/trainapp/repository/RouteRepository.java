package com.trainapp.repository;

import com.trainapp.model.Route;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RouteRepository implements Repository<Route, String> {

    @Override
    public String insert(java.sql.Connection conn, Route entity) throws SQLException {
        // Routes are typically pre-populated from CSV, so insertion is not commonly needed
        throw new UnsupportedOperationException("Route insertion not supported. Routes are loaded from CSV.");
    }

    @Override
    public Route findById(java.sql.Connection conn, String id) throws SQLException {
        String sql = "SELECT route_id, departure_city, arrival_city, departure_time, arrival_time, " +
                    "train_type, days_of_operation, first_class_price, second_class_price, arrives_next_day " +
                    "FROM Routes WHERE route_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return convertResultToRoute(rs);
                }
            }
        }
        return null;
    }

    public String findRouteIdByAttributes(java.sql.Connection conn, Route route) throws SQLException {
        // If train_type is available, use it in the match
        if (route.getTrainType() != null && !route.getTrainType().isEmpty()) {
            String sql = "SELECT route_id FROM Routes WHERE " +
                    "departure_city = ? AND arrival_city = ? AND " +
                    "departure_time = ? AND arrival_time = ? AND " +
                    "train_type = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, route.getDepartureCity().toLowerCase());
                pstmt.setString(2, route.getArrivalCity().toLowerCase());
                pstmt.setTime(3, Time.valueOf(route.getDepartureTime()));
                pstmt.setTime(4, Time.valueOf(route.getArrivalTime()));
                pstmt.setString(5, route.getTrainType());
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("route_id");
                    }
                }
            }
        } else {
            // Match without train_type
            String sql = "SELECT route_id FROM Routes WHERE " +
                    "departure_city = ? AND arrival_city = ? AND " +
                    "departure_time = ? AND arrival_time = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, route.getDepartureCity().toLowerCase());
                pstmt.setString(2, route.getArrivalCity().toLowerCase());
                pstmt.setTime(3, Time.valueOf(route.getDepartureTime()));
                pstmt.setTime(4, Time.valueOf(route.getArrivalTime()));
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("route_id");
                    }
                }
            }
        }
        return null; // Route not found
    }

    public List<Route> findRoutesByDepartureCity(java.sql.Connection conn, String departureCity) throws SQLException {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT route_id, departure_city, arrival_city, departure_time, arrival_time, " +
                    "train_type, days_of_operation, first_class_price, second_class_price, arrives_next_day " +
                    "FROM Routes WHERE departure_city = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, departureCity.toLowerCase());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Route route = convertResultToRoute(rs);
                    routes.add(route);
                }
            }
        }
        
        return routes;
    }

    private Route convertResultToRoute(ResultSet rs) throws SQLException {
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

