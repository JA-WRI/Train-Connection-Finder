package com.trainapp.model;

import java.time.LocalTime;

public class Ticket {
    private String ticketId;
    private String reservationId;
    private String userId;
    private String arrivalCity;
    private String departureCity;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int numOfRoutes;
    private String duration;
    private double price;
    private Connection connection;

    public Ticket(Connection connection, Reservation reservation, User user) {
        this.connection = connection;
        this.reservationId = reservation.getReservationId();
        this.userId = user.getUserId();
        this.arrivalCity = connection.getArrivalCity();
        this.departureCity = connection.getDepartureCity();
        this.departureTime = connection.getDepartureTime();
        this.arrivalTime = connection.getArrivalTime();
        this.numOfRoutes = connection.getNumOfRoutes();
        this.duration = connection.convertIntoHour(connection.getDuration());
        this.price = connection.getFirstClassPrice(); // Default to first class
    }

    public Ticket(String arrivalCity, String departureCity) {
        this.arrivalCity = arrivalCity;
        this.departureCity = departureCity;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getNumOfRoutes() {
        return numOfRoutes;
    }

    public void setNumOfRoutes(int numOfRoutes) {
        this.numOfRoutes = numOfRoutes;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
