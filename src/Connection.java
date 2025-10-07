import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Connection {
    private List<Route> routes;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Double duration;
    private Double price;
    private String departureCity;
    private String arrivalCity;
    private int numOfRoutes;
    //Add parameter for Routes

    public Connection(){

    }
    public Connection(List<Route> routes, LocalTime departureTime, LocalTime arrivalTime, Double duration, Double price, String departureCity, String arrivalCity, int numOdRoutes) {
        this.routes = routes;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.price = price;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.numOfRoutes = numOdRoutes;
    }

    public List<Route> getRoutes() {return routes;}

    public List<Route> setRoutes(List<Route> routes) {return this.routes = routes;}

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

    public List<Route> searchConnections(String departureCity, String arrivalCity, RouteCatalogue catalogue) {
        List<Route> connections = new ArrayList<>();
        for (Route route : catalogue.getAllRoutes()) {
            if (route.getDepartureCity().equalsIgnoreCase(departureCity) && route.getArrivalCity().equalsIgnoreCase(arrivalCity)) {
                connections.add(route);
            }
        }
        return connections;
    }


}
