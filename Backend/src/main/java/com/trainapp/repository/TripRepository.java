package com.trainapp.repository;

import com.trainapp.model.Trip;
import java.sql.*;

public class TripRepository implements Repository<Trip, Integer> {

    @Override
    public Integer insert(java.sql.Connection conn, Trip entity) throws SQLException {
        // Note: Trip entity needs connection_id and booker_id to insert
        // This method would need additional context, so we keep the existing method
        throw new UnsupportedOperationException("Use insertTrip(conn, bookerId, connectionId) instead");
    }

    @Override
    public Trip findById(java.sql.Connection conn, Integer id) throws SQLException {
        String sql = "SELECT trip_id, booker_id, connection_id FROM trips WHERE trip_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Trip trip = new Trip(null, null); // Connection and User would need to be loaded separately
                    trip.setTripId(String.valueOf(rs.getInt("trip_id")));
                    return trip;
                }
            }
        }
        return null;
    }

    public int insertTrip(java.sql.Connection conn, int bookerId, int connectionId) throws SQLException {
        String sql = "INSERT INTO trips (booker_id, connection_id) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, bookerId);
            pstmt.setInt(2, connectionId);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert trip");
    }
}

