package com.trainapp.repository;

import com.trainapp.model.Connection;
import java.sql.*;

public class TicketRepository {

    public void insertTicket(java.sql.Connection conn, int reservationId, int userId, Connection connection) throws SQLException {
        String sql = "INSERT INTO tickets (reservation_id, user_id, departure_city, arrival_city, departure_time, arrival_time, num_of_routes, duration, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, connection.getDepartureCity().toLowerCase());
            pstmt.setString(4, connection.getArrivalCity().toLowerCase());
            pstmt.setTime(5, Time.valueOf(connection.getDepartureTime()));
            pstmt.setTime(6, Time.valueOf(connection.getArrivalTime()));
            pstmt.setInt(7, connection.getNumOfRoutes());
            pstmt.setString(8, connection.convertIntoHour(connection.getDuration()));
            pstmt.setDouble(9, connection.getFirstClassPrice());
            
            pstmt.executeUpdate();
        }
    }
}

