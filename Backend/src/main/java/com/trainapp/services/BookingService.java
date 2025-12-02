package com.trainapp.services;

import com.trainapp.database.DatabaseConnection;
import com.trainapp.model.Connection;
import com.trainapp.model.Reservation;
import com.trainapp.model.Ticket;
import com.trainapp.model.Trip;
import com.trainapp.model.User;
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
                // 1. Find or insert connection to get connectionId
                int connectionId = connectionRepository.findOrInsertConnection(dbConnection, connection);
                connection.setConnectionId(String.valueOf(connectionId));

                // 2. Create User domain object (booker)
                String[] nameParts = parseName(userName);
                User booker = new User(nameParts[1], nameParts[0], userAge, true);

                // 3. Save User to database and get generated userId
                Integer userIdInt = userRepository.insert(dbConnection, booker);
                booker.setUserId(String.valueOf(userIdInt));

                // 4. Create Trip domain object
                Trip trip = new Trip(connection, booker);

                // 5. Save Trip to database and get generated tripId
                Integer tripId = tripRepository.insertTrip(dbConnection, userIdInt, connectionId);
                trip.setTripId(String.valueOf(tripId));
                booker.setTrip(trip);

                // 6. Create Reservation domain object
                Reservation reservation = new Reservation(userId, nameParts[0], nameParts[1], userAge);
                reservation.setTripId(String.valueOf(tripId));

                // 7. Save Reservation to database and get generated reservationId
                Integer reservationId = reservationRepository.insert(dbConnection, reservation);
                reservation.setReservationId(String.valueOf(reservationId));

                // 8. Create Ticket domain object
                Ticket ticket = new Ticket(connection, reservation, booker);

                // 9. Save Ticket to database
                ticketRepository.insert(dbConnection, ticket);
                reservation.setTicket(ticket);
                trip.getReservations().add(reservation);

                dbConnection.commit(); // Commit transaction
                return reservationId;

            } catch (SQLException e) {
                dbConnection.rollback(); // Rollback on error
                throw e;
            }
        }
    }

    private String[] parseName(String name) {
        String[] nameParts = name.trim().split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : name;
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        return new String[]{firstName, lastName};
    }
    public int addTraveler(int bookerReservationId, String travelerName, int travelerAge, String travelerId, Connection connection) throws SQLException {
        try (java.sql.Connection dbConnection = DatabaseConnection.getConnection()) {
            dbConnection.setAutoCommit(false); // Start transaction

            try {
                // 1. Get trip_id from the booker's reservation
                Reservation bookerReservation = reservationRepository.findById(dbConnection, bookerReservationId);
                if (bookerReservation == null || bookerReservation.getTripId() == null) {
                    throw new SQLException("Trip not found for reservation ID: " + bookerReservationId);
                }
                String tripId = bookerReservation.getTripId();

                // 2. Create Traveler User domain object
                String[] nameParts = parseName(travelerName);
                User traveler = new User(nameParts[1], nameParts[0], travelerAge, false);

                // 3. Save Traveler User to database and get generated userId
                Integer travelerUserIdInt = userRepository.insert(dbConnection, traveler);
                traveler.setUserId(String.valueOf(travelerUserIdInt));

                // 4. Create Reservation domain object for traveler
                Reservation travelerReservation = new Reservation(travelerId, nameParts[0], nameParts[1], travelerAge);
                travelerReservation.setTripId(tripId);

                // 5. Save Reservation to database and get generated reservationId
                Integer travelerReservationId = reservationRepository.insert(dbConnection, travelerReservation);
                travelerReservation.setReservationId(String.valueOf(travelerReservationId));

                // 6. Create Ticket domain object for traveler
                Ticket ticket = new Ticket(connection, travelerReservation, traveler);

                // 7. Save Ticket to database
                ticketRepository.insert(dbConnection, ticket);
                travelerReservation.setTicket(ticket);

                dbConnection.commit();
                return travelerReservationId;

            } catch (SQLException e) {
                dbConnection.rollback();
                throw e;
            }
        }
    }
}

