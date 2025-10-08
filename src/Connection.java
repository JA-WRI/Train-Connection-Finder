import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Connection {
    LocalTime departureTime;
    LocalTime arrivalTime;
    Double duration;
    Double price;
    String departureCity;
    String arrivalCity;
    int numOfRoutes;
    List<Route> routes;

    public Connection(){

    }
    public Connection(LocalTime departureTime, LocalTime arrivalTime, Double duration, Double price, String departureCity, String arrivalCity, int numOdRoutes) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.price = price;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.numOfRoutes = numOdRoutes;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public int getNumOdRoutes() {
        return numOfRoutes;
    }

    public void setNumOdRoutes(int numOdRoutes) {
        this.numOfRoutes = numOdRoutes;
    }

}
