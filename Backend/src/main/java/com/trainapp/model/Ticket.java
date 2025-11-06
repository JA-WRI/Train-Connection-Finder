package com.trainapp.model;

public class Ticket {
    private String ticketId;
    private String arrivalCity;
    private String departureCity;

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
}
