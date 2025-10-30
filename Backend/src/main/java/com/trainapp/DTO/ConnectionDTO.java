package com.trainapp.DTO;
import com.trainapp.model.Connection;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionDTO {

    public String departureTime;
    public String arrivalTime;
    public String departureCity;
    public String arrivalCity;
    public String duration;
    public int stops;
    public double price;
    public String cabinType;
    public boolean detailsAvailable;
    public List<RouteDTO> routes;

    public ConnectionDTO(Connection c, boolean firstClass) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        this.departureTime = c.getDepartureTime().format(fmt);
        this.arrivalTime = c.getArrivalTime().format(fmt);
        this.departureCity = c.getDepartureCity();
        this.arrivalCity = c.getArrivalCity();
        this.stops = c.getNumOfRoutes()-1 ; // 0 = direct
        this.detailsAvailable = c.getRoutes() != null && !c.getRoutes().isEmpty();
        this.duration = c.convertIntoHour(c.getDuration());

        if (firstClass) {
            this.price = c.getFirstClassPrice();
            this.cabinType = "First Class";
        } else {
            this.price = c.getSecondClassPrice();
            this.cabinType = "Standard";
        }
        if (c.getRoutes() != null) {
            this.routes = c.getRoutes().stream()
                    .map(RouteDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCabinType() {
        return cabinType;
    }

    public void setCabinType(String cabinType) {
        this.cabinType = cabinType;
    }

    public boolean isDetailsAvailable() {
        return detailsAvailable;
    }

    public void setDetailsAvailable(boolean detailsAvailable) {
        this.detailsAvailable = detailsAvailable;
    }
}
