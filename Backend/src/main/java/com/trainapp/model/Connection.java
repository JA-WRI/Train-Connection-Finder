package com.trainapp.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Connection {
    LocalTime departureTime;
    LocalTime arrivalTime;
    Double duration;
    Double firstClassPrice;
    Double secondClassPrice;
    String departureCity;
    String arrivalCity;
    int numOfRoutes;
    List<Route> routes;

    public Connection(){

    }

    public Connection(List<Route> routes) {
        this.routes = new ArrayList<>(routes);
        this.numOfRoutes = routes.size();

        if (!routes.isEmpty()) {
            this.departureTime = routes.get(0).getDepartureTime();
            this.arrivalTime = routes.get(routes.size()-1).getArrivalTime();
            this.arrivalTime = routes.get(routes.size() - 1).getArrivalTime();
            this.departureCity = routes.get(0).getDepartureCity();
            this.arrivalCity = routes.get(routes.size()-1).getArrivalCity();
            this.firstClassPrice = calculateFirstClassTicketPrice();
            this.secondClassPrice = calculateSecondClassTicketPrice();
            this.duration = calculateTripDuration();
        }

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

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getFirstClassPrice() {
        return firstClassPrice;
    }

    public void setFirstClassPrice(Double firstClassPrice) {
        this.firstClassPrice = firstClassPrice;
    }

    public Double getSecondClassPrice() {
        return secondClassPrice;
    }

    public void setSecondClassPrice(Double secondClassPrice) {
        this.secondClassPrice = secondClassPrice;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public int getNumOfRoutes() {
        return numOfRoutes;
    }

    public void setNumOfRoutes(int numOdRoutes) {
        this.numOfRoutes = numOdRoutes;
    }

    public List<Route> getRoutes() { return routes; }

    public void setRoutes(List<Route> routes) { this.routes = routes; }

    // Method to calculate the total trip duration (in minutes)
    private Double calculateTripDuration(){
        double totalDuration = 0;

        for (Route route : routes) {
            double routeDuration = calculateRouteDuration(route);
            totalDuration += routeDuration;
        }

        return totalDuration;
    }

    public String convertIntoHour(double duration) {
        String durationHour = "";
        int hours = (int) (duration / 60);
        int minutes = (int) (duration % 60);

        return durationHour = hours + "h" + minutes + "m";
    }

    // Method to calculate the duration of one route (in minutes)
    private Double calculateRouteDuration(Route route) {
        // Converting the departure time from hour to minute
        double departureTime = (double) route.getDepartureTime().toSecondOfDay() / 60;

        // Converting the arrival time from hour to minute
        double arrivalTime = (double) route.getArrivalTime().toSecondOfDay() / 60;
        if (route.getArriveNextDay() || arrivalTime < departureTime) {
            arrivalTime += (24 * 60); // Taking into consideration if the arrival time is for the next day, the (+1d) in CSV
        }

        return arrivalTime - departureTime;
    }
    private double calculateFirstClassTicketPrice() {
        double total = 0.0;
        for(Route route: routes){
            total += route.getFirstClassTicketRate();
        }
        return total;
    }

    private double calculateSecondClassTicketPrice() {
        double total = 0.0;
        for(Route route: routes){
            total += route.getSecondClassTicketRate();
        }
        return total;
    }

    public String toString(){
        String connections = "Duration: " + convertIntoHour(getDuration())+
                "\nFirst class price: €" + getFirstClassPrice() + " | Second class price: €" + getSecondClassPrice() ;


        for (Route route : routes) {
            connections+="\n- "+routes;

        }
        return connections;
    }
}



