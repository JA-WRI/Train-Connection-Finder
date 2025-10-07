import java.util.List;

public class Main {
    public static void main(String[] args) {
        RouteCatalogue catalogue = new RouteCatalogue();

        // Load the CSV file
        catalogue.loadRoutesFromCSV("data/eu_rail_network.csv");

        // Get all the routes (TEST)
        List<Route> allRoutes = catalogue.getAllRoutes();
        System.out.println("Total routes: " + allRoutes.size());

        for (Route route : allRoutes) {
            System.out.println(route.toString());
        }
    }
}