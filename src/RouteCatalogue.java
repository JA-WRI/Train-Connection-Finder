import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalTime;
import java.util.*;

public class RouteCatalogue {

    // Attributes
    private List<Route> routes;
    private Map<String, List<Route>> departureMap;

    // Constructor
    public RouteCatalogue() {
        this.routes = new ArrayList<>();
        this.departureMap = new HashMap<>();
    }

    // Method to load the Route from the CSV into Route list
    public void loadRoutesFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip row with column names
                }

                String[] fields = parseCSVLine(line);

                if (fields.length == 9) {
                    LocalTime departureTime = parseTime(fields[3].trim());
                    LocalTime arrivalTime = parseTime(fields[4].trim());
                    List<String> daysOfOperation = parseDays(fields[6].trim());
                    boolean nextDayArrival = hasNextDayArrival(fields[4].trim());

                    Route route = new Route(
                            fields[0].trim(), // routeID
                            fields[1].trim(), // departureCity
                            fields[2].trim(), // arrivalCity
                            departureTime,    // departureTime
                            arrivalTime,      // arrivalTime
                            fields[5].trim(), // trainType
                            daysOfOperation,  // daysOfOperation
                            Double.parseDouble(fields[7].trim()), // firstClass
                            Double.parseDouble(fields[8].trim()), // secondClass
                            nextDayArrival
                    );
                    routes.add(route);
                }
            }

            System.out.println("Loaded " + routes.size() + " routes from CSV");
            buildDepartureMap();

        } catch (Exception e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }
    }

    // Method to make a map to search by departure city
    private void buildDepartureMap() {
        departureMap.clear();
        for (Route route : routes) {
            String city = route.getDepartureCity();
            departureMap.computeIfAbsent(city, k -> new ArrayList<>()).add(route);
        }
    }

    // Method to parse String into LocalTime
    private LocalTime parseTime(String timeStr) {
        String time = timeStr.split(" ")[0];
        return LocalTime.parse(time);
    }

    // Method to check if train arrives the next day or not (check for +1d)
    private boolean hasNextDayArrival(String timeStr) {
        return timeStr.contains("(+1d)");
    }

    // Method to add days of operation into String list
    private List<String> parseDays(String daysStr) {
        List<String> daysList = new ArrayList<>();

        if (daysStr.equalsIgnoreCase("Daily")) {
            // Add all days
            String[] allDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            daysList.addAll(Arrays.asList(allDays));
        } else if (daysStr.contains("-")) {
            // Handle ranges like "Mon-Fri"
            parseDayRange(daysStr, daysList);
        } else {
            // Handle days separated by commas like "Mon,Wed,Fri"
            String[] days = daysStr.split(",");
            for (String day : days) {
                daysList.add(day.trim());
            }
        }
        return daysList;
    }

    // Method to add days in String array between min and max of range
    private void parseDayRange(String range, List<String> daysList) {
        String[] parts = range.split("-");
        if (parts.length == 2) {
            String startDay = parts[0].trim();
            String endDay = parts[1].trim();

            String[] dayOrder = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

            int startIndex = -1;
            int endIndex = -1;

            // Find indices of start and end days
            for (int i = 0; i < dayOrder.length; i++) {
                if (dayOrder[i].equalsIgnoreCase(startDay)) {
                    startIndex = i;
                }
                if (dayOrder[i].equalsIgnoreCase(endDay)) {
                    endIndex = i;
                }
            }

            // Add all days in the range
            if (startIndex != -1 && endIndex != -1) {
                if (startIndex <= endIndex) {
                    daysList.addAll(Arrays.asList(dayOrder).subList(startIndex, endIndex + 1));
                } else {
                    // Handle day range
                    daysList.addAll(Arrays.asList(dayOrder).subList(startIndex, dayOrder.length));
                    daysList.addAll(Arrays.asList(dayOrder).subList(0, endIndex + 1));
                }
            }
        }
    }

    // Method to separate row into an array
    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }

    // Method to get all the routes from the catalogue
    public List<Route> getAllRoutes() {
        return new ArrayList<>(routes);
    }

    // Method to get all routes departing from a specific city
    public List<Route> getRoutesFromCity(String city) {
        return departureMap.getOrDefault(city, new ArrayList<>());
    }
}