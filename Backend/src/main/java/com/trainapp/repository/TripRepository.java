package com.trainapp.repository;

import java.sql.*;

public class TripRepository {

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

