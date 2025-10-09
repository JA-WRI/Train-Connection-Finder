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
            // to add similar logic but for first and second class ticket price
        }

        this.duration = calculateTripDuration();
    }

    public Connection(LocalTime departureTime, LocalTime arrivalTime, Double duration, Double firstClassPrice, Double secondClassPrice, String departureCity, String arrivalCity, int numOdRoutes, List<Route> routes) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.firstClassPrice = firstClassPrice;
        this.secondClassPrice = secondClassPrice;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.numOfRoutes = numOdRoutes;
        this.routes = routes;
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
        return firstClassPrice;
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

}
