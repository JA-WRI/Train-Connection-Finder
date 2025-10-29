package com.trainapp.model;

import java.time.LocalTime;
import java.util.List;

public class Route {

    // Attributes
    private String routeID;
    private String departureCity;
    private String arrivalCity;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String trainType;
    private List<String> daysOfOperation;
    private double firstClassTicketRate;
    private double secondClassTicketRate;
    private boolean arriveNextDay;

    // Constructor
    public Route(String routeID, String departureCity, String arrivalCity, LocalTime departureTime, LocalTime arrivalTime, String trainType, List<String> daysOfOperation, double firstClassTicketRate, double secondClassTicketRate, boolean arriveNextDay) {
        this.routeID = routeID;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trainType = trainType;
        this.daysOfOperation = daysOfOperation;
        this.firstClassTicketRate = firstClassTicketRate;
        this.secondClassTicketRate = secondClassTicketRate;
        this.arriveNextDay = arriveNextDay;
    }

    // Getters
    public String getRouteID() {return routeID;}
    public double getSecondClassTicketRate() {return secondClassTicketRate;}
    public String getDepartureCity() {return departureCity;}
    public double getFirstClassTicketRate() {return firstClassTicketRate;}
    public LocalTime getDepartureTime() {return departureTime;}
    public LocalTime getArrivalTime() {return arrivalTime;}
    public List<String> getDaysOfOperation() { return daysOfOperation; }
    public String getTrainType() {return trainType;}
    public String getArrivalCity() {return arrivalCity;}
    public boolean getArriveNextDay() {return arriveNextDay;}

    // Setters
    public void setRouteID(String routeID) {this.routeID = routeID;}
    public void setDepartureCity(String departureCity) {this.departureCity = departureCity;}
    public void setArrivalCity(String arrivalCity) {this.arrivalCity = arrivalCity;}
    public void setTrainType(String trainType) {this.trainType = trainType;}
    public void setSecondClassTicketRate(double secondClassTicketRate) {this.secondClassTicketRate = secondClassTicketRate;}
    public void setFirstClassTicketRate(double firstClassTicketRate) {this.firstClassTicketRate = firstClassTicketRate;}
    public void setDepartureTime(LocalTime departureTime) {this.departureTime = departureTime;}
    public void setDaysOfOperation(List<String> daysOfOperation) {this.daysOfOperation = daysOfOperation;}
    public void setArrivalTime(LocalTime arrivalTime) {this.arrivalTime = arrivalTime;}
    public void setArriveNextDay(boolean arriveNextDay) {this.arriveNextDay = arriveNextDay;}

    @Override
//    public String toString() {
//        String arrivalDisplay = arriveNextDay ?
//                arrivalTime + " (+1d)" :
//                arrivalTime.toString();
//
//        return String.format("model.Route %s: %s to %s | %s - %s | Train: %s | Days: %s | 1st: €%.2f | 2nd: €%.2f",
//                routeID, departureCity, arrivalCity, departureTime, arrivalDisplay,
//                trainType, daysOfOperation, firstClassTicketRate, secondClassTicketRate);
//    }
    public String toString() {
        String arrivalDisplay = arriveNextDay ?
                arrivalTime + " (+1d)" :
                arrivalTime.toString();
        return String.format("model.Route %s: %s to %s | %s - %s | Days: %s |",
                routeID, departureCity, arrivalCity, departureTime, arrivalDisplay,
                daysOfOperation);
    }



}