import jdk.jfr.FlightRecorder;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        RouteCatalogue catalogue = new RouteCatalogue();
        CSVReader.loadRoutesFromCSV("data/eu_rail_network.csv", catalogue);
        ConnectionFinder findConnections = new ConnectionFinder(catalogue);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter departure city: ");
        String departureCity = scanner.nextLine();

        System.out.println("Enter arrival city: ");
        String arrivalCity = scanner.nextLine();

        List<Connection> connections = findConnections.searchDirectConnections(departureCity, arrivalCity);

        if (connections.isEmpty()) {
            System.out.println("No direct connections found.");
            connections = findConnections.searchIndirectConnections(departureCity, arrivalCity);
        }

        if (connections.isEmpty()) {
            System.out.println("No indirect connections found");
        } else {
            System.out.print("Found " + connections.size());
            String connectionType = connections.get(0).getNumOfRoutes() == 1 ? " direct" : " indirect";
            System.out.println(connectionType + " connection: \n");
            for (Connection connection: connections){
                System.out.println(connection);
                System.out.println("-----");
            }

        }
        scanner.close();
    }
}