package utils;

import database.DatabaseConnection;
import database.DatabaseInitializer;
import model.Route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {

    // Method to load the routes from CSV into SQLite database
    public static void loadRoutesFromCSVToDatabase(String filename){
        DatabaseInitializer.initializeDatabase();

        try (java.sql.Connection connection = DatabaseConnection.getConnection();
             BufferedReader br = new BufferedReader(new FileReader(filename))){

            String line;
            boolean firstLine = true;

            // Clear existing data
            try (Statement stmt = connection.createStatement()){
                stmt.execute("DELETE FROM routes");
            }

            int loadedCount = 0;

            while ((line = br.readLine()) != null){
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] fields = parseCSVLine(line);

                if (fields.length == 9) {
                    insertRouteIntoDatabase(connection, fields);
                    loadedCount++;
                }
            }

            System.out.println("Loaded " + loadedCount + " routes into database");

        } catch (Exception e) {
            System.out.println("Error loading CSV into database: " + e.getMessage());
        }
    }

    private static void insertRouteIntoDatabase(java.sql.Connection connection, String[] fields) throws SQLException{
        String insertRouteSQL = """
            INSERT INTO routes (route_id, departure_city, arrival_city, departure_time, 
                               arrival_time, train_type, days_of_operation, first_class_price, second_class_price, arrives_next_day)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertRouteSQL)){
            preparedStatement.setString(1, fields[0].trim());
            preparedStatement.setString(2, fields[1].trim().toLowerCase());
            preparedStatement.setString(3, fields[2].trim().toLowerCase());
            preparedStatement.setTime(4, java.sql.Time.valueOf(LocalTime.parse(fields[3].trim().split(" ")[0])));
            preparedStatement.setTime(5, java.sql.Time.valueOf(LocalTime.parse(fields[4].trim().split(" ")[0])));
            preparedStatement.setString(6, fields[5].trim());
            preparedStatement.setString(7, fields[6].trim());
            preparedStatement.setDouble(8, Double.parseDouble(fields[7].trim()));
            preparedStatement.setDouble(9, Double.parseDouble(fields[8].trim()));
            preparedStatement.setInt(10, fields[4].contains("(+1d)") ? 1 : 0);

            preparedStatement.executeUpdate();
        }

    }

    // Method to parse String into LocalTime
    private static LocalTime parseTime(String timeStr) {
        String time = timeStr.split(" ")[0];
        return LocalTime.parse(time);
    }

    // Method to check if train arrives the next day or not (check for +1d)
    private static boolean hasNextDayArrival(String timeStr) {
        return timeStr.contains("(+1d)");
    }

    // Method to add days of operation into String list
    private static List<String> parseDays(String daysStr) {
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
    private static void parseDayRange(String range, List<String> daysList) {
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
    private static String[] parseCSVLine(String line) {
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
}