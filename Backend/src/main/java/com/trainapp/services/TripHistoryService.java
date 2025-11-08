package com.trainapp.services;

import com.trainapp.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripHistoryService {

    public TripHistoryService() {}

    /**
     * Gets all trips for a user by last name and ID
     * Returns trips separated into current (today/future) and past
     */
    public Map<String, List<Map<String, Object>>> getUserTrips(String lastName, String userId) throws SQLException {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        List<Map<String, Object>> currentTrips = new ArrayList<>();
        List<Map<String, Object>> pastTrips = new ArrayList<>();

        try (java.sql.Connection conn = DatabaseConnection.getConnection()) {
            // Query to get all reservations for users matching last name and traveler_id
            // Join with tickets table to get connection details
            String sql = "SELECT DISTINCT " +
                    "r.reservation_id, " +
                    "r.trip_id, " +
                    "r.fname, " +
                    "r.lname, " +
                    "r.age, " +
                    "r.traveler_id, " +
                    "r.created_timestamp, " +
                    "t.ticket_id, " +
                    "t.departure_city, " +
                    "t.arrival_city, " +
                    "t.departure_time, " +
                    "t.arrival_time, " +
                    "t.duration, " +
                    "t.price, " +
                    "t.num_of_routes, " +
                    "c.connection_id " +
                    "FROM reservations r " +
                    "INNER JOIN tickets t ON r.reservation_id = t.reservation_id " +
                    "INNER JOIN trips tr ON r.trip_id = tr.trip_id " +
                    "INNER JOIN connections c ON tr.connection_id = c.connection_id " +
                    "WHERE r.lname = ? AND r.traveler_id = ? " +
                    "ORDER BY r.created_timestamp DESC";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, lastName.trim());
                pstmt.setString(2, userId.trim());

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> trip = new HashMap<>();
                        trip.put("reservationId", rs.getInt("reservation_id"));
                        trip.put("tripId", rs.getInt("trip_id"));
                        trip.put("ticketId", rs.getInt("ticket_id"));
                        trip.put("firstName", rs.getString("fname"));
                        trip.put("lastName", rs.getString("lname"));
                        trip.put("age", rs.getInt("age"));
                        trip.put("travelerId", rs.getString("traveler_id"));
                        trip.put("departureCity", rs.getString("departure_city"));
                        trip.put("arrivalCity", rs.getString("arrival_city"));
                        trip.put("departureTime", rs.getTime("departure_time").toLocalTime().toString());
                        trip.put("arrivalTime", rs.getTime("arrival_time").toLocalTime().toString());
                        trip.put("duration", rs.getString("duration"));
                        trip.put("price", rs.getDouble("price"));
                        trip.put("numOfRoutes", rs.getInt("num_of_routes"));
                        trip.put("connectionId", rs.getInt("connection_id"));
                        
                        // Get the reservation timestamp
                        Timestamp createdTimestamp = rs.getTimestamp("created_timestamp");
                        if (createdTimestamp != null) {
                            LocalDateTime reservationDateTime = createdTimestamp.toLocalDateTime();
                            LocalDate reservationDate = reservationDateTime.toLocalDate();
                            LocalDate today = LocalDate.now();
                            
                            // Compare reservation date with today
                            if (reservationDate.isBefore(today)) {
                                pastTrips.add(trip);
                            } else {
                                // Reservation created today or in the future - current trip
                                currentTrips.add(trip);
                            }
                        }
                    }
                }
            }
        }

        result.put("current", currentTrips);
        result.put("past", pastTrips);
        return result;
    }
}

