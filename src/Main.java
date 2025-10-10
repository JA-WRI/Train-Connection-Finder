import jdk.jfr.FlightRecorder;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        RouteCatalogue catalogue = new RouteCatalogue();
        ConnectionFinder findConnections = new ConnectionFinder(catalogue);
        filterConnections filteredConnections = new filterConnections();

        Scanner scanner = new Scanner(System.in);
        int ticketChoice = 0;

        System.out.print("Enter departure city: ");
        String departureCity = scanner.nextLine();

        System.out.print("Enter arrival city: ");
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
        System.out.println(" ");
        System.out.print("Do you want to filter your result? (y/n)");
        String isFilter = scanner.nextLine();

        if (isFilter.equalsIgnoreCase("y")){
            String filterChoice = "";
            while (!filterChoice.equals("10")) {
                System.out.println("Filter menu:");
                System.out.println("--------------------------------------------------");
                System.out.println("1. Sort tickets from low to high based on price.");
                System.out.println("2. Sort tickets from high to low based on price.");
                System.out.println("3. Display tickets under a certain price.");
                System.out.println("4. Sort tickets from shortest to longest duration.");
                System.out.println("5. Sort tickets from longest to shortest duration.");
                System.out.println("6. Display tickets under a certain duration.");
                //Add Emy's filters
                System.out.println("10. Exit.");
                System.out.println("--------------------------------------------------");

                System.out.print("Select a filter form the menu(enter the number ex: 1: ");
                filterChoice = scanner.nextLine();
                switch (filterChoice) {
                    case "1":
                        System.out.println("1. First Class ticket.");
                        System.out.println("2. Second Class ticket.");
                        System.out.println("--------------------------------------------------");
                        System.out.println(" ");
                        System.out.print("Which class of ticket do you want (enter the number ex: 1)? ");
                        ticketChoice = scanner.nextInt();
                        if (ticketChoice == 1) {
                            //Emy add function here
                        } else if (ticketChoice == 2) {
                            //Emy add function here
                        }
                        break;
                    case "2":
                        System.out.println("1. First Class ticket.");
                        System.out.println("2. Second Class ticket.");
                        System.out.println("--------------------------------------------------");
                        System.out.println(" ");
                        System.out.print("Which class of ticket do you want (enter the number ex: 1)? ");
                        ticketChoice = scanner.nextInt();
                        if (ticketChoice == 1) {
                            //Emy add function here
                        } else if (ticketChoice == 2) {
                            //Emy add function here
                        }
                        break;
                    case "3":
                        System.out.println("1. First Class ticket.");
                        System.out.println("2. Second Class ticket.");
                        System.out.println("--------------------------------------------------");
                        System.out.println(" ");
                        System.out.print("Which class of ticket do you want? (enter the number ex: 1)");
                        ticketChoice = scanner.nextInt();
                        System.out.print("Enter the maximum price (round number ex: 2000): ");
                        int maxPrice = scanner.nextInt();
                        if (ticketChoice == 1) {
                            connections = filteredConnections.filterPriceFirstClass(maxPrice, connections);
                        } else if (ticketChoice == 2) {
                            connections = filteredConnections.filterPriceSecondClass(maxPrice, connections);
                        }
                        break;
                    case "4":
                        connections = filteredConnections.AscenSortDuration(connections);
                        break;
                    case "5":
                        connections = filteredConnections.DescenSortDuration(connections);
                        break;
                    case "6":
                        System.out.print("Enter the maximum duration (round hours ex: 2)");
                        int maxHours = scanner.nextInt();
                        connections = filteredConnections.filterDuration(maxHours, connections);
                        break;
                    case "10":
                        System.out.println("Exit filters");
                        break;
                    default:
                        System.out.print("Please enter a valid input:");
                        filterChoice = scanner.nextLine();
                        break;
                }
                if (connections == null) {
                    System.out.println("Non connections found under the given filter");
                } else {

                    for (Connection connection : connections) {
                        System.out.println(connection);
                        System.out.println("-----");
                    }
                }
                System.out.println("Select a filter form the menu(enter the number ex: 1): ");
                filterChoice = scanner.nextLine();
            }


        }else if (isFilter.equalsIgnoreCase("n")){
            System.out.print("Please select a ticket");
        }else {
            System.out.print("Please enter a valid input");
        }

        scanner.close();

    }
}