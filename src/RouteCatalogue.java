import java.util.*;

public class RouteCatalogue {

    //changed the dataStructure to a HashMap where the key is the departure city and the key is a list of all the
    //routes from that departure city.
    private final Map<String, List<Route>> routesCatalogue;

    public RouteCatalogue() {
        this.routesCatalogue = new HashMap<>();
    }


    public Map<String, List<Route>> getRoutesCatalogue() {
        return routesCatalogue;
    }

    public int getTotalRoutes() {
        return routesCatalogue.values().stream().mapToInt(List::size).sum();
    }
}