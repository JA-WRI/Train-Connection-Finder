package com.trainapp.services;

import com.trainapp.database.DatabaseConnection;
import com.trainapp.repository.ReservationRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripHistoryService {

    private ReservationRepository reservationRepository;

    public TripHistoryService() {
        this.reservationRepository = new ReservationRepository();
    }

    public Map<String, List<Map<String, Object>>> getUserTrips(String lastName, String userId) throws SQLException {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        List<Map<String, Object>> currentTrips = new ArrayList<>();
        List<Map<String, Object>> pastTrips = new ArrayList<>();

        try (java.sql.Connection conn = DatabaseConnection.getConnection()) {
            // Use repository method instead of direct SQL
            List<Map<String, Object>> trips = reservationRepository.findTripsByLastNameAndTravelerId(conn, lastName, userId);

            for (Map<String, Object> trip : trips) {
                // Get the reservation timestamp
                Timestamp createdTimestamp = (Timestamp) trip.get("createdTimestamp");
                if (createdTimestamp != null) {
                    LocalDateTime reservationDateTime = createdTimestamp.toLocalDateTime();
                    LocalDate reservationDate = reservationDateTime.toLocalDate();
                    LocalDate today = LocalDate.now();
                    
                    // Compare reservation date with today
                    if (reservationDate.isBefore(today)) {
                        pastTrips.add(trip);
                    } else {
                        // Reservation created today or in the future - current trip
                        currentTrips.add(trip);
                    }
                }
            }
        }

        result.put("current", currentTrips);
        result.put("past", pastTrips);
        return result;
    }
}

