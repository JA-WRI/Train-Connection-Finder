package com.trainapp.services;

import com.trainapp.database.DatabaseConnection;
import com.trainapp.model.Connection;
import com.trainapp.repository.*;

import java.sql.*;

public class BookingService {

    private UserRepository userRepository;
    private ConnectionRepository connectionRepository;
    private TripRepository tripRepository;
    private ReservationRepository reservationRepository;
    private TicketRepository ticketRepository;

    public BookingService() {
        this.userRepository = new UserRepository();
        this.connectionRepository = new ConnectionRepository();
        this.tripRepository = new TripRepository();
        this.reservationRepository = new ReservationRepository();
        this.ticketRepository = new TicketRepository();
    }
    public int bookConnection(Connection connection, String userName, int userAge, String userId) throws SQLException {
        try (java.sql.Connection dbConnection = DatabaseConnection.getConnection()) {
            dbConnection.setAutoCommit(false); // Start transaction

            try {
                // 1. Insert booker user
                int userIdInt = userRepository.insertBooker(dbConnection, userName, userAge, userId);

                // 2. Find or insert connection
                int connectionId = connectionRepository.findOrInsertConnection(dbConnection, connection);

                // 3. Insert trip
                int tripId = tripRepository.insertTrip(dbConnection, userIdInt, connectionId);

                // 4. Insert reservation
                int reservationId = reservationRepository.insertReservation(dbConnection, tripId, userName, userAge, userId);

                // 5. Insert ticket
                ticketRepository.insertTicket(dbConnection, reservationId, userIdInt, connection);

                dbConnection.commit(); // Commit transaction
                return reservationId;

            } catch (SQLException e) {
                dbConnection.rollback(); // Rollback on error
                throw e;
            }
        }
    }
    public int addTraveler(int bookerReservationId, String travelerName, int travelerAge, String travelerId, Connection connection) throws SQLException {
        try (java.sql.Connection dbConnection = DatabaseConnection.getConnection()) {
            dbConnection.setAutoCommit(false); // Start transaction

            try {
                // 1. Get trip_id from the booker's reservation
                int tripId = reservationRepository.getTripIdFromReservation(dbConnection, bookerReservationId);
                if (tripId == -1) {
                    throw new SQLException("Trip not found for reservation ID: " + bookerReservationId);
                }

                // 2. Insert traveler as user (is_booker = 0)
                int travelerUserId = userRepository.insertTraveler(dbConnection, travelerName, travelerAge, travelerId);

                // 3. Insert reservation for traveler
                int travelerReservationId = reservationRepository.insertReservation(dbConnection, tripId, travelerName, travelerAge, travelerId);

                // 4. Insert ticket for traveler
                ticketRepository.insertTicket(dbConnection, travelerReservationId, travelerUserId, connection);

                dbConnection.commit();
                return travelerReservationId;

            } catch (SQLException e) {
                dbConnection.rollback();
                throw e;
            }
        }
    }
}

