import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        RouteCatalogue catalogue = new RouteCatalogue();
        Connection connections = new Connection();
        CSVReader.loadRoutesFromCSV("data/eu_rail_network.csv", catalogue);

        List<List<Route>> thisIsAllTheRoutes = SearchConnections.searchByCity("Amsterdam", "Dresden", catalogue);
        if (thisIsAllTheRoutes.isEmpty()){
            System.out.println("No Connections Found");
        }
        else{
        for (List<Route> routePath : thisIsAllTheRoutes) {
            System.out.println("Route found:");
            for (Route route : routePath) {
                System.out.println(route);
            }
            System.out.println("-----");
        }
        }

        // Get all the routes (TEST)
//        Map<String, List<Route>> allRoutes = catalogue.getRoutesCatalogue();

//        for (Route route : allRoutes) {
//            System.out.println(route.toString());
//        }

//        int count = 0;
//        for (List<Route> value : allRoutes.values()){
//            for (Route route: value){
//                count +=1;
//            }
//
//        }
//        System.out.println(count);

//        List<Route> connection = connections.searchConnections("Amsterdam", "Bruges", catalogue);
//
//        for(Route route: connection){
//            System.out.println(route);
//        }


    }
}