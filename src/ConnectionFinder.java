import java.util.ArrayList;
import java.util.List;

public class ConnectionFinder {
    private final RouteCatalogue routeCatalogue;

    public ConnectionFinder(RouteCatalogue routeCatalogue) {
        this.routeCatalogue = routeCatalogue;
    }

    public List<Connection> searchConnections(String departureCity, String arrivalCity){
        List<Connection> connections = new ArrayList<>();
        List<Route> routesDepartingFromCity = routeCatalogue.getRoutesCatalogue().get(departureCity);

        if (routesDepartingFromCity != null) {
            // Get routes that depart from this city (directly from the map)
            for (Route route: routesDepartingFromCity){
                if(route.getArrivalCity().equalsIgnoreCase(arrivalCity)){
                    Connection connection = new Connection(List.of(route));
                    connections.add(connection);
                }
            }
        }
        if(connections.isEmpty()){
            connections = searchIndirectConnections(departureCity, arrivalCity);
        }
        return connections;
    }

    public List<Connection> searchIndirectConnections(String departureCity, String arrivalCity){
        List<Connection> foundConnections = new ArrayList<>();

        List<Route> routesFromDeparture = routeCatalogue.getRoutesCatalogue().get(departureCity.toLowerCase());

        if (routesFromDeparture == null) return foundConnections;

        for (Route firstLeg : routesFromDeparture) {
            String firstStop = firstLeg.getArrivalCity();

            List<Route> secondLegs = routeCatalogue.getRoutesCatalogue().get(firstStop);
            if (secondLegs == null) continue;

            for (Route secondLeg : secondLegs) {
                String secondStop = secondLeg.getArrivalCity();

                // 1-stop route
                if (secondStop.equalsIgnoreCase(arrivalCity)) {
                    Connection connection = new Connection(List.of(firstLeg, secondLeg));
                    foundConnections.add(connection);
                } else {
                    // Check for 2-stop route
                    List<Route> thirdLegs = routeCatalogue.getRoutesCatalogue().get(secondStop);
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

}
