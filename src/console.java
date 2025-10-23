import model.Connection;
import services.ConnectionFinder;
import utils.CSVReader;
import utils.filterConnections;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class console {

    public console(){}

    public void startProgram(){
        System.out.println("Starting Railway System Database Setup...");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // This will create the database and load all routes from CSV
        CSVReader.loadRoutesFromCSVToDatabase("data/eu_rail_network.csv");
        System.out.println("Database will be at: " + new java.io.File("../railway_system.db").getAbsolutePath());

        System.out.println("Database setup complete YIPPEE YIPPEE");
    }

    public List<Connection> searchConnections() {
        Scanner scanner = new Scanner(System.in);
        List<Connection> foundConnections = findFromDepartureAndArrivalCity();

        System.out.println();
        System.out.print("Do you want to filter your results? (y/n): ");
        String isFilter = scanner.nextLine();

        if (isFilter.equalsIgnoreCase("y")) {
            return filterAllFoundConnections(foundConnections);
        }
        scanner.close();
        return foundConnections;

    }


    public List<Connection> findFromDepartureAndArrivalCity(){
        ConnectionFinder findConnections = new ConnectionFinder();

        Scanner scanner = new Scanner(System.in);


        System.out.print("Enter departure city: ");
        String departureCity = scanner.nextLine();

        System.out.print("Enter arrival city: ");
        String arrivalCity = scanner.nextLine();

        List<Connection> connections = findConnections.searchDirectConnections(departureCity, arrivalCity);

        if (connections.isEmpty()) {
            System.out.println("No direct connections found. Searching for indirect connections");
            connections = findConnections.searchIndirectConnections(departureCity, arrivalCity);
        }

        if (connections.isEmpty()) {
            System.out.println("No connections found.");
            scanner.close();
        }
        return connections;

    }

    public List<Connection> filterAllFoundConnections(List<Connection> connections) {
        Scanner scanner = new Scanner(System.in);
        filterConnections filteredConnections = new filterConnections(); // Fixed class name casing

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

            int ticketChoice = 0;
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
        }
        return connections;
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
