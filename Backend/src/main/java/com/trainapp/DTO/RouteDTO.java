package com.trainapp.DTO;

import com.trainapp.model.Route;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RouteDTO {
    public String departureCity;
    public String arrivalCity;
    public String departureTime;
    public String arrivalTime;
    public String trainType;
    public List<String> daysOfOperation;
    public double firstClassTicketRate;
    public double secondClassTicketRate;

    public RouteDTO(Route r) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        this.departureCity = r.getDepartureCity();
        this.arrivalCity = r.getArrivalCity();
        this.departureTime = r.getDepartureTime().format(fmt);
        this.arrivalTime = r.getArrivalTime().format(fmt);
        this.trainType = r.getTrainType();
        this.daysOfOperation = r.getDaysOfOperation();
        this.firstClassTicketRate = r.getFirstClassTicketRate();
        this.secondClassTicketRate = r.getSecondClassTicketRate();
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

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public List<String> getDaysOfOperation() {
        return daysOfOperation;
    }

    public void setDaysOfOperation(List<String> daysOfOperation) {
        this.daysOfOperation = daysOfOperation;
    }

    public double getFirstClassTicketRate() {
        return firstClassTicketRate;
    }

    public void setFirstClassTicketRate(double firstClassTicketRate) {
        this.firstClassTicketRate = firstClassTicketRate;
    }

    public double getSecondClassTicketRate() {
        return secondClassTicketRate;
    }

    public void setSecondClassTicketRate(double secondClassTicketRate) {
        this.secondClassTicketRate = secondClassTicketRate;
    }
}
