import java.util.ArrayList;
import java.util.List;

public class SearchConnections {

    public static List<Connection> searchByCity(String departureCity, String arrivalCity, RouteCatalogue allRoutes){
        List<Connection> foundConnections = new ArrayList<>();

        List<Route> routesFromDeparture = allRoutes.getRoutesCatalogue().get(departureCity);

        if (routesFromDeparture == null) return foundConnections;

        for (Route firstLeg : routesFromDeparture) {
                String firstStop = firstLeg.getArrivalCity();

                List<Route> secondLegs = allRoutes.getRoutesCatalogue().get(firstStop);
                if (secondLegs == null) continue;

                for (Route secondLeg : secondLegs) {
                    String secondStop = secondLeg.getArrivalCity();

                    // 1-stop route
                    if (secondStop.equalsIgnoreCase(arrivalCity)) {
                        Connection connection = new Connection(List.of(firstLeg, secondLeg));
                        foundConnections.add(connection);
                    } else {
                        // Check for 2-stop route
                        List<Route> thirdLegs = allRoutes.getRoutesCatalogue().get(secondStop);
                        if (thirdLegs == null) continue;

                        for (Route thirdLeg : thirdLegs) {
                            if (thirdLeg.getArrivalCity().equalsIgnoreCase(arrivalCity)) {
                                Connection connection = new Connection(List.of(firstLeg, secondLeg, thirdLeg));
                                foundConnections.add(connection);
                            }
                        }
                    }
                }
            }
        return foundConnections;

    }

    public static List<Connection> searchDirectConnections(String departureCity, String arrivalCity, RouteCatalogue allRoutes) {
        List<Connection> connections = new ArrayList<>();
        List<Route> routesDepartingFromCity = allRoutes.getRoutesCatalogue().get(departureCity);

        if (routesDepartingFromCity != null) {
            // Get routes that depart from this city (directly from the map)
            for (Route route: routesDepartingFromCity){
                if(route.getArrivalCity().equalsIgnoreCase(arrivalCity)){
                    Connection connection = new Connection(List.of(route));
                    connections.add(connection);
                }
            }
        }
        return connections;
    }

}
