package com.trainapp.repository;

import com.trainapp.model.Reservation;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationRepository implements Repository<Reservation, Integer> {

    @Override
    public Integer insert(java.sql.Connection conn, Reservation entity) throws SQLException {
        if (entity.getTripId() == null || entity.getTripId().isEmpty()) {
            throw new SQLException("Reservation must have a tripId to be inserted");
        }

        LocalDateTime timestamp = entity.getCreatedTimestamp();
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
            entity.setCreatedTimestamp(timestamp);
        }

        String sql = "INSERT INTO reservations (trip_id, traveler_id, fname, lname, age, created_timestamp) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, Integer.parseInt(entity.getTripId()));
            pstmt.setString(2, entity.getTravelerId());
            pstmt.setString(3, entity.getFname());
            pstmt.setString(4, entity.getLname());
            pstmt.setInt(5, entity.getAge());
            pstmt.setTimestamp(6, Timestamp.valueOf(timestamp));
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int reservationId = rs.getInt(1);
                    entity.setReservationId(String.valueOf(reservationId));
                    return reservationId;
                }
            }
        }
        throw new SQLException("Failed to insert reservation");
    }

    @Override
    public Reservation findById(java.sql.Connection conn, Integer id) throws SQLException {
        String sql = "SELECT reservation_id, trip_id, traveler_id, fname, lname, age, created_timestamp " +
                    "FROM reservations WHERE reservation_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Reservation reservation = new Reservation(
                        rs.getString("traveler_id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getInt("age")
                    );
                    reservation.setReservationId(String.valueOf(rs.getInt("reservation_id")));
                    reservation.setTripId(String.valueOf(rs.getInt("trip_id")));
                    Timestamp timestamp = rs.getTimestamp("created_timestamp");
                    if (timestamp != null) {
                        reservation.setCreatedTimestamp(timestamp.toLocalDateTime());
                    }
                    return reservation;
                }
            }
        }
        return null;
    }

    public List<Map<String, Object>> findTripsByLastNameAndTravelerId(java.sql.Connection conn, String lastName, String travelerId) throws SQLException {
        List<Map<String, Object>> trips = new ArrayList<>();
        
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
            pstmt.setString(2, travelerId.trim());

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
                    
                    Timestamp createdTimestamp = rs.getTimestamp("created_timestamp");
                    if (createdTimestamp != null) {
                        trip.put("createdTimestamp", createdTimestamp);
                    }
                    
                    trips.add(trip);
                }
            }
        }
        
        return trips;
    }
}

