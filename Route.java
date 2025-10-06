import java.time.LocalTime;

public class Route {
    private String routeID;
    private String departureCity;
    private String arrivalCity;
    private String trainType;
    private String daysOfOperation;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private double firstClassTicketRate;
    private double secondClassTicketRate;

    public Route(String routeID, String departureCity, String arrivalCity, String trainType, String daysOfOperation, LocalTime arrivalTime, LocalTime departureTime, double firstClassTicketRate, double secondClassTicketRate) {
        this.routeID = routeID;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.trainType = trainType;
        this.daysOfOperation = daysOfOperation;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.firstClassTicketRate = firstClassTicketRate;
        this.secondClassTicketRate = secondClassTicketRate;
    }

    public String getRouteID() {
        return routeID;
    }

    public double getSecondClassTicketRate() {
        return secondClassTicketRate;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public double getFirstClassTicketRate() {
        return firstClassTicketRate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public String getDaysOfOperation() {
        return daysOfOperation;
    }

    public String getTrainType() {
        return trainType;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public void setSecondClassTicketRate(double secondClassTicketRate) {
        this.secondClassTicketRate = secondClassTicketRate;
    }

    public void setFirstClassTicketRate(double firstClassTicketRate) {
        this.firstClassTicketRate = firstClassTicketRate;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setDaysOfOperation(String daysOfOperation) {
        this.daysOfOperation = daysOfOperation;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
