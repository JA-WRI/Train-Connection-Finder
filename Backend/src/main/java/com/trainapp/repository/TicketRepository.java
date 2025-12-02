package com.trainapp.repository;

import com.trainapp.model.Connection;
import com.trainapp.model.Ticket;
import java.sql.*;


public class TicketRepository implements Repository<Ticket, Integer> {

    @Override
    public Integer insert(java.sql.Connection conn, Ticket entity) throws SQLException {
        String sql = "INSERT INTO tickets (reservation_id, user_id, departure_city, arrival_city, departure_time, arrival_time, num_of_routes, duration, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, Integer.parseInt(entity.getReservationId()));
            pstmt.setInt(2, Integer.parseInt(entity.getUserId()));
            pstmt.setString(3, entity.getDepartureCity().toLowerCase());
            pstmt.setString(4, entity.getArrivalCity().toLowerCase());
            pstmt.setTime(5, Time.valueOf(entity.getDepartureTime()));
            pstmt.setTime(6, Time.valueOf(entity.getArrivalTime()));
            pstmt.setInt(7, entity.getNumOfRoutes());
            pstmt.setString(8, entity.getDuration());
            pstmt.setDouble(9, entity.getPrice());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int ticketId = rs.getInt(1);
                    entity.setTicketId(String.valueOf(ticketId));
                    return ticketId;
                }
            }
        }
        throw new SQLException("Failed to insert ticket");
    }

    @Override
    public Ticket findById(java.sql.Connection conn, Integer id) throws SQLException {
        String sql = "SELECT ticket_id, departure_city, arrival_city FROM tickets WHERE ticket_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Ticket ticket = new Ticket(
                        rs.getString("arrival_city"),
                        rs.getString("departure_city")
                    );
                    ticket.setTicketId(String.valueOf(rs.getInt("ticket_id")));
                    return ticket;
                }
            }
        }
        return null;
    }
}

