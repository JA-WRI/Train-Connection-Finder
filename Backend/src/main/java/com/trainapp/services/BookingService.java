package com.trainapp.services;

import com.trainapp.database.DatabaseConnection;
import com.trainapp.model.Connection;
import com.trainapp.model.Route;

import java.sql.*;
import java.util.List;

public class BookingService {

    public BookingService() {}

    /**
     * Books a connection for a user
     * @param connection The connection to book
     * @param userName Full name of the user
     * @param userAge Age of the user
     * @param userId Generic ID of the user
     * @return Reservation ID if successful
     * @throws SQLException if database operation fails
     */
    public int bookConnection(Connection connection, String userName, int userAge, String userId) throws SQLException {
        try (java.sql.Connection dbConnection = DatabaseConnection.getConnection()) {
            dbConnection.setAutoCommit(false); // Start transaction

            try {
                // 1. Insert or get user
                int userIdInt = insertOrGetUser(dbConnection, userName, userAge, userId);

                // 2. Insert or get connection in database
                int connectionId = insertOrGetConnection(dbConnection, connection);

                // 3. Insert trip
                int tripId = insertTrip(dbConnection, userIdInt, connectionId);

                // 4. Insert reservation (split name into first and last name)
                int reservationId = insertReservation(dbConnection, tripId, userName, userAge, userId);

                // 5. Insert ticket
                insertTicket(dbConnection, reservationId, userIdInt, connection);

                dbConnection.commit(); // Commit transaction
                return reservationId;

            } catch (SQLException e) {
                dbConnection.rollback(); // Rollback on error
                throw e;
            }
        }
    }

    private int insertOrGetUser(java.sql.Connection conn, String name, int age, String userId) throws SQLException {
        // Split name into first and last name
        String[] nameParts = name.trim().split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : name;
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // Insert new user - the person making the booking must have is_booker = 1
        String sql = "INSERT INTO users (first_name, last_name, age, is_booker) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, age);
            pstmt.setInt(4, 1); // is_booker = 1 (required: person doing the booking must be marked as booker)

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert user");
    }

    private int insertOrGetConnection(java.sql.Connection conn, Connection connection) throws SQLException {
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

        // Insert new connection
        String sql = "INSERT INTO connections (departure_city, arrival_city, departure_time, arrival_time, duration_minutes, number_of_routes, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, connection.getDepartureCity().toLowerCase());
            pstmt.setString(2, connection.getArrivalCity().toLowerCase());
            pstmt.setTime(3, Time.valueOf(connection.getDepartureTime()));
            pstmt.setTime(4, Time.valueOf(connection.getArrivalTime()));
            pstmt.setInt(5, connection.getDuration().intValue()); // duration in minutes
            pstmt.setInt(6, connection.getNumOfRoutes());
            pstmt.setDouble(7, connection.getFirstClassPrice()); // Using first class price as default

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
                
                // If route doesn't have an ID, try to find it in the database by matching attributes
                if (routeId == null || routeId.isEmpty()) {
                    routeId = findRouteIdByAttributes(conn, route);
                }
                
                // Insert if we have a valid route ID
                if (routeId != null && !routeId.isEmpty()) {
                    pstmt.setInt(1, connectionId);
                    pstmt.setString(2, routeId);
                    pstmt.setInt(3, i + 1); // route_order starts from 1 (RouteA = 1, RouteB = 2, etc.)
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        }
    }
    
    /**
     * Finds a route_id in the database by matching route attributes
     * Matches by departure_city, arrival_city, departure_time, and arrival_time
     * If train_type is available, it's also used for matching
     */
    private String findRouteIdByAttributes(java.sql.Connection conn, Route route) throws SQLException {
        // If train_type is available, use it in the match; otherwise match without it
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
            // Match without train_type (for routes created from DTOs that might not have train_type)
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
        return null; // Route not found in database
    }

    private int insertTrip(java.sql.Connection conn, int bookerId, int connectionId) throws SQLException {
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

    private int insertReservation(java.sql.Connection conn, int tripId, String name, int age, String travelerId) throws SQLException {
        // Split name into first and last name
        String[] nameParts = name.trim().split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : name;
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
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

    private void insertTicket(java.sql.Connection conn, int reservationId, int userId, Connection connection) throws SQLException {
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
            pstmt.setDouble(9, connection.getFirstClassPrice()); // Using first class price

            pstmt.executeUpdate();
        }
    }

    /**
     * Adds a traveler to an existing trip (identified by the booker's reservation_id)
     * @param bookerReservationId The reservation ID of the booker
     * @param travelerName Full name of the traveler
     * @param travelerAge Age of the traveler
     * @param travelerId Generic ID of the traveler
     * @param connection The connection for the ticket
     * @return Reservation ID of the new traveler reservation
     * @throws SQLException if database operation fails
     */
    public int addTraveler(int bookerReservationId, String travelerName, int travelerAge, String travelerId, Connection connection) throws SQLException {
        try (java.sql.Connection dbConnection = DatabaseConnection.getConnection()) {
            dbConnection.setAutoCommit(false); // Start transaction

            try {
                // 1. Get trip_id from the booker's reservation
                int tripId = getTripIdFromReservation(dbConnection, bookerReservationId);
                if (tripId == -1) {
                    throw new SQLException("Trip not found for reservation ID: " + bookerReservationId);
                }

                // 2. Insert traveler as user (is_booker = 0)
                int travelerUserId = insertTravelerUser(dbConnection, travelerName, travelerAge, travelerId);

                // 3. Insert reservation for traveler
                int travelerReservationId = insertReservation(dbConnection, tripId, travelerName, travelerAge, travelerId);

                // 4. Insert ticket for traveler
                insertTicket(dbConnection, travelerReservationId, travelerUserId, connection);

                dbConnection.commit(); // Commit transaction
                return travelerReservationId;

            } catch (SQLException e) {
                dbConnection.rollback(); // Rollback on error
                throw e;
            }
        }
    }

    /**
     * Gets trip_id from a reservation_id
     */
    private int getTripIdFromReservation(java.sql.Connection conn, int reservationId) throws SQLException {
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

    /**
     * Inserts a traveler user (is_booker = 0)
     */
    private int insertTravelerUser(java.sql.Connection conn, String name, int age, String userId) throws SQLException {
        // Split name into first and last name
        String[] nameParts = name.trim().split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : name;
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        String sql = "INSERT INTO users (first_name, last_name, age, is_booker) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, age);
            pstmt.setInt(4, 0); // is_booker = 0 for travelers (not the booker)

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert traveler user");
    }
}

