import java.util.List;

public class Main {
    public static void main(String[] args) {
        RouteCatalogue catalogue = new RouteCatalogue();
        Connection connect = new Connection();

        // Load the CSV file
        catalogue.loadRoutesFromCSV("data/eu_rail_network.csv");

        // Get all the routes (TEST)
        List<Route> allRoutes = catalogue.getAllRoutes();
        System.out.println("Total routes: " + allRoutes.size());

//        for (Route route : allRoutes) {
//            System.out.println(route.toString());
//        }


        List<Route> connection = connect.searchConnections("Amsterdam", "Bruges", catalogue);

        for(Route route: connection){
            System.out.println(route);
        }


    }
}