import java.util.ArrayList;
import java.util.List;

public class SearchConnections {

    public static List<List<Route>> searchByCity(String departureCity, String arrivalCity, RouteCatalogue allRoutes){
        List<List<Route>>  foundRoutes= new ArrayList<>();

        List<Route> routesFromDeparture = allRoutes.getRoutesCatalogue().get(departureCity);

            for (Route firstLeg : routesFromDeparture) {
                String firstStop = firstLeg.getArrivalCity();

                List<Route> secondLegs = allRoutes.getRoutesCatalogue().get(firstStop);
                if (secondLegs == null) continue;

                for (Route secondLeg : secondLegs) {
                    String secondStop = secondLeg.getArrivalCity();

                    // 1-stop route
                    if (secondStop.equalsIgnoreCase(arrivalCity)) {
                        foundRoutes.add(List.of(firstLeg, secondLeg));
                    } else {
                        // Check for 2-stop route
                        List<Route> thirdLegs = allRoutes.getRoutesCatalogue().get(secondStop);
                        if (thirdLegs == null) continue;

                        for (Route thirdLeg : thirdLegs) {
                            if (thirdLeg.getArrivalCity().equalsIgnoreCase(arrivalCity)) {
                                foundRoutes.add(List.of(firstLeg, secondLeg, thirdLeg));
                            }
                        }
                    }
                }
            }
        return foundRoutes;

    }

    public static List<Route> searchDirectConnections(String departureCity, String arrivalCity, List<Route> routesDepartingFromCity) {
        List<Route> connections = new ArrayList<>();

        // Get routes that depart from this city (directly from the map)
        for (Route route: routesDepartingFromCity){
            if(route.getDepartureCity().equalsIgnoreCase(departureCity) && route.getDepartureCity().equalsIgnoreCase(arrivalCity)){
                connections.add(route);
            }
        }
        return connections;
    }

}
