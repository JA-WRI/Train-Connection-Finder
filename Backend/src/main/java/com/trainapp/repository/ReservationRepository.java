package com.trainapp.repository;

import java.sql.*;

public class ReservationRepository {
    

    public int insertReservation(java.sql.Connection conn, int tripId, String firstName, String lastName, int age, String travelerId) throws SQLException {
        String sql = "INSERT INTO reservations (trip_id, traveler_id, fname, lname, age) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, tripId);
            pstmt.setString(2, travelerId);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setInt(5, age);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert reservation");
    }
    

    public int insertReservation(java.sql.Connection conn, int tripId, String name, int age, String travelerId) throws SQLException {
        String[] nameParts = name.trim().split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : name;
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        return insertReservation(conn, tripId, firstName, lastName, age, travelerId);
    }

    public int getTripIdFromReservation(java.sql.Connection conn, int reservationId) throws SQLException {
        String sql = "SELECT trip_id FROM reservations WHERE reservation_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("trip_id");
                }
            }
        }
        return -1; // Not found
    }
}

