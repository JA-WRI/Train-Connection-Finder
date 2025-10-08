import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        RouteCatalogue catalogue = new RouteCatalogue();
        Connection connections = new Connection();
        CSVReader.loadRoutesFromCSV("data/eu_rail_network.csv", catalogue);


        // Get all the routes (TEST)
        Map<String, List<Route>> allRoutes = catalogue.getRoutesCatalogue();
        System.out.println("Total routes: " + allRoutes.size());

//        for (Route route : allRoutes) {
//            System.out.println(route.toString());
//        }


        List<Route> connection = connections.searchConnections("Amsterdam", "Bruges", catalogue);

        for(Route route: connection){
            System.out.println(route);
        }


    }
}