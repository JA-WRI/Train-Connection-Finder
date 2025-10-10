import java.util.*;

public class RouteCatalogue {

    private final Map<String, List<Route>> routesCatalogue = new HashMap<>();

    public RouteCatalogue(String csvFilePath) {
        CSVReader.loadRoutesFromCSV(csvFilePath, this);
    }
    public RouteCatalogue(){
        CSVReader.loadRoutesFromCSV("data/eu_rail_network.csv", this);
    }

    public Map<String, List<Route>> getRoutesCatalogue() {
        return routesCatalogue;
    }

    public int getTotalRoutes() {
        return routesCatalogue.values().stream().mapToInt(List::size).sum();
    }
}