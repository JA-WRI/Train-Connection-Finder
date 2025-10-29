package com.trainapp.model;

public class Reservation {
    private String reservationId;
    private String travelerId;
    private String name;
    private String age;

    private Ticket ticket;

    public Reservation(String reservationId, String travelerId, String name, String age, Ticket ticket) {
        this.reservationId = reservationId;
        this.travelerId = travelerId;
        this.name = name;
        this.age = age;
        this.ticket = ticket;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getTravelerId() {
        return travelerId;
    }

    public void setTravelerId(String travelerId) {
        this.travelerId = travelerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
