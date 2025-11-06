package com.trainapp.model;

public class Reservation {
    private String reservationId;
    private String travelerId;
    private String fname;
    private String lname;
    private int age;

    private Ticket ticket;

    public Reservation(String travelerId, String fname,String lname, int age, Ticket ticket) {
        this.travelerId = travelerId;
        this.fname = fname;
        this.age = age;
        this.ticket = ticket;
        this.lname = lname;
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

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
}
