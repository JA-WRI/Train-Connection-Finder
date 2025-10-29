package com.trainapp.model;

import java.util.ArrayList;
import java.util.List;

public class Trip {

    private String tripId;
    private Connection connection;
    private User user;
    private List<Reservation> reservations = new ArrayList<>();

    public Trip(String tripId, Connection connection, User user) {
        this.tripId = tripId;
        this.connection = connection;
        this.user = user;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Reservation> getReservations() {return reservations;}
    public void setReservations(List<Reservation> reservations) {this.reservations = reservations;}
}

