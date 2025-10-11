import jdk.jfr.FlightRecorder;

import java.time.LocalTime;
import java.util.*;

import java.util.List;
import java.util.Scanner;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        RouteCatalogue catalogue = new RouteCatalogue();
        ConnectionFinder findConnections = new ConnectionFinder(catalogue);
        filterConnections filteredConnections = new filterConnections(); // Fixed class name casing

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
            System.out.println("No connections found.");
            scanner.close();
            return; // Exit early if no connections found
        }

        // Fixed: Check if connections is not empty before calling getFirst()
        System.out.print("Found " + connections.size());
        String connectionType = connections.getFirst().getNumOfRoutes() == 1 ? " direct" : " indirect";
        System.out.println(connectionType + " connection(s): \n");
        for (Connection connection : connections) {
            System.out.println(connection);
            System.out.println("-----");
        }

        System.out.println();
        System.out.print("Do you want to filter your results? (y/n): ");
        String isFilter = scanner.nextLine();

        if (isFilter.equalsIgnoreCase("y")) {
            String filterChoice = "";
            while (!filterChoice.equals("11")) {
                System.out.println("Filter menu:");
                System.out.println("--------------------------------------------------");
                System.out.println("1. Sort tickets from low to high based on price.");
                System.out.println("2. Sort tickets from high to low based on price.");
                System.out.println("3. Display tickets under a certain price.");
                System.out.println("4. Sort tickets from shortest to longest duration.");
                System.out.println("5. Sort tickets from longest to shortest duration.");
                System.out.println("6. Display tickets under a certain duration.");
                System.out.println("7. Select departure time.");
                System.out.println("8. Select arrival time.");
                System.out.println("9. Select day of departure.");
                System.out.println("10. Select day of arrival.");
                System.out.println("11. Exit.");
                System.out.println("--------------------------------------------------");

                System.out.print("Select a filter from the menu (enter the number ex: 1): ");
                filterChoice = scanner.nextLine();

                // Consume leftover newline characters for numeric inputs
                if (filterChoice.matches("\\d+") && Integer.parseInt(filterChoice) >= 1 && Integer.parseInt(filterChoice) <= 3) {
                    scanner.nextLine(); // Consume leftover newline
                }

                switch (filterChoice) {
                    case "1":
                        System.out.println("1. First Class ticket.");
                        System.out.println("2. Second Class ticket.");
                        System.out.println("--------------------------------------------------");
                        System.out.println();
                        System.out.print("Which class of ticket do you want (enter the number ex: 1)? ");
                        ticketChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (ticketChoice == 1) {
                            connections = filteredConnections.sortByFirstClassPriceAscending(connections);
                        } else if (ticketChoice == 2) {
                            connections = filteredConnections.sortBySecondClassPriceAscending(connections);
                        }
                        break;
                    case "2":
                        System.out.println("1. First Class ticket.");
                        System.out.println("2. Second Class ticket.");
                        System.out.println("--------------------------------------------------");
                        System.out.println();
                        System.out.print("Which class of ticket do you want (enter the number ex: 1)? ");
                        ticketChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (ticketChoice == 1) {
                            connections = filteredConnections.sortByFirstClassPriceDescending(connections);
                        } else if (ticketChoice == 2) {
                            connections = filteredConnections.sortBySecondClassPriceDescending(connections);
                        }
                        break;
                    case "3":
                        System.out.println("1. First Class ticket.");
                        System.out.println("2. Second Class ticket.");
                        System.out.println("--------------------------------------------------");
                        System.out.println();
                        System.out.print("Which class of ticket do you want? (enter the number ex: 1): ");
                        ticketChoice = scanner.nextInt();
                        System.out.print("Enter the maximum price (round number ex: 2000): ");
                        int maxPrice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
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
                        System.out.print("Enter the maximum duration (round hours ex: 2): ");
                        int maxHours = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        connections = filteredConnections.filterDuration(maxHours, connections);
                        break;
                    case "7":
                        System.out.print("Enter departure time (HH:MM format, ex: 14:30): ");
                        String departureTimeStr = scanner.nextLine();
                        try {
                            LocalTime departureTime = LocalTime.parse(departureTimeStr);
                            connections = filteredConnections.filterByDepartureTime(connections, departureTime);
                            System.out.println("Filtered by departure time: " + departureTime);
                        } catch (Exception e) {
                            System.out.println("Invalid time format! Please use HH:MM format (ex: 14:30)");
                        }
                        break;

                    case "8":
                        System.out.print("Enter arrival time (HH:MM format, ex: 16:45): ");
                        String arrivalTimeStr = scanner.nextLine();
                        try {
                            LocalTime arrivalTime = LocalTime.parse(arrivalTimeStr);
                            connections = filteredConnections.filterByArrivalTime(connections, arrivalTime);
                            System.out.println("Filtered by arrival time: " + arrivalTime);
                        } catch (Exception e) {
                            System.out.println("Invalid time format! Please use HH:MM format (ex: 16:45)");
                        }
                        break;

                    case "9":
                        System.out.println("Available days: Mon, Tue, Wed, Thu, Fri, Sat, Sun");
                        System.out.print("Enter day of departure (ex: Mon): ");
                        String departureDay = scanner.nextLine().trim();
                        if (isValidDay(departureDay)) {
                            connections = filteredConnections.filterByDayOfDeparture(connections, departureDay);
                            System.out.println("Filtered by departure day: " + departureDay);
                        } else {
                            System.out.println("Invalid day! Please use: Mon, Tue, Wed, Thu, Fri, Sat, Sun");
                        }
                        break;

                    case "10":
                        System.out.println("Available days: Mon, Tue, Wed, Thu, Fri, Sat, Sun");
                        System.out.print("Enter day of arrival (ex: Fri): ");
                        String arrivalDay = scanner.nextLine().trim();
                        if (isValidDay(arrivalDay)) {
                            connections = filteredConnections.filterByDayOfArrival(connections, arrivalDay);
                            System.out.println("Filtered by arrival day: " + arrivalDay);
                        } else {
                            System.out.println("Invalid day! Please use: Mon, Tue, Wed, Thu, Fri, Sat, Sun");
                        }
                        break;

                    case "11":
                        System.out.println("Exit filters");
                        break;
                    default:
                        System.out.println("Please enter a valid input (1-11).");
                        break;
                }

                // Display filtered results if not exiting
                if (!filterChoice.equals("11")) {
                    if (connections == null || connections.isEmpty()) {
                        System.out.println("No connections found under the given filter");
                    } else {
                        System.out.println("Filtered results:");
                        for (Connection connection : connections) {
                            System.out.println(connection);
                            System.out.println("-----");
                        }
                    }
                }
            }

        } else if (isFilter.equalsIgnoreCase("n")) {
            System.out.println("Please select a ticket");
        } else {
            System.out.println("Please enter a valid input (y/n)");
        }

        scanner.close();
    }

    private static boolean isValidDay(String day) {
        String[] validDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String validDay : validDays) {
            if (validDay.equalsIgnoreCase(day)) {
                return true;
            }
        }
        return false;
    }
}