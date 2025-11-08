package com.trainapp.repository;

import com.trainapp.model.Route;
import java.sql.*;

public class RouteRepository {

    public String findRouteIdByAttributes(java.sql.Connection conn, Route route) throws SQLException {
        // If train_type is available, use it in the match
        if (route.getTrainType() != null && !route.getTrainType().isEmpty()) {
            String sql = "SELECT route_id FROM routes WHERE " +
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
            String sql = "SELECT route_id FROM routes WHERE " +
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
}

