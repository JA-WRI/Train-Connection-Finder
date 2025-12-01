package com.trainapp.repository;

import com.trainapp.model.Connection;
import com.trainapp.model.Route;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConnectionRepository implements Repository<Connection, Integer> {
    
    private RouteRepository routeRepository;
    
    public ConnectionRepository() {
        this.routeRepository = new RouteRepository();
    }

    @Override
    public Integer insert(java.sql.Connection conn, Connection entity) throws SQLException {
        return insertConnection(conn, entity);
    }

    @Override
    public Connection findById(java.sql.Connection conn, Integer id) throws SQLException {
        String sql = "SELECT connection_id, departure_city, arrival_city, departure_time, arrival_time, " +
                    "duration_minutes, number_of_routes, price FROM connections WHERE connection_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Connection connection = new Connection();
                    connection.setConnectionId(String.valueOf(rs.getInt("connection_id")));
                    connection.setDepartureCity(rs.getString("departure_city"));
                    connection.setArrivalCity(rs.getString("arrival_city"));
                    
                    Time departureTime = rs.getTime("departure_time");
                    if (departureTime != null) {
                        connection.setDepartureTime(departureTime.toLocalTime());
                    }
                    
                    Time arrivalTime = rs.getTime("arrival_time");
                    if (arrivalTime != null) {
                        connection.setArrivalTime(arrivalTime.toLocalTime());
                    }
                    
                    connection.setDuration((double) rs.getInt("duration_minutes"));
                    connection.setNumOfRoutes(rs.getInt("number_of_routes"));
                    connection.setFirstClassPrice(rs.getDouble("price"));
                    
                    return connection;
                }
            }
        }
        return null;
    }

    public int findOrInsertConnection(java.sql.Connection conn, Connection connection) throws SQLException {
        // Check if connection already exists
        String checkSql = "SELECT connection_id FROM connections WHERE " +
                "departure_city = ? AND arrival_city = ? AND " +
                "departure_time = ? AND arrival_time = ?";
        
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, connection.getDepartureCity().toLowerCase());
            checkStmt.setString(2, connection.getArrivalCity().toLowerCase());
            checkStmt.setTime(3, Time.valueOf(connection.getDepartureTime()));
            checkStmt.setTime(4, Time.valueOf(connection.getArrivalTime()));
            
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("connection_id");
                }
            }
        }
        // Insert new connection if not found
        return insertConnection(conn, connection);
    }

    private int insertConnection(java.sql.Connection conn, Connection connection) throws SQLException {
        String sql = "INSERT INTO connections (departure_city, arrival_city, departure_time, arrival_time, duration_minutes, number_of_routes, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, connection.getDepartureCity().toLowerCase());
            pstmt.setString(2, connection.getArrivalCity().toLowerCase());
            pstmt.setTime(3, Time.valueOf(connection.getDepartureTime()));
            pstmt.setTime(4, Time.valueOf(connection.getArrivalTime()));
            pstmt.setInt(5, connection.getDuration().intValue());
            pstmt.setInt(6, connection.getNumOfRoutes());
            pstmt.setDouble(7, connection.getFirstClassPrice());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int connectionId = rs.getInt(1);
                    // Insert connection routes if available
                    if (connection.getRoutes() != null) {
                        insertConnectionRoutes(conn, connectionId, connection.getRoutes());
                    }
                    return connectionId;
                }
            }
        }
        throw new SQLException("Failed to insert connection");
    }

    private void insertConnectionRoutes(java.sql.Connection conn, int connectionId, List<Route> routes) throws SQLException {
        String sql = "INSERT INTO connection_routes (connection_id, route_id, route_order) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < routes.size(); i++) {
                Route route = routes.get(i);
                String routeId = route.getRouteID();
                
                // If route doesn't have an ID, try to find it in the database
                if (routeId == null || routeId.isEmpty()) {
                    routeId = routeRepository.findRouteIdByAttributes(conn, route);
                }
                
                // Insert if we have a valid route ID
                if (routeId != null && !routeId.isEmpty()) {
                    pstmt.setInt(1, connectionId);
                    pstmt.setString(2, routeId);
                    pstmt.setInt(3, i + 1); // route_order starts from 1
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        }
    }
}

